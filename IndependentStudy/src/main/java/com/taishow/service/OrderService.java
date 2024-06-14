package com.taishow.service;

import com.taishow.dao.*;
import com.taishow.dto.OrderDto;
import com.taishow.entity.Orders;
import com.taishow.entity.Payment;
import com.taishow.entity.Tickets;
import com.taishow.myutil.QRCodeGenerator;
import com.taishow.myutil.Snowflake;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final BonusRepository bonusRepository;
    private final TicketRepository ticketRepository;
    private final Snowflake snowflake;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        PaymentRepository paymentRepository, BonusRepository bonusRepository,
                        TicketRepository ticketRepository, Snowflake snowflake) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.bonusRepository = bonusRepository;
        this.ticketRepository = ticketRepository;
        this.snowflake = snowflake;
    }

    public String ecpayCheckout() {
        AllInOne allInOne = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();

        //亂數產生UUID
        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = simpleDateFormat.format(now);

        obj.setMerchantID("3002607");
        obj.setMerchantTradeNo(uuId);
        obj.setMerchantTradeDate(formattedDate);
        obj.setTotalAmount("250");
        obj.setTradeDesc("電影訂票");
        obj.setItemName("電影票");
        obj.setReturnURL("https://9c6c-111-253-164-251.ngrok-free.app"); //付款完成通知回傳網址
        obj.setStoreID("TestId1234"); //特店旗下店舖代號 (theaterId)
        obj.setClientBackURL("https://www.youtube.com/"); //Client端返回特店的按鈕連結
//        obj.setOrderResultURL(""); //Client端回傳付款結果網址，測試完成再改用這個
        obj.setNeedExtraPaidInfo("N");

        String form = allInOne.aioCheckOut(obj, null);

        return form;
    }

    @Transactional
    public void createOrder(OrderDto orderDto, Integer movieId){
        Date now = new Date();

        Orders orders = new Orders();
        orders.setUserId(1); //等JWT token給我
        orders.setOrderNum(String.valueOf(snowflake.nextId()));
        orders.setOrderDate(now);
        orders.setTotalAmount(orderDto.getTotalAmount());
        try {
            String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(orders.getOrderNum(), 350, 350);
            orders.setQrcode(qrCodeBase64);
        } catch (Exception e){
            System.out.println(e);
        }
        orderRepository.save(orders);

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

        for (int i = 0; i < orderDto.getSeatStatusId().size(); i++){
            Tickets tickets = new Tickets();
            tickets.setTicketTypeId(orderDto.getTicketTypeId().get(i));
            tickets.setShowTimeId(orderDto.getShowTimeId());
            tickets.setSeatNum(orderDto.getSeatStatusId().get(i).toString()); //後續改為setSeatStatusId
            tickets.setOrdersId(orders.getId());
            ticketRepository.save(tickets);
        }

    }
}
