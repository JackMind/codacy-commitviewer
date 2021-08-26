package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.commitviewer.mapper.CommitMapper;
import com.codacy.commitviewer.domain.git.services.GitCommands;
import com.codacy.commitviewer.domain.git.services.LocalRepoManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;

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
    void AssertThatReturnCommitsDto_CreateLocalRepoDir_ThenGitClone_ThenGitPull_ThenGetCommits(){
        int limit = 1;
        int offset = 0;
        String url = "url";
        String owner = "owner";
        String repo = "repo";
        String expectedRepoDir = "expectedRepoDir";
        Commit expectedCommit = new Commit("sha", "message", OffsetDateTime.MIN, "author");
        GitParsedUrl gitParsedUrl = GitParsedUrl.builder()
                .url(url).owner(owner).repo(repo).build();

        Mockito.when(localRepoManagerService.createLocalRepoDirectory(gitParsedUrl))
                .thenReturn(expectedRepoDir);
        Mockito.when(gitCommands.getCommits(Paths.get(expectedRepoDir), limit, offset))
                .thenReturn(List.of(expectedCommit));

        List<Commit> commits = Assertions.assertDoesNotThrow(
                () -> victim.getCommits(gitParsedUrl, limit, offset) );

        Assertions.assertEquals(expectedCommit, commits.get(0));

        Mockito.verify(localRepoManagerService, Mockito.times(1))
                .createLocalRepoDirectory(gitUrlArgumentCaptor.capture());
        GitParsedUrl actualGitUrl = gitUrlArgumentCaptor.getValue();
        Assertions.assertEquals(gitParsedUrl, actualGitUrl);

        Mockito.verify(gitCommands, Mockito.times(1))
                .pull(expectedRepoDir);
        Mockito.verify(gitCommands, Mockito.times(1))
                .getCommits(Paths.get(expectedRepoDir), limit, offset);

    }
}