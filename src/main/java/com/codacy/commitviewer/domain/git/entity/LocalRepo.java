package com.codacy.commitviewer.domain.git.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@ToString
public class LocalRepo{
    private final String pathAsString;
    private final OffsetDateTime created;

    public Path getPath(){
        return Paths.get(pathAsString);
    }

}