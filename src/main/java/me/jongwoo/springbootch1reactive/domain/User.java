package me.jongwoo.springbootch1reactive.domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class User {

    @Id
    private String id;

    private String name;

    private String password;

    private List<String> roles;

    public User(){};

    public User(String id, String name, String password, List<String> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public User(String name, String password, List<String> roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }
}
