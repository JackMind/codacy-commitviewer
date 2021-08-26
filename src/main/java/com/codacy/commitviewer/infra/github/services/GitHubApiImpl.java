package com.codacy.commitviewer.infra.github.services;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.service.GitHubApiClient;
import com.codacy.commitviewer.infra.github.dtos.GitHubCommitDto;
import com.codacy.commitviewer.infra.github.mapper.GitHubApiMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class GitHubApiImpl implements GitHubApiClient {

    private final GitHubApi gitHubApi;
    private final GitHubApiMapper gitHubApiMapper;

    @Override
    public List<Commit> getCommits(String owner, String repo, Integer perPage, Integer page) {
        log.info("Execute remote get commit http request owner: {} repo:{} limit:{} offset:{}", owner, repo, perPage, page);

        List<GitHubCommitDto> gitHubCommitDtos = gitHubApi.getCommits(owner, repo, perPage, page);
        log.debug("gitHubCommitDtos: {}", gitHubCommitDtos);

        return gitHubApiMapper.to( gitHubCommitDtos );
    }
}
