package com.codacy.commitviewer.infra.github.services;

import com.codacy.commitviewer.infra.commons.exception.ExternalException;
import com.codacy.commitviewer.infra.github.dtos.GitHubCommitDto;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.zalando.problem.Status;

import java.util.List;

@FeignClient(name = "githubapi", url = "${app.feign.githubapi.url}",
        configuration = GitHubApi.GitHuhApiConfig.class)
public interface GitHubApi {

    @GetMapping(path = "/repos/{owner}/{repo}/commits",
            produces = MediaType.APPLICATION_JSON_VALUE)
    List<GitHubCommitDto> getCommits(@PathVariable("owner") final String owner,
                                     @PathVariable("repo") final String repo,
                                     @RequestParam("per_page") final Integer perPage,
                                     @RequestParam("page") final Integer page);

    class GitHuhApiConfig {
        @Bean
        ErrorDecoder errorDecoder(){
            return (s, response) -> new ExternalException(Status.valueOf(response.status()), response.reason());
        }
    }
}
