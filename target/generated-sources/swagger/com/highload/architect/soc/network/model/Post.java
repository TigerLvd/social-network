package com.highload.architect.soc.network.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Пост пользователя
 */

@Schema(name = "Post", description = "Пост пользователя")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T10:15:19.882431+05:00[Asia/Yekaterinburg]", comments = "Generator version: 7.9.0")
public class Post {

  private String id;

  private String text;

  private String authorUserId;

  public Post id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор поста
   * @return id
   */
  
  @Schema(name = "id", example = "1d535fd6-7521-4cb1-aa6d-031be7123c4d", description = "Идентификатор поста", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Post text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Текст поста
   * @return text
   */
  
  @Schema(name = "text", example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lectus mauris ultrices eros in cursus turpis massa.", description = "Текст поста", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Post authorUserId(String authorUserId) {
    this.authorUserId = authorUserId;
    return this;
  }

  /**
   * Идентификатор пользователя
   * @return authorUserId
   */
  
  @Schema(name = "author_user_id", description = "Идентификатор пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("author_user_id")
  public String getAuthorUserId() {
    return authorUserId;
  }

  public void setAuthorUserId(String authorUserId) {
    this.authorUserId = authorUserId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Post post = (Post) o;
    return Objects.equals(this.id, post.id) &&
        Objects.equals(this.text, post.text) &&
        Objects.equals(this.authorUserId, post.authorUserId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, authorUserId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Post {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    authorUserId: ").append(toIndentedString(authorUserId)).append("\n");
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

