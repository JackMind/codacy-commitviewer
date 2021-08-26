package com.codacy.commitviewer.domain.git.exceptions;

import com.codacy.commitviewer.domain.commons.exception.CommitViewerException;
import org.zalando.problem.Status;

public class UnableToCreateLocalRepoException extends CommitViewerException {

    private static final String TITLE = "Unable To Create Local Repo";

    public UnableToCreateLocalRepoException(String message) {
        super(TITLE, Status.INTERNAL_SERVER_ERROR, message);
    }
}
