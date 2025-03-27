package com.highload.architect.soc.network.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "account_info", schema = "social_network")
public class AccountInfo {
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "password", nullable = false)
    private String password;

    public AccountInfo() {
    }

    public AccountInfo(UUID id, String password) {
        this.id = id;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
