package org.web.service;

import org.springframework.stereotype.Service;
import org.web.dao.UserDao;
import org.web.dto.Result;
import org.web.entity.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public Result createUser(User users){
        System.out.println(users); //印出User的資料 => 查看傳輸內容
        userDao.save(users);
        return new Result(200, "success");
    }

    public Result updateUser(User users){
        userDao.save(users);
        return new Result(200, "success");
    }

    public Result deleteUser(Integer userId){
        userDao.deleteById(userId);
        return new Result(200, "success");
    }

    public Result getUser(Integer userId){
        // 避免找不到user、缺少值、為null的情況發生
        Optional<User> uesrsOptional = userDao.findById(userId);
        if(uesrsOptional.isPresent()){
            return new Result(200, uesrsOptional.get());
        }else {
            return new Result(9999, "no data");
        }
    }

    public Result findAll(){
        List<User> users =  userDao.findAll();
        return new Result(200, users);
    }

    public Result test(User users){
        List<User> list = userDao.findByEmailJPQL(users.getEmail());
        return new Result(200,list);
    }

}
