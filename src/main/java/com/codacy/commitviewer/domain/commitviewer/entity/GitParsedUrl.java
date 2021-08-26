package com.codacy.commitviewer.domain.commitviewer.entity;

import com.codacy.commitviewer.domain.commitviewer.exception.BadGitHubUrlException;
import com.codacy.commitviewer.domain.git.exceptions.MalformatedUrlException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Slf4j
@Builder
public class GitParsedUrl {
    private final String url;
    private final String owner;
    private final String repo;

    public static GitParsedUrl parse(final String url){
        log.info("Parsing url: {}", url);
        //Checks if the url is well formated
        URL urlParsed;
        try{
            urlParsed = new URL(url);
        } catch (MalformedURLException e) {
            throw new MalformatedUrlException(url, e);
        }

        //Extracts owner and repo from url, most usefull for git hub api
        StringTokenizer stringTokenizer = new StringTokenizer(urlParsed.getPath(), "/");
        try{
            String owner = stringTokenizer.nextToken();
            //Extracts .git from repo
            String repo = stringTokenizer.nextToken().replaceAll(".git", "");
            log.debug("owner: {}, repo:{}", owner, repo);

            return new GitParsedUrl(url, owner, repo);
        }catch (NoSuchElementException e){
            throw new BadGitHubUrlException(url);
        }
    }

}
