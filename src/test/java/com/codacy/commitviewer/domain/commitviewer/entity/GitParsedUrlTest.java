package com.codacy.commitviewer.domain.commitviewer.entity;

import com.codacy.commitviewer.domain.commitviewer.exception.BadGitHubUrlException;
import com.codacy.commitviewer.domain.git.exceptions.MalformatedUrlException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GitParsedUrlTest {

    @Test
    void Assert_WellFormatedUrlParse(){
        String owner = "octocat";
        String repo = "Hello-World";

        String url = "https://github.com/"+owner+"/"+repo+".git" ;

        GitParsedUrl actual = GitParsedUrl.parse(url);

        Assertions.assertEquals(owner, actual.getOwner());
        Assertions.assertEquals(repo, actual.getRepo());
        Assertions.assertEquals(url, actual.getUrl());
    }

    @Test
    void AssertThrow_IfUrlIsMallFormatted(){
        Assertions.assertThrows(MalformatedUrlException.class, () -> GitParsedUrl.parse("/url"));
    }

    @Test
    void AssertThrow_OnMalformatedGitHubUrl(){
        String url = "https://github.com/";
        Assertions.assertThrows(BadGitHubUrlException.class, () -> GitParsedUrl.parse(url));

    }
}