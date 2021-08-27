package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.git.entity.LocalRepo;
import com.codacy.commitviewer.domain.git.services.GitCommands;
import com.codacy.commitviewer.domain.git.services.LocalRepoManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

class CommitViewerLocalTest {

    @Mock
    private LocalRepoManagerService localRepoManagerService;
    @Mock
    private GitCommands gitCommands;

    @InjectMocks
    private CommitViewerLocal victim;

    @Captor
    ArgumentCaptor<GitParsedUrl> gitUrlArgumentCaptor;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void AssertThatReturnCommitsDto_CreateLocalRepoDir_ThenGetCommits(){
        int perPage = 1;
        int page = 1;

        int expectedOffset = 0;
        int expectedLimit = 1;

        String expectedRepoDir = "expectedRepoDir";
        LocalRepo localRepo = new LocalRepo(expectedRepoDir, OffsetDateTime.now());
        Commit expectedCommit = new Commit("sha", "message", OffsetDateTime.MIN, "author");
        GitParsedUrl gitParsedUrl = GitParsedUrl.builder().url("url").owner("owner").repo("repo").build();

        Mockito.when(localRepoManagerService.createLocalRepoDirectory(gitParsedUrl))
                .thenReturn(localRepo);
        Mockito.when(gitCommands.logFormatted(Paths.get(expectedRepoDir), expectedLimit, expectedOffset))
                .thenReturn(List.of(expectedCommit));

        List<Commit> commits = Assertions.assertDoesNotThrow(
                () -> victim.getCommits(gitParsedUrl, perPage, page) );

        Assertions.assertEquals(expectedCommit, commits.get(0));

        Mockito.verify(localRepoManagerService, Mockito.times(1))
                .createLocalRepoDirectory(gitUrlArgumentCaptor.capture());
        GitParsedUrl actualGitUrl = gitUrlArgumentCaptor.getValue();
        Assertions.assertEquals(gitParsedUrl, actualGitUrl);

        Mockito.verify(gitCommands, Mockito.times(1))
                .logFormatted(Paths.get(expectedRepoDir), expectedLimit, expectedOffset);

    }

    @Test
    void AssertThatReturnCommitsDto_CreateLocalRepoDir_ThenGitPull_ThenGetCommits(){
        int perPage = 1;
        int page = 2;

        int expectedOffset = 1;
        int expectedLimit = 1;

        String expectedRepoDir = "expectedRepoDir";
        Commit expectedCommit = new Commit("sha", "message", OffsetDateTime.MIN, "author");
        GitParsedUrl gitParsedUrl = GitParsedUrl.builder()
                .url("url").owner("owner").repo("repo").build();
        LocalRepo localRepo = new LocalRepo(expectedRepoDir, OffsetDateTime.now());

        Mockito.when(localRepoManagerService.createLocalRepoDirectory(gitParsedUrl))
                .thenReturn(localRepo);
        Mockito.when(gitCommands.logFormatted(Paths.get(expectedRepoDir), expectedLimit, expectedOffset))
                .thenReturn(List.of(expectedCommit));

        List<Commit> commits = Assertions.assertDoesNotThrow(
                () -> victim.getCommits(gitParsedUrl, perPage, page) );

        Assertions.assertEquals(expectedCommit, commits.get(0));

        Mockito.verify(localRepoManagerService, Mockito.times(1))
                .createLocalRepoDirectory(gitUrlArgumentCaptor.capture());
        GitParsedUrl actualGitUrl = gitUrlArgumentCaptor.getValue();
        Assertions.assertEquals(gitParsedUrl, actualGitUrl);


        Mockito.verify(gitCommands, Mockito.times(1))
                .fetchWithDepth(eq(expectedRepoDir), eq(expectedOffset + expectedLimit) );
        Mockito.verify(gitCommands, Mockito.times(1))
                .logFormatted(Paths.get(expectedRepoDir), expectedLimit, expectedOffset);

    }
}