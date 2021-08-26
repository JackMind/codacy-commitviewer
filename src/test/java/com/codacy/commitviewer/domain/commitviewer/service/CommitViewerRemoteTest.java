package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class CommitViewerRemoteTest {

    @Mock
    private GitHubApiClient gitHubApiClient;

    @InjectMocks
    private CommitViewerRemote victim;


    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Assert_GitHubApiClientIsExecuted(){
        int limit = 1;
        int offset = 0;
        String url = "url";
        String owner = "owner";
        String repo = "repo";
        GitParsedUrl gitParsedUrl = GitParsedUrl.builder()
                .url(url).owner(owner).repo(repo).build();

        Assertions.assertDoesNotThrow( () -> victim.getCommits(gitParsedUrl, limit, offset) );
        Mockito.verify(gitHubApiClient, times(1))
                .getCommits(owner, repo, limit, offset);

    }
}