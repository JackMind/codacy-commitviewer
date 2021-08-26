package com.codacy.commitviewer.infra.commons.exception;

import com.codacy.commitviewer.domain.commons.exception.CommitViewerException;
import org.zalando.problem.Status;

public class ExternalException extends CommitViewerException {

    private static final String TITLE = "External Exception";

    public ExternalException(Status status, String message) {
        super(TITLE, status, message);
    }

    public ExternalException(String title, Status status, String message) {
        super(title, status, message);
    }
}
