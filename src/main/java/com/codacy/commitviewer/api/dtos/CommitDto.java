package com.codacy.commitviewer.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class CommitDto {
    String sha;
    String message;
    String date;
    String author;
}
