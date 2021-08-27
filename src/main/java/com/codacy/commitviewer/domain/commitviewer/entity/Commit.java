package com.codacy.commitviewer.domain.commitviewer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
public class Commit {

    private final String sha;
    private final String message;
    private final OffsetDateTime date;
    private final String author;

    public String abreviatedToString(){
        return sha.substring(0, 5) + " " + message.substring(0, 10) + " "+ date;
    }
}
