package com.highload.architect.soc.network.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class DialogMessage {
    private UUID id;
    private String text;
    private UUID from;
    private UUID to;
    private LocalDateTime created;

    public DialogMessage() {}

    public DialogMessage(UUID id, String text, UUID from, UUID to, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.from = from;
        this.to = to;
        this.created = created;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getFrom() {
        return from;
    }

    public void setFrom(UUID from) {
        this.from = from;
    }

    public UUID getTo() {
        return to;
    }

    public void setTo(UUID to) {
        this.to = to;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}


