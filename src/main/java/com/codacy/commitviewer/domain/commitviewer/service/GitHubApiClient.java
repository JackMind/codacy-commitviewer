package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;

import java.util.List;

/**
 * The interface that specifies the contract with the GitHub API.
 */
public interface GitHubApiClient {

    /**
     * Specifies the contract with the GitHub API.
     * Handles the mapping of the dto provided by the GitHub API and the domain model.
     *
     * @param owner   the owner
     * @param repo    the repo
     * @param perPage the per page
     * @param page    the page
     * @return the commits
     */
    List<Commit> getCommits(final String owner, final String repo, final Integer perPage, final Integer page);
}
