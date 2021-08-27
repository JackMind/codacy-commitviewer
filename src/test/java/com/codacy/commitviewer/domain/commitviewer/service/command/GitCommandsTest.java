package com.codacy.commitviewer.domain.commitviewer.service.command;

import com.codacy.commitviewer.domain.git.services.GitCommands;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class GitCommandsTest {

    @Autowired
    private GitCommands gitCommands;

    @Test
    void test(){
        Path dir = Paths.get("/Users/rubensantos/Projects/commitviewer");

        gitCommands.gitLogFormatted(dir, 10, 0);
    }
}