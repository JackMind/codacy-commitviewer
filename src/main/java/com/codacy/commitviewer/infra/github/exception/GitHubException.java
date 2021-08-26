package com.codacy.commitviewer.infra.github.exception;

import com.codacy.commitviewer.infra.commons.exception.ExternalException;
import org.zalando.problem.Status;

public class GitHubException extends ExternalException {
    private static final String TITLE = "Git Hub Api Exception";

    public GitHubException(Status status, String message) {
        super(TITLE, status, message);
    }
}
