package com.taishow.controller.cms;

import com.taishow.annotation.PermissionAnnotation;
import com.taishow.dao.UserDao;
import com.taishow.entity.User;
import com.taishow.util.JwtUtilForCms;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/permission")
public class LoginController {

    private UserDao userDao;

    @Autowired
    private JwtUtilForCms jwtUtil;

    public LoginController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/login")
    @PermissionAnnotation()
    public ResponseEntity<String> loginPost(@RequestBody User user,
                                            HttpServletRequest request) {
        Optional<User> optionalUser = userDao.findByAccountAndPasswd(user.getAccount(), user.getPasswd());
        if(optionalUser.isPresent()){
            User users1 = optionalUser.get();
            String token = jwtUtil.generateToken(
                    users1.getId(),
                    users1.getAccount()
            );
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("查無權限");
        }
    }
}
