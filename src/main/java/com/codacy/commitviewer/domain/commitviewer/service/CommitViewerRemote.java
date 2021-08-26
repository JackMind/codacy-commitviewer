package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.api.dtos.CommitDto;
import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.commitviewer.mapper.CommitMapper;
import com.codacy.commitviewer.infra.github.dtos.GitHubCommitDto;
import com.codacy.commitviewer.infra.github.services.GitHubApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class CommitViewerRemote {

    private final GitHubApiClient gitHubApiClient;

    public List<Commit> getCommits(final GitParsedUrl gitParsedUrl, int limit, int offset) {
        log.info("Execute remote get commits to owner: {} repo:{} limit:{} offset:{}", gitParsedUrl.getOwner(), gitParsedUrl.getRepo(), limit, offset);

        List<Commit> commits = gitHubApiClient.getCommits(gitParsedUrl.getOwner(), gitParsedUrl.getRepo(), limit, offset);
        log.debug("commits: {}", commits);

        return commits;
    }
}