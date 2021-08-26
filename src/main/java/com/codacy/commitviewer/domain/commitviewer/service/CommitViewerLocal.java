package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.api.dtos.CommitDto;
import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.commitviewer.mapper.CommitMapper;
import com.codacy.commitviewer.domain.commons.exception.CommitViewerException;
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
        String repoDirectory = localRepoManagerService.createLocalRepoDirectory(gitParsedUrl);
        log.debug("Repository directory: {}", repoDirectory);

        gitCommands.pull(repoDirectory);

        List<Commit> commits = gitCommands.getCommits(Paths.get(repoDirectory), limit, offset);
        log.info("Retrieving {} commits.", commits);

        return commits;
    }
}
