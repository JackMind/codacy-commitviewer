package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.infra.commons.exception.ExternalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the main service of the app, its purpose is to serve as a coordinator for the
 * different types of executions, remote or local.
 */
@Service
@AllArgsConstructor
@Slf4j
public class CommitViewerAggregator {

    private final CommitViewerLocal commitViewerLocal;
    private final CommitViewerRemote commitViewerRemote;

    /**
     * This method returns a list of structured commits {@link Commit} executing
     * a remote call to the github api via a provided github url.
     * In case of error while executing the request to the git hub api it fallback to a local execution.
     * This method also checks if the url is a well formatted github url.
     * In case of the forceLocal variable is true, only the local execution is executed (mainly for test purposes).
     *
     *
     * @param url        the url
     * @param perPage    the per page
     * @param page       the page
     * @param forceLocal the force local
     * @return the list
     */
    public List<Commit> getCommits(String url, int perPage, int page, boolean forceLocal){
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

        return commits;
    }


}
