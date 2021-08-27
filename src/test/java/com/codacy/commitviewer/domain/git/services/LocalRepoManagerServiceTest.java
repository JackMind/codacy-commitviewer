package com.codacy.commitviewer.domain.git.services;

import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class LocalRepoManagerServiceTest {

    @Mock
    private GitCommands gitCommands;

    @InjectMocks
    private LocalRepoManagerService victim;


    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Assert_TempFolderIsCreated() throws IOException {
        GitParsedUrl gitParsedUrl = GitParsedUrl.builder()
                .url("url").owner("owner").repo("repo").build();
        int limit = 1;

        String tempPath = victim.createLocalRepoDirectory(gitParsedUrl, limit);

        File tempRepo = new File(tempPath);
        Assertions.assertTrue( tempRepo.exists() );
        Assertions.assertTrue( tempRepo.getName().contains(gitParsedUrl.getRepo()) );
        Assertions.assertTrue( tempRepo.getName().contains(gitParsedUrl.getOwner()) );

        Mockito.verify(gitCommands, Mockito.times(1))
                .cloneRemoteToDirWithDepth(gitParsedUrl.getUrl(), tempPath, limit);

        Files.delete(Paths.get(tempPath));

    }


}