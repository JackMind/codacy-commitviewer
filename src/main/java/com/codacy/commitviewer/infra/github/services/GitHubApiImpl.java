package com.codacy.commitviewer.infra.github.services;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.service.GitHubApiClient;
import com.codacy.commitviewer.infra.github.dtos.GitHubCommitDto;
import com.codacy.commitviewer.infra.github.mapper.GitHubApiMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Git hub api.
 */
@Component
@AllArgsConstructor
@Slf4j
public class GitHubApiImpl implements GitHubApiClient {

    private final GitHubApi gitHubApi;
    private final GitHubApiMapper gitHubApiMapper;

    @Override
    public List<Commit> getCommits(String owner, String repo, Integer perPage, Integer page) {
        log.info("Execute remote get commit http request owner: {} repo:{} limit:{} offset:{}", owner, repo, perPage, page);

        //Executes the remote http request to the GitHub API.
        List<GitHubCommitDto> gitHubCommitDtos = gitHubApi.getCommits(owner, repo, perPage, page);
        log.trace("gitHubCommitDtos: {}", gitHubCommitDtos);

        //Maps the returned commits to the domain model.
        return gitHubApiMapper.to( gitHubCommitDtos );
    }
}
