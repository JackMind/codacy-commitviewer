package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.api.dtos.CommitDto;
import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.commitviewer.mapper.CommitMapper;
import com.codacy.commitviewer.infra.commons.exception.ExternalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommitViewerAggregator {

    private final CommitViewerLocal commitViewerLocal;
    private final CommitViewerRemote commitViewerRemote;
    private final CommitMapper commitMapper;

    public List<CommitDto> getCommits(String url, int perPage, int page, boolean forceLocal){
        log.info("Executing get commits");
        log.trace("url={} perPage={} page={} forceLocal={}", url, perPage, page, forceLocal);

        GitParsedUrl gitParsedUrl = GitParsedUrl.parse(url);

        List<Commit> commits;
        if(forceLocal){
            log.info("Force local execution requested");
            commits = commitViewerLocal.getCommits(gitParsedUrl, perPage, page);
        } else {

            try{
                commits = commitViewerRemote.getCommits(gitParsedUrl, perPage, page);
            } catch (ExternalException externalException){
                log.warn("An error occurred while invoking external git hub api, trying local execution...");
                log.trace("externalException", externalException);
                commits = commitViewerLocal.getCommits(gitParsedUrl, perPage, page);
            }

        }
        log.info("Retrieving {} commits", commits.size());
        log.debug(commits.stream().map(Commit::abreviatedToString).collect(Collectors.joining("\n")));
        log.trace("commits: {}", commits);

        return commitMapper.from(commits);
    }


}
