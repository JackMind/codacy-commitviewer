package com.codacy.commitviewer.domain.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zalando.problem.Status;

@AllArgsConstructor
@Getter
public abstract class CommitViewerException extends RuntimeException{
    private final String title;
    private final Status status;
    private final String message;
}
