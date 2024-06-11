package com.taishow.service;

import com.taishow.dao.BookingRepository;
import com.taishow.dto.BookingDto;
import com.taishow.dto.ShowsDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<BookingDto> getWeekShowById(Integer movieId){
        LocalDate endDateLocal = LocalDate.now().plusWeeks(1);
        java.sql.Date endDate = java.sql.Date.valueOf(endDateLocal);

        List<Object[]> results = bookingRepository.getWeekShowById(movieId, endDate);
        Map<Integer, BookingDto> bookingMap = new HashMap<>();

        for (Object[] result : results) {
            Integer theaterId = (Integer) result[0];
            String theaterName = (String) result[1];
            String address = (String) result[2];
            String showTime = result[3].toString();
            Integer screenId = (Integer) result[4];
            String screenName = (String) result[5];
            String screenClass = (String) result[6];

            ShowsDto showsDto = new ShowsDto();
            showsDto.setShowTime(showTime);
            showsDto.setScreenId(screenId);
            showsDto.setScreenName(screenName);
            showsDto.setScreenClass(screenClass);

            BookingDto bookingDto = bookingMap.get(theaterId);
            if (bookingDto == null) {
                bookingDto = new BookingDto();
                bookingDto.setTheaterId(theaterId);
                bookingDto.setTheaterName(theaterName);
                bookingDto.setAddress(address);
                bookingDto.setshowList(new ArrayList<>());
                bookingMap.put(theaterId, bookingDto);
            }
            bookingDto.getshowList().add(showsDto);
        }

        return new ArrayList<>(bookingMap.values());
    }
}
