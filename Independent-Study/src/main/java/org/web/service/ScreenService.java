//package org.web.service;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//import org.web.dao.ScreenDao;
//import org.web.dto.Result;
//import org.web.entity.Screen;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class ScreenService {
//
//    private ScreenDao screenDao;
//
//    public ScreenService(ScreenDao screenDao) {
//        this.screenDao = screenDao;
//    }
//
//    public Result createScreen(Screen screen){
//        screenDao.save(screen);
//        return new Result(200, "success");
//    }
//
//    public Result updateScreen(Screen screen){
//        screenDao.save(screen);
//        return new Result(200, "success");
//    }
//
//    public Result deleteScreen(Integer screenId){
//        screenDao.deleteById(screenId);
//        return new Result(200, "success");
//    }
//
//    public Result getScreen(Integer screenId){
//        Optional<Screen> optionalScreen = screenDao.findById(screenId);
//        if(optionalScreen.isPresent()){
//            return new Result(200, optionalScreen.get());
//        }else {
//            return new Result(999, "no data");
//        }
//    }
//
//    public Result getScreenByTheaterId(Integer theaterId){
//        List<Screen> list = screenDao.findByTheaterId(theaterId);
//        return new Result(200, list);
//    }
//
//}
