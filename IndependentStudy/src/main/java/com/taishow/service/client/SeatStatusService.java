package com.taishow.service.client;

import com.taishow.dao.SeatStatusRepository;
import com.taishow.dto.SeatStatusDto;
import com.taishow.entity.SeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatStatusService {

    @Autowired
    private SeatStatusRepository seatStatusRepository;

    private final Object lock = new Object(); // 鎖對象

    public List<SeatStatusDto> getSeatStatus(String theaterName, String screenName,  LocalDateTime showTime){

        List<Object[]> results = seatStatusRepository.findSeatStatus(theaterName, screenName,  showTime);
        return results.stream()
                .map(result -> {
//                      // 調試信息：打印每個字段的類型
//                      System.out.println("Theater Name: " + result[0].getClass().getName());
//                      System.out.println("Screen Name: " + result[1].getClass().getName());
//                      System.out.println("Row Num: " + result[2].getClass().getName());
//                      System.out.println("Seat Number: " + result[3].getClass().getName());
//                      System.out.println("Status: " + result[4].getClass().getName());
//                      System.out.println("Showtime: " + result[5].getClass().getName());
//                      System.out.println("Seat Status: " + result[6].getClass().getName());
//                      System.out.println("Seat Status: " + result[7].getClass().getName());
//                      System.out.println("Movie Title: " + result[8].getClass().getName());
//                      System.out.println("Movie Poster: " + result[9].getClass().getName());

                    return new SeatStatusDto(
                            (String) result[0], // theater_name
                            (String) result[1], // screen_name
                            (String) result[4], // seat_status
                            convertToLocalDateTime(result[5]), // showtime
                            (Integer) result[2],  // row_num
                            (Integer) result[3], // seat_number      陣列的值會長這樣是因為SQL查詢順序
                            (Integer) result[6], // seat_id
                            (Integer) result[7],  // showtime_id
                            (String)  result[8],  // movie.title
                            (String)  result[9]   // movie.poster

                    );
                })
                .collect(Collectors.toList());
    }

    private LocalDateTime convertToLocalDateTime(Object dateObject) {
        if (dateObject instanceof LocalDateTime) {
            return (LocalDateTime) dateObject;
        } else if (dateObject instanceof String) {
            return LocalDateTime.parse((String) dateObject, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if (dateObject instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) dateObject).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Unsupported date type: " + dateObject.getClass().getName());
        }
    }
    @Transactional // 确保整个方法在一个事务中执行，保证数据一致性
    public List<Integer> reserveSeats(List<SeatStatus> seatStatuses) {
        List<Integer> reservedSeatIds = new ArrayList<>(); // 用于存储已预订座位的ID

        for (SeatStatus seatStatus : seatStatuses) { // 遍历所有需要预订的座位状态
            if (seatStatus.getSeatId() != null) { // 检查 seatId 是否为空
                synchronized (lock) { // 同步块，确保同一时刻只有一个线程可以进入此块，避免并发问题
                    SeatStatus existingSeatStatus = seatStatusRepository.findBySeatId(seatStatus.getSeatId());
                    // 查找数据库中是否已有该座位的状态信息

                    if (existingSeatStatus != null) { // 如果座位状态信息已存在
                        if ("available".equals(existingSeatStatus.getStatus())) {
                            // 如果座位状态是 "available"，则可以预订
                            existingSeatStatus.setStatus("taken"); // 将状态设置为 "taken"
                            existingSeatStatus.setCreateAt(LocalDateTime.now()); // 更新创建时间为当前时间
                            seatStatusRepository.saveAndFlush(existingSeatStatus);
                            // 使用 saveAndFlush 确保更改立即写入数据库，避免延迟
                            reservedSeatIds.add(existingSeatStatus.getId()); // 将预订的座位ID添加到列表
                        } else {
                            throw new IllegalStateException("座位已被選取");
                            // 如果座位状态不是 "available"，则抛出异常
                        }
                    } else {
                        // 如果座位状态信息不存在，说明是新座位，直接保存新的状态
                        seatStatus.setCreateAt(LocalDateTime.now()); // 设置创建时间为当前时间
                        SeatStatus savedSeatStatus = seatStatusRepository.saveAndFlush(seatStatus);
                        // 保存并立即刷新到数据库，确保数据一致性
                        reservedSeatIds.add(savedSeatStatus.getId()); // 将预订的座位ID添加到列表
                    }
                } // 同步块结束
            } else {
                throw new IllegalArgumentException("seatId cannot be null");
                // 如果 seatId 为空，抛出非法参数异常
            }
        }
        return reservedSeatIds; // 返回预订的座位ID列表
    }
    // 定時任務：每50秒檢查一次支付狀態並更新座位狀態
    @Scheduled(fixedRate = 50000)
    public void checkPaymentStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<SeatStatus> seatStatusList = seatStatusRepository.findAll();

        for (SeatStatus seatStatus : seatStatusList) {
            LocalDateTime createAt = seatStatus.getCreateAt();
            // 如果座位已經超過10分鐘未支付，則處理座位狀態
            if (createAt != null && now.isAfter(createAt.plusMinutes(10))) {
                processSeatStatus(seatStatus);
            }
        }
    }

    // 非同步方法：處理單個座位的支付狀態
    @Async
    public void processSeatStatus(SeatStatus seatStatus) {
        Integer seatStatusId = seatStatus.getId();
        List<String> payStatusList = seatStatusRepository.getPayStatusById1(seatStatusId);

        if (payStatusList.isEmpty()) {
            System.out.println("No payment data for seat status ID: " + seatStatusId);
            updateSeatStatusToAvailable(seatStatus);
        } else {
            String payStatus = payStatusList.get(0);
            switch (payStatus) {
                case "已退款":
                    System.out.println("Payment refunded for seat status ID: " + seatStatusId);
                    updateSeatStatusToAvailable(seatStatus);
                    break;
                case "付款失敗":
                    System.out.println("Payment failed for seat status ID: " + seatStatusId);
                    updateSeatStatusToAvailable(seatStatus);
                    break;
                case "已取消":
                    System.out.println("Payment cancelled for seat status ID: " + seatStatusId);
                    // 不再需要更新座位狀態為"cancelled"
                    break;
                default:
                    System.out.println("Payment status: " + payStatus + " for seat status ID: " + seatStatusId);
                    // 處理其他支付狀態的情況
                    break;
            }
        }
    }

    // 更新座位狀態為"available"
    private void updateSeatStatusToAvailable(SeatStatus seatStatus) {
        seatStatus.setStatus("available");
        seatStatusRepository.save(seatStatus);
    }


}

