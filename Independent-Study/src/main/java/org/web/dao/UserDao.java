package org.web.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByAccountAndPasswd(String account, String passwd);
    Optional<User> findByPhone(String phone);
    boolean existsByAccount(String account); // 添加这行
    boolean existsByEmail(String email); // 添加这行
    @Transactional
    List<User> findByEmail(String email);
    List<User> findByEmailAndPasswd(String email, String passwd);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    List<User> findByEmailJPQL(String email);
}
