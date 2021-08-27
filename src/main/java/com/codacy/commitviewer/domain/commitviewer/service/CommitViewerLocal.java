package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.git.services.GitCommands;
import com.codacy.commitviewer.domain.git.services.LocalRepoManagerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class CommitViewerLocal {

    private final LocalRepoManagerService localRepoManagerService;
    private final GitCommands gitCommands;

    public List<Commit> getCommits(final GitParsedUrl gitParsedUrl, int limit, int offset) {
        log.info("Execute Local get commits");
        String repoDirectory = localRepoManagerService.createLocalRepoDirectory(gitParsedUrl, limit);
        log.debug("Repository directory: {}", repoDirectory);

        if(offset != 0){
            gitCommands.pullWithDepth(repoDirectory, limit + offset);
        }

        List<Commit> commits = gitCommands.gitLogFormatted(Paths.get(repoDirectory), limit, offset);
        log.trace("Retrieving {} commits.", commits);

        return commits;
    }
}
