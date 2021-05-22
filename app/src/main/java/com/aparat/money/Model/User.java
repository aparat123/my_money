package com.aparat.money.Model;

public class User {
    public String name, email, password;

    public User(){

    }

    public User(String name, String email, String password){
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
