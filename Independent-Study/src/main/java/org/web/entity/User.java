package org.web.entity;



import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String user_name;
    @Column
    private String image;
    @Column
    private String account;
    @Column
    private String passwd;
    @Column
    private String phone;
    @Column
    private String email ;
    @Column
    private String address;
    @Column
    private LocalDate birthday; // 使用 LocalDate 類型
    @Column
    private String gender;

    public User() {}

    public User(Integer id, String image, String account, String passwd, String phone, String email, String address, LocalDate birthday, String user_name, String gender) {
        this.id = id;
        this.image = image;
        this.account = account;
        this.passwd = passwd;
        this.phone = phone;
        this.email = email;
        this.address=address;
        this.birthday=birthday;
        this.user_name=user_name;
        this.gender=gender;
    }

    public Integer getUserId() {
        return id;
    }

    public void setUserId(Integer id) {this.id =id; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String user_name) {
        this.user_name = user_name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    @Override
    public String toString() {
        return "Users{" +
                "userId=" + id +
                ", image=" + image +
                ", account='" + account + '\'' +
                ", passwd='" + passwd + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
