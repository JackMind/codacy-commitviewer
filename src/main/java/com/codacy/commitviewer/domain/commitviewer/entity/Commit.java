package com.codacy.commitviewer.domain.commitviewer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class Commit {

    private String sha;
    private String message;
    private OffsetDateTime date;
    private String author;

    public String getSha() {
        return sha;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }
}
