package com.codacy.commitviewer.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class Commit {

    private final String sha;
    private final String message;
    private final OffsetDateTime date;
    private final String author;
}
