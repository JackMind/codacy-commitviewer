package com.codacy.commitviewer.infra.github.dtos;

import lombok.Data;

/**
 * The object that matches the GitHub API commit provided object.
 * Only the required values are mapped.
 */
@Data
public class GitHubCommitDto {
    private String sha;
    private GitHubCommitContentDto commit;

    @Data
    public static class GitHubCommitContentDto {
        private GitHubAuthorDto author;
        private String message;

        @Data
        public static class GitHubAuthorDto {
            private String name;
            private String date;

        }
    }
}
