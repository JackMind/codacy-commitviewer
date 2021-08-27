package com.codacy.commitviewer.domain.commitviewer.service;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.git.services.GitCommands;
import com.codacy.commitviewer.domain.git.services.LocalRepoManagerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class CommitViewerLocal {

    private final LocalRepoManagerService localRepoManagerService;
    private final GitCommands gitCommands;

    /**
     * Returns a list of commits from a remote repo, but it is resolved locally,
     * first we clone the remote repo to a local temp directory (the temp directory path is saved
     * on a cache, so it can be easily managed for future pagination requests and deletion), the
     * clone is executed with a depth flag so the whole repo wont be downloaded and thus being less
     * heavy for the server and for the request time to the client.
     * The depth value is the number of commits requested by the client specified on the variable perPage.
     * If the requested repo already exits or the page requested is greater than one, then a git pull is
     * executed to retrieve more commits to the local repo.
     * Finally, the git log command is executed on the local repo .
     *
     * @param gitParsedUrl the git parsed url
     * @param perPage      the per page
     * @param page         the page
     * @return the commits
     */
    public List<Commit> getCommits(final GitParsedUrl gitParsedUrl, int perPage, int page) {
        int limit = perPage;
        //Converts the page value into a offset logic (DB like), if the page is below 1,
        //that means its the first page, so the offset (skip) does not exists and must be 0,
        //otherwise the number of offset/skip is the product of perPage/limit with the page minus the first page
        int offset = page <= 1 ? 0 : perPage * (page-1);
        log.info("Execute Local get commits limit: {} offset:{}", limit, offset);

        //Creates a local repo by cloning it to a local directory with a depth flag wich value is
        //specified by the limit variable
        String repoDirectory = localRepoManagerService.createLocalRepoDirectory(gitParsedUrl, limit);
        log.debug("Repository directory: {}", repoDirectory);

        //In case of more commit history is needed a git pull is executed with a depth flag being
        //the sum of the number of commits requested and the offset (commits to skip)
        if(offset > 0){
            gitCommands.pullWithDepth(repoDirectory, limit + offset);
        }

        //Executes the git log command on local repo
        List<Commit> commits = gitCommands.logFormatted(Paths.get(repoDirectory), limit, offset);
        log.trace("Retrieving {} commits.", commits);

        return commits;
    }
}
