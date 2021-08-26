package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;

import java.util.List;

public interface GitHubApiClient {

    List<Commit> getCommits(final String owner, final String repo, final Integer perPage, final Integer page);
}
