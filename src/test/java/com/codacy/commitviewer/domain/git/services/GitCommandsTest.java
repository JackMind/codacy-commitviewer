package com.codacy.commitviewer.domain.git.services;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commons.services.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class GitCommandsTest {

    @InjectMocks
    private GitCommands victim;

    @Mock
    private Command command;


    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Assert_GitLogOutputIsWellParsed(){
        Path path = Paths.get("dummy");
        int limit = 1;
        int offset = 0;

        String sha = "6a8f27162559371909ca1c2ca3d00204b82ca9b6";
        String message = "Git message";
        String date = "2021-08-22T14:19:01+01:00";
        String author = "This author";

        String unparsedCommit = sha+"==FIELD=="+message+"==FIELD=="+date+"==FIELD=="+author;

        Mockito.when(command.execute(path, "git", "log",
                "--pretty=" + GitCommands.format, "--date=iso-strict", "-"+limit, "--skip="+offset, "--reverse"))
                .thenReturn(List.of(unparsedCommit));

        List<Commit> commits = Assertions.assertDoesNotThrow( () -> victim.getCommits(path, limit, offset) );

        Assertions.assertEquals(1, commits.size());
        Assertions.assertEquals(sha, commits.get(0).getSha());
        Assertions.assertEquals(message, commits.get(0).getMessage());
        Assertions.assertEquals(OffsetDateTime.parse(date), commits.get(0).getDate());
        Assertions.assertEquals(author, commits.get(0).getAuthor());
    }


    @Test
    void Assert_GitCloneExecution(){
        String repo = "repo";
        String url = "url";

        Assertions.assertDoesNotThrow( () -> victim.cloneRepo(url, repo) );

        Mockito.verify(command, times(1))
                .execute("git", "clone", "url", "repo");
    }

    @Test
    void Assert_GitPullExecution(){
        String repo = "repo";

        Assertions.assertDoesNotThrow( () -> victim.pull(repo) );

        Mockito.verify(command, times(1))
                .execute(Paths.get(repo), "git", "pull");
    }
}