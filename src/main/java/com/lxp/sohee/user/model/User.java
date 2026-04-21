package com.lxp.sohee.user.model;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String name;
    private UserType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String name, UserType type) {
        this.name = name;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserType getType() {
        return type;
    }
}
