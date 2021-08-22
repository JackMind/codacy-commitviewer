package com.codacy.commitviewer.domain.command;

import com.codacy.commitviewer.domain.git.GitLogCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class GitLogCommandTest {

    @Autowired
    private GitLogCommand gitLogCommand;

    @Test
    void test(){
        Path dir = Paths.get("/Users/rubensantos/Projects/commitviewer");

        gitLogCommand.execute(dir);
    }
}