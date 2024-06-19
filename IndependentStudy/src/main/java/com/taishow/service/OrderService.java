package com.taishow.service;

import com.taishow.dao.*;
import com.taishow.dto.OrderDto;
import com.taishow.entity.*;
import com.taishow.myutil.QRCodeGenerator;
import com.taishow.myutil.Snowflake;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.DoActionObj;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final BonusRepository bonusRepository;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final SeatStatusRepository seatStatusRepository;
    private final Snowflake snowflake;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        PaymentRepository paymentRepository, BonusRepository bonusRepository,
                        TicketRepository ticketRepository, TicketTypeRepository ticketTypeRepository,
                        SeatStatusRepository seatStatusRepository, Snowflake snowflake) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.bonusRepository = bonusRepository;
        this.ticketRepository = ticketRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.seatStatusRepository = seatStatusRepository;
        this.snowflake = snowflake;
    }

    public List<TicketType> getTicketTypeDetail(Integer movieId){
        return ticketTypeRepository.findAll();
    }

    @Transactional
    public Map<String, String> createOrder(OrderDto orderDto, Integer movieId) {
        // 檢查票數是否為0張、大於6張
        if (orderDto.getSeatStatusId().isEmpty()) {
            throw new IllegalArgumentException("請先選擇座位");
        } else if (orderDto.getSeatStatusId().size() > 6) {
            throw new IllegalArgumentException("最多僅能購買六張票");
        }

        // 檢查票數跟票種是否相同
        if (orderDto.getSeatStatusId().size() != orderDto.getTicketTypeId().size()) {
            throw new IllegalArgumentException("請選擇等同座位數的票");
        }

        // 計算總金額和總扣除紅利
        int totalPrice = 0;
        int reduceBonusPoint = 0;

        // 會員持有紅利點數 (之後有JWT時，改為從user表獲取)
        int userBonusPoint = 1000;

        for (int i = 0; i < orderDto.getSeatStatusId().size(); i++) {
            TicketType ticketType = ticketTypeRepository.findById(orderDto.getTicketTypeId().get(i))
                    .orElseThrow(() -> new RuntimeException("無效的票種"));

            if ("紅利點數".equals(ticketType.getTicketType())) {
                // 紅利點數兌票需要250點
                final int requiredBonusPoints = 250;
                if (userBonusPoint >= requiredBonusPoints) {
                    userBonusPoint -= requiredBonusPoints;
                    reduceBonusPoint -= requiredBonusPoints;
                } else {
                    throw new RuntimeException("紅利點數不足");
                }
            } else {
                // 酌收10%手續費
                totalPrice += (int) (ticketType.getUnitPrice() * 1.1);
            }
        }

        // 取得現在時間
        Date now = new Date();

        // 建立訂單
        Orders orders = new Orders();
        orders.setUserId(1); // 等JWT token給我
        orders.setOrderNum(String.valueOf(snowflake.nextId()));
        orders.setOrderDate(now);
        orders.setTotalAmount(totalPrice);
        try {
            String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(orders.getOrderNum(), 350, 350);
            orders.setQrcode(qrCodeBase64);
        } catch (Exception e) {
            System.out.println("生成QR碼失敗: " + e.getMessage());
        }
        orderRepository.save(orders);

        // 建立支付紀錄
        Payment payment = new Payment();
        payment.setOrdersId(orders.getId());
        payment.setPayway(""); // 等金流回調，要記得更新這欄
        payment.setPayStatus(orderDto.getTotalAmount() != 0 ? "未付款" : "不需付款");
        payment.setMethod("付款");
        payment.setModifyTime(now);
        paymentRepository.save(payment);

        // 若使用紅利點數兌票，於此扣除紅利
        if (reduceBonusPoint < 0) {
            Bonus bonus = new Bonus();
            bonus.setPaymentId(payment.getId());
            bonus.setBonus(reduceBonusPoint);
            bonus.setModifyTime(now);
            bonusRepository.save(bonus);
        }

        // 建立電影票
        for (int i = 0; i < orderDto.getSeatStatusId().size(); i++) {
            Tickets tickets = new Tickets();
            tickets.setTicketTypeId(orderDto.getTicketTypeId().get(i));
            tickets.setSeatStatusId(orderDto.getSeatStatusId().get(i));
            tickets.setOrdersId(orders.getId());
            ticketRepository.save(tickets);
        }

        // 返回訂單詳情
        Map<String, String> orderDetail = new HashMap<>();
        orderDetail.put("totalPrice", String.valueOf(totalPrice));
        orderDetail.put("theaterId", orderDto.getTheaterId().toString());
        orderDetail.put("OrderNum", orders.getOrderNum());

        return orderDetail;
    }

    public String ecpayCheckout(Map<String, String> orderDetail) {
        AllInOne allInOne = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = simpleDateFormat.format(now);

        obj.setMerchantID("3002607");
        obj.setMerchantTradeNo(orderDetail.get("OrderNum"));
        obj.setMerchantTradeDate(formattedDate);
        obj.setTotalAmount(orderDetail.get("totalPrice"));
        obj.setTradeDesc("電影訂票");
        obj.setItemName("電影票");
        obj.setReturnURL("http://localhost:8080/ecpayCallback"); //付款完成通知回傳網址
        obj.setStoreID(orderDetail.get("theaterId")); //特店旗下店舖代號 (theaterId)
        obj.setClientBackURL("https://www.youtube.com/"); //Client端返回特店的按鈕連結
//        obj.setOrderResultURL(""); //Client端回傳付款結果網址，測試完成再改用這個
        obj.setNeedExtraPaidInfo("N");

        String form = allInOne.aioCheckOut(obj, null);

        return form;
    }

    @Transactional
    public void paymentSuccess(Hashtable<String, String> callbackData) {
        String orderNum = callbackData.get("MerchantTradeNo");

        Optional<Orders> ordersOptional = orderRepository.findByOrderNum(orderNum);
        if (ordersOptional.isEmpty()) {
            throw new RuntimeException("訂單不存在");
        }

        Orders orders = ordersOptional.get();
        Optional<Payment> paymentOptional = paymentRepository.findByOrdersId(orders.getId());

        if (paymentOptional.isEmpty()) {
            throw new RuntimeException("付款紀錄不存在");
        }

        Payment payment = paymentOptional.get();
        Date now = new Date();
        payment.setPayway(callbackData.get("PaymentType"));
        payment.setPayStatus("完成");
        payment.setPayTime(now);
        payment.setModifyTime(now);
        payment.setTradeNum(callbackData.get("TradeNo"));
        paymentRepository.save(payment);

        // 訂單成功，產生紅利
        Bonus bonus = new Bonus();
        bonus.setPaymentId(payment.getId());
        bonus.setBonus((int) (orders.getTotalAmount() * 0.1));
        bonus.setModifyTime(now);
        bonusRepository.save(bonus);
    }

    @Transactional
    public void paymentFailure(Hashtable<String, String> callbackData) {
        String orderNum = callbackData.get("MerchantTradeNo");

        Optional<Orders> ordersOptional = orderRepository.findByOrderNum(orderNum);
        if (ordersOptional.isEmpty()) {
            throw new RuntimeException("訂單不存在");
        }

        Orders orders = ordersOptional.get();
        Optional<Payment> paymentOptional = paymentRepository.findByOrdersId(orders.getId());

        if (paymentOptional.isEmpty()) {
            throw new RuntimeException("付款紀錄不存在");
        }

        Payment payment = paymentOptional.get();
        Date now = new Date();
        payment.setPayway(callbackData.get("PaymentType")); //須檢查訂單未付款時，該值回調為何
        payment.setPayStatus("付款失敗");
        payment.setModifyTime(now);
        payment.setTradeNum(callbackData.get("TradeNo"));
        paymentRepository.save(payment);

        // 紅利購票時，退回紅利點數
        List<Bonus> bonusList = bonusRepository.findByPaymentId(payment.getId());

        if (!bonusList.isEmpty()) {
            Bonus bonusRecord = bonusList.get(0);

            // 將扣除的bonus變回正數
            Integer revertBonus = Math.abs(bonusRecord.getBonus());

            // 寫入一筆退回的紀錄
            Bonus bonus = new Bonus();
            bonus.setPaymentId(payment.getId());
            bonus.setBonus(revertBonus);
            bonus.setModifyTime(now);
            bonusRepository.save(bonus);
        } else {
            System.out.println("該筆訂單未使用紅利點數購票");
        }

        // 刪除電影票、清空座位狀態
        List<Tickets> ticketsList = ticketRepository.findByOrdersId(orders.getId());

        if (ticketsList.isEmpty()) {
            throw new RuntimeException("電影票不存在");
        }

        ticketsList.forEach(tickets -> {
            try {
                seatStatusRepository.deleteById(tickets.getSeatStatusId());
            } catch (Exception e) {
                System.out.println("座位狀態刪除失敗: " + e.getMessage());
            }
            ticketRepository.deleteById(tickets.getId());
        });
    }

    //此處待後台實作退款才可以測試
    public boolean checkBuyTicketsOnlyUseBonus(Integer ordersId) {
        Optional<Orders> ordersOptional = orderRepository.findById(ordersId);

        if (ordersOptional.isEmpty()) {
            throw new RuntimeException("訂單不存在");
        }

        // 檢查是不是僅用紅利購票
        return ordersOptional.get().getTotalAmount().equals(0);
    }


    public void onlyRefundBonus(Integer ordersId) {
        Optional<Payment> paymentOptional = paymentRepository.findByOrdersId(ordersId);

        if (paymentOptional.isEmpty()) {
            throw new RuntimeException("付款紀錄不存在");
        }

        List<Bonus> bonusList = bonusRepository.findByPaymentId(paymentOptional.get().getId());

        if (bonusList.isEmpty()) {
            throw new RuntimeException("紅利單號不存在");
        }

        // 退回紅利點數
        Date now = new Date();
        Bonus bonus = new Bonus();
        bonus.setPaymentId(bonusList.get(0).getPaymentId());
        bonus.setBonus(bonusList.get(0).getBonus() * -1);
        bonus.setModifyTime(now);
        bonusRepository.save(bonus);
    }


    //建立退款單
    public Map<String, String> createRefund(Integer ordersId){
        Optional<Orders> ordersOptional = orderRepository.findById(ordersId);
        Optional<Payment> paymentOptional = paymentRepository.findByOrdersId(ordersId);

        if (ordersOptional.isEmpty() || paymentOptional.isEmpty()) {
            throw new RuntimeException("訂單不存在");
        }

        //是否需要返還紅利點數
        boolean isReturnBonus = true;

        //紅利點數等值金額 (會員將紅利點數使用掉，導致返還會變成負數)
        int bonusEquivalentAmount = 0;

        Orders orders = ordersOptional.get();
        Payment payment = paymentOptional.get();
        Optional<User> userOptional = userRepository.findById(orders.getUserId());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("會員不存在");
        }

        User user = userOptional.get();

        //計算退款後，會員總紅利點數 (在此先取得會員身上紅利點數)
        Integer estimatedBonusAfterReturn = user.getBonusPoint();

        List<Bonus> bonusList = bonusRepository.findByPaymentId(payment.getId());

        if (bonusList.isEmpty()) {
            throw new RuntimeException("紅利紀錄不存在");
        }

        //會員當前紅利 + 紅利點數購票 + 購票產生紅利
        //計算退款後，因此"紅利點數購票、購票產生紅利" * -1
        for (Bonus bonus : bonusList) {
            estimatedBonusAfterReturn -= bonus.getBonus();
            bonusEquivalentAmount -= bonus.getBonus();
        }

        if (estimatedBonusAfterReturn < 0) {
            isReturnBonus = false;
        }

        AllInOne allInOne = new AllInOne("");
        DoActionObj doActionObj = new DoActionObj();

        doActionObj.setMerchantID("3002607");
        doActionObj.setMerchantTradeNo(orders.getOrderNum());
        doActionObj.setTradeNo(payment.getTradeNum());
        doActionObj.setAction("R");
        doActionObj.setTotalAmount(isReturnBonus ? orders.getTotalAmount().toString() :
                String.valueOf(orders.getTotalAmount() + bonusEquivalentAmount));
        doActionObj.setPlatformID("");

        String response = allInOne.doAction(doActionObj);

        // 返回訂單詳情
        Map<String, String> refundDetail = new HashMap<>();
        refundDetail.put("response", response);
        refundDetail.put("isReturnBonus", String.valueOf(isReturnBonus));

        return refundDetail;
    }


    //*****POST給綠界退款請求，直接接收回調參數，異動資料庫*****
    @Transactional
    public void handleRefundResponse(Map<String, String> refundDetail) {
        // 解析response
        JSONObject jsonResponse = new JSONObject(refundDetail.get("response"));
        String merchantTradeNo = jsonResponse.getString("MerchantTradeNo");
        String rtnCode = jsonResponse.getString("RtnCode");
        String tradeNo = jsonResponse.getString("TradeNo");
        boolean isReturnBonus = Boolean.parseBoolean(refundDetail.get("isReturnBonus"));

        Optional<Orders> ordersOptional = orderRepository.findByOrderNum(merchantTradeNo);

        if (ordersOptional.isEmpty()) {
            throw new RuntimeException("訂單不存在");
        }

        Orders orders = ordersOptional.get();
        Date now = new Date();

        Payment payment = new Payment();
        payment.setOrdersId(orders.getId());
        payment.setPayway("信用卡");
        payment.setPayStatus("1".equals(rtnCode) ? "已退款" : "退款失敗");
        payment.setPayTime(now);
        payment.setMethod("退款");
        payment.setModifyTime(now);
        payment.setTradeNum(tradeNo);
        paymentRepository.save(payment);

        // 退回紅利點數
        if (isReturnBonus) {
            List<Bonus> bonusList = bonusRepository.getBonusByOrderNumAndMethod(merchantTradeNo, "付款");

            if (bonusList.isEmpty()) {
                throw new RuntimeException("紅利紀錄不存在");
            }

            for (Bonus bonus : bonusList) {
                Bonus returnBonus = new Bonus();
                returnBonus.setPaymentId(payment.getId());
                returnBonus.setBonus(bonus.getBonus() * -1);
                returnBonus.setModifyTime(now);
                bonusRepository.save(returnBonus);
            }
        }

        // 刪除電影票、清空座位狀態
        List<Tickets> ticketsList = ticketRepository.findByOrdersId(orders.getId());

        if (ticketsList.isEmpty()) {
            throw new RuntimeException("電影票不存在");
        }

        for (Tickets ticket : ticketsList) {
            try {
                seatStatusRepository.deleteById(ticket.getSeatStatusId());
            } catch (Exception e) {
                throw new RuntimeException("座位狀態刪除失敗: " + e.getMessage());
            }
            ticketRepository.deleteById(ticket.getId());
        }
    }
}
