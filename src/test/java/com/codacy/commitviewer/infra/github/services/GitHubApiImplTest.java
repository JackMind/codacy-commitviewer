package com.codacy.commitviewer.infra.github.services;

import com.codacy.commitviewer.infra.github.dtos.GitHubCommitDto;
import com.codacy.commitviewer.infra.github.mapper.GitHubApiMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

class GitHubApiImplTest {

    @Mock
    private GitHubApi gitHubApi;
    @Mock
    private GitHubApiMapper gitHubApiMapper;
    @InjectMocks
    private GitHubApiImpl victim;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Assert_GitHubApiExecution_AndDtoConvertedToDomainObject(){
        String owner = "owner";
        String repo = "repo";
        int perPage = 1;
        int page = 0;

        List<GitHubCommitDto> gitHubCommitDtos= List.of(new GitHubCommitDto());

        Mockito.when(gitHubApi.getCommits(owner, repo, perPage, page))
                .thenReturn(gitHubCommitDtos);

        Assertions.assertDoesNotThrow( () -> victim.getCommits(owner, repo, perPage, page) );

        Mockito.verify(gitHubApiMapper, Mockito.times(1)).to(gitHubCommitDtos);
    }
}