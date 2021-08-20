package com.codacy.commitviewer.data;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Commit {

    private String sha;
    private String message;
    private OffsetDateTime date;
    private String author;
}
