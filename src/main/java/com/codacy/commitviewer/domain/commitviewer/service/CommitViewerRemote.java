package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class CommitViewerRemote {

    private final GitHubApiClient gitHubApiClient;

    public List<Commit> getCommits(final GitParsedUrl gitParsedUrl, int perPage, int page) {
        log.info("Execute remote get commits to owner: {} repo:{} perPage:{} page:{}", gitParsedUrl.getOwner(), gitParsedUrl.getRepo(), perPage, page);

        List<Commit> commits = gitHubApiClient.getCommits(gitParsedUrl.getOwner(), gitParsedUrl.getRepo(), perPage, page);
        log.trace("commits: {}", commits);

        return commits;
    }
}
