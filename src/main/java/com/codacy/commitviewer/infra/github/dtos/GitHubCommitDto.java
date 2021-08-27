package com.codacy.commitviewer.infra.github.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * The object that matches the GitHub API commit provided object.
 * Only the required values are mapped.
 */
@Value
@AllArgsConstructor
@Builder
public class GitHubCommitDto {
    String sha;
    GitHubCommitContentDto commit;

    @Value
    public static class GitHubCommitContentDto {
        GitHubAuthorDto author;
        String message;

        @Value
        public static class GitHubAuthorDto {
            String name;
            String date;

        }
    }
}
