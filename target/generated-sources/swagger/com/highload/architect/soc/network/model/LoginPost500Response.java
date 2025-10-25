package com.highload.architect.soc.network.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LoginPost500Response
 */

@JsonTypeName("_login_post_500_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T10:15:19.882431+05:00[Asia/Yekaterinburg]", comments = "Generator version: 7.9.0")
public class LoginPost500Response {

  private String message;

  private String requestId;

  private Integer code;

  public LoginPost500Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoginPost500Response(String message) {
    this.message = message;
  }

  public LoginPost500Response message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Описание ошибки
   * @return message
   */
  @NotNull 
  @Schema(name = "message", description = "Описание ошибки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LoginPost500Response requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Идентификатор запроса. Предназначен для более быстрого поиска проблем.
   * @return requestId
   */
  
  @Schema(name = "request_id", description = "Идентификатор запроса. Предназначен для более быстрого поиска проблем.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("request_id")
  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public LoginPost500Response code(Integer code) {
    this.code = code;
    return this;
  }

  /**
   * Код ошибки. Предназначен для классификации проблем и более быстрого решения проблем.
   * @return code
   */
  
  @Schema(name = "code", description = "Код ошибки. Предназначен для классификации проблем и более быстрого решения проблем.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("code")
  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginPost500Response loginPost500Response = (LoginPost500Response) o;
    return Objects.equals(this.message, loginPost500Response.message) &&
        Objects.equals(this.requestId, loginPost500Response.requestId) &&
        Objects.equals(this.code, loginPost500Response.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, requestId, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginPost500Response {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

