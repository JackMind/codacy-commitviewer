package com.codacy.commitviewer.domain.git.exceptions;

import com.codacy.commitviewer.domain.commons.exception.CommitViewerException;
import org.zalando.problem.Status;

import java.net.MalformedURLException;

public class MalformatedUrlException  extends CommitViewerException {

    private static final String TITLE = "Malformated url exception";

    public MalformatedUrlException(final String url, final MalformedURLException exception) {
        super(TITLE, Status.INTERNAL_SERVER_ERROR,
                String.format("Unable to parse url %s, %s", url, exception.getMessage()));
    }
}
