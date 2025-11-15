package com.highload.architect.soc.network.model;

import java.time.LocalDate;

public class UserRegisterPostRequest {
    private String firstName;
    private String secondName;
    private LocalDate birthdate;
    private String biography;
    private String city;
    private String password;

    // Constructors
    public UserRegisterPostRequest() {}

    public UserRegisterPostRequest(String firstName, String secondName, LocalDate birthdate, String biography, String city, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthdate = birthdate;
        this.biography = biography;
        this.city = city;
        this.password = password;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


