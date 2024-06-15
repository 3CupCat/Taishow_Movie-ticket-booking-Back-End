package com.taishow.service;

import com.taishow.dao.*;
import com.taishow.dto.OrderDto;
import com.taishow.entity.*;
import com.taishow.myutil.QRCodeGenerator;
import com.taishow.myutil.Snowflake;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.transaction.Transactional;
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
    private final Snowflake snowflake;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        PaymentRepository paymentRepository, BonusRepository bonusRepository,
                        TicketRepository ticketRepository, TicketTypeRepository ticketTypeRepository,
                        Snowflake snowflake) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.bonusRepository = bonusRepository;
        this.ticketRepository = ticketRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.snowflake = snowflake;
    }

    @Transactional
    public Map<String, String> createOrder(OrderDto orderDto, Integer movieId){
        //檢查票數是否為0張、大於6張
        if (orderDto.getSeatStatusId().isEmpty()){
            throw new IllegalArgumentException("請先選擇座位");
        } else if (orderDto.getSeatStatusId().size() > 6) {
            throw new IllegalArgumentException("最多僅能購買六張票");
        }

        //檢查票數跟票種是否相同
        if (orderDto.getSeatStatusId().size() != orderDto.getTicketTypeId().size()){
            throw new IllegalArgumentException("請選擇等同座位數的票");
        }

        //計算總金額
        int totalPrice = 0;

        //計算總扣除紅利
        int reduceBonusPoint = 0;

        //會員持有紅利
        int userBonusPoint = 1000; //之後有JWT時，改為user表get

        for (int i = 0; i < orderDto.getSeatStatusId().size(); i++){
            TicketType ticketType = ticketTypeRepository.findById(orderDto.getTicketTypeId().get(i))
                    .orElseThrow(() -> new RuntimeException("無效的票種"));
            if (ticketType.getTicketType().equals("紅利點數")){
                //紅利點數兌票需要250點
                if (userBonusPoint >= 250){
                    userBonusPoint -= 250;
                    reduceBonusPoint -= 250;
                } else {
                    throw new RuntimeException("紅利點數不足");
                }
            } else {
                totalPrice += ticketType.getUnitPrice();
            }
        }

        //取得現在時間
        Date now = new Date();

        //建立訂單
        Orders orders = new Orders();
        orders.setUserId(1); //等JWT token給我
        orders.setOrderNum(String.valueOf(snowflake.nextId()));
        orders.setOrderDate(now);
        orders.setTotalAmount(totalPrice);
        try {
            String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(orders.getOrderNum(), 350, 350);
            orders.setQrcode(qrCodeBase64);
        } catch (Exception e){
            System.out.println(e);
        }
        orderRepository.save(orders);

        //建立支付紀錄
        Payment payment = new Payment();
        payment.setOrdersId(orders.getId());
        payment.setPayway(""); //等金流回調，要記得更新這欄
        if (orderDto.getTotalAmount() != 0){
            payment.setPayStatus("未付款");
        } else {
            payment.setPayStatus("不需付款");
        }
        payment.setPayTime(now);
        payment.setMethod("付款");
        payment.setModifyTime(now);
        paymentRepository.save(payment);

        //若使用紅利點數兌票，於此扣除紅利
        if (reduceBonusPoint < 0){
            Bonus bonus = new Bonus();
            bonus.setPaymentId(payment.getId());
            bonus.setBonus(reduceBonusPoint);
            bonus.setModifyTime(now);
            bonusRepository.save(bonus);

//            User user = new User();
            //有JWT時，set紅利點數扣除多少
        }

        //建立票根
        for (int i = 0; i < orderDto.getSeatStatusId().size(); i++){
            Tickets tickets = new Tickets();
            tickets.setTicketTypeId(orderDto.getTicketTypeId().get(i));
            tickets.setShowTimeId(orderDto.getShowTimeId());
            tickets.setSeatNum(orderDto.getSeatStatusId().get(i).toString()); //後續改為setSeatStatusId
            tickets.setOrdersId(orders.getId());
            ticketRepository.save(tickets);
        }

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
    public void paymentSuccess(Hashtable<String, String> callbackData){
        String orderNum = callbackData.get("MerchantTradeNo");

        Optional<Orders> ordersOptional = orderRepository.findByOrderNum(orderNum);
        if (ordersOptional.isPresent()){
            Orders orders = ordersOptional.get();
            Integer ordersId = orders.getId();
            Optional<Payment> paymentOptional = paymentRepository.findByOrdersId(ordersId);

            if(paymentOptional.isPresent()){
                Date now = new Date();

                Payment payment = paymentOptional.get();
                payment.setPayway(callbackData.get("PaymentType"));
                payment.setPayStatus("完成");
                payment.setPayTime(now);
                payment.setModifyTime(now);
                paymentRepository.save(payment);

                //訂單成功，產生紅利
                Bonus bonus = new Bonus();
                bonus.setPaymentId(payment.getId());
                bonus.setBonus((int) (orders.getTotalAmount() * 0.1));
                bonus.setModifyTime(now);
                bonusRepository.save(bonus);
            } else {
                System.out.println("存款紀錄不存在");
            }
        } else {
            System.out.println("訂單不存在");
        }
    }

    @Transactional
    public void paymentFail(Hashtable<String, String> callbackData){
        //透過orderNum更改payment資料
        //檢查是否有被扣紅利(需歸還)
        //tickets刪除
        //seat_status刪除
    }

    @Transactional
    public void refundSuccess(Hashtable<String, String> callbackData){
        //透過orderNum更改payment資料
        //扣除購票生成之紅利 (若會員將該筆紅利花掉,則在退票controller少退回等值金額 ==> total_amount * 10%)
        //紅利購票須退回紅利
        //tickets刪除
        //seat_status刪除
    }
}
