package com.codacy.commitviewer.domain.commitviewer.exception;

import com.codacy.commitviewer.domain.commons.exception.CommitViewerException;
import org.zalando.problem.Status;

public class BadGitHubUrlException extends CommitViewerException {

    private static final String TITLE = "Please provide a well formatted git hub url, with owner and repo on path";

    public BadGitHubUrlException(String url) {
        super(TITLE, Status.INTERNAL_SERVER_ERROR, url);
    }
}