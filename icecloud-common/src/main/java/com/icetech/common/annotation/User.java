package com.icetech.common.annotation;

import com.icetech.common.validator.Validator;

public class User {
    @NotNull()
    private String name;
    @NotNull(condition = "phone == *SCAN AND name==1")
    private String pw;
    private String phone;

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setPhone("1SCAN");
        user.setName("2");
//        user.setPw("2");
        Validator.validate(user);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
