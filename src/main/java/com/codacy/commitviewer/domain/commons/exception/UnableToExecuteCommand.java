package com.codacy.commitviewer.domain.commons.exception;

import org.zalando.problem.Status;

public class UnableToExecuteCommand extends CommitViewerException {

    private static final String TITLE = "Unable To execute command";

    public UnableToExecuteCommand(String message) {
        super(TITLE, Status.BAD_REQUEST, message);
    }
}
