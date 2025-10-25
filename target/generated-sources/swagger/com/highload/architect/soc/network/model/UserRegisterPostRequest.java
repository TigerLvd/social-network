package com.highload.architect.soc.network.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserRegisterPostRequest
 */

@JsonTypeName("_user_register_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T10:15:19.882431+05:00[Asia/Yekaterinburg]", comments = "Generator version: 7.9.0")
public class UserRegisterPostRequest {

  private String firstName;

  private String secondName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthdate;

  private String biography;

  private String city;

  private String password;

  public UserRegisterPostRequest firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  
  @Schema(name = "first_name", example = "Имя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("first_name")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public UserRegisterPostRequest secondName(String secondName) {
    this.secondName = secondName;
    return this;
  }

  /**
   * Get secondName
   * @return secondName
   */
  
  @Schema(name = "second_name", example = "Фамилия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("second_name")
  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public UserRegisterPostRequest birthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
    return this;
  }

  /**
   * Дата рождения
   * @return birthdate
   */
  @Valid 
  @Schema(name = "birthdate", example = "Wed Feb 01 05:00:00 YEKT 2017", description = "Дата рождения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("birthdate")
  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public UserRegisterPostRequest biography(String biography) {
    this.biography = biography;
    return this;
  }

  /**
   * Get biography
   * @return biography
   */
  
  @Schema(name = "biography", example = "Хобби, интересы и т.п.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("biography")
  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public UserRegisterPostRequest city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
   */
  
  @Schema(name = "city", example = "Москва", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public UserRegisterPostRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   */
  
  @Schema(name = "password", example = "Секретная строка", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRegisterPostRequest userRegisterPostRequest = (UserRegisterPostRequest) o;
    return Objects.equals(this.firstName, userRegisterPostRequest.firstName) &&
        Objects.equals(this.secondName, userRegisterPostRequest.secondName) &&
        Objects.equals(this.birthdate, userRegisterPostRequest.birthdate) &&
        Objects.equals(this.biography, userRegisterPostRequest.biography) &&
        Objects.equals(this.city, userRegisterPostRequest.city) &&
        Objects.equals(this.password, userRegisterPostRequest.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, secondName, birthdate, biography, city, password);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRegisterPostRequest {\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    secondName: ").append(toIndentedString(secondName)).append("\n");
    sb.append("    birthdate: ").append(toIndentedString(birthdate)).append("\n");
    sb.append("    biography: ").append(toIndentedString(biography)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

