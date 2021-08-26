package com.codacy.commitviewer.domain.git.exceptions;

import com.codacy.commitviewer.domain.commons.exception.CommitViewerException;
import org.zalando.problem.Status;

public class UnableToDeleteLocalRepo extends CommitViewerException {

    private static final String TITLE = "Unable To Delete Local Repo";

    public UnableToDeleteLocalRepo(String message) {
        super(TITLE, Status.INTERNAL_SERVER_ERROR, message);
    }
}