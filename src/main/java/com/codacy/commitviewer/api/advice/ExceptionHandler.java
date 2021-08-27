package com.codacy.commitviewer.api.advice;

import com.codacy.commitviewer.domain.commons.exception.CommitViewerException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
public class ExceptionHandler implements ProblemHandling {


    @org.springframework.web.bind.annotation.ExceptionHandler(CommitViewerException.class)
    ResponseEntity<Problem> handleCommitException(final CommitViewerException commitViewerException, final NativeWebRequest nativeWebRequest){
        return create(commitViewerException, new CommitViwerProblem(commitViewerException), nativeWebRequest);
    }


    @Data
    public static class CommitViwerProblem extends AbstractThrowableProblem {
        public CommitViwerProblem(final CommitViewerException commitViewerException){
            super(null, commitViewerException.getTitle(), commitViewerException.getStatus(), commitViewerException.getMessage());
        }
    }
}
