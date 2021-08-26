package com.codacy.commitviewer.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CommitDto {
    private String sha;
    private String message;
    private String date;
    private String author;

}
