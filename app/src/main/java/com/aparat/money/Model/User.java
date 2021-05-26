package com.aparat.money.Model;

public class User {
    public String name, email, password;
    public int balance;

    public User(){

    }

    public User(String name, String email, String password, int balance){
        this.email = email;
        this.name = name;
        this.balance = balance;
        this.password = password;
    }
}
