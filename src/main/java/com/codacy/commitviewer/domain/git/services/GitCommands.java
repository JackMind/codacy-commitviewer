package com.codacy.commitviewer.domain.git.services;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.domain.commons.services.Command;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringTokenizer;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class GitCommands {

    private final Command command;

    static String FIELD = "==FIELD==";

    /**
     * The format give to the logFormatted command that specifies the different required fields separated
     * by the FIELD variable.
     */
    public static String GIT_LOG_FORMAT = "format:" +
            "%H" +  //sha full hash
            FIELD +
            "%s" +  //commit message content
            FIELD +
            "%ad" + //author date
            FIELD +
            "%an";  //author name


    /**
     * Parses the output of the logFormatted command that is specified by the GIT_LOG_FORMAT.
     *
     * @param output the output
     * @return the commit
     */
    public static Commit parse(String output) {
        StringTokenizer stringTokenizer = new StringTokenizer(output, FIELD);
        String sha = stringTokenizer.nextToken();
        String message = stringTokenizer.nextToken();
        String date = stringTokenizer.nextToken();
        String author = stringTokenizer.nextToken();
        return new Commit(sha, message, OffsetDateTime.parse(date), author);
    }

    /**
     * Executes the git log command on the specified local repo directory with a formatter for later parsing.
     * The limit and offset flags are used to replicate the pagination functionality like on a DB.
     *
     * @param gitRepo the git repo
     * @param limit   the limit
     * @param offset  the offset
     * @return the list
     */
    public List<Commit> logFormatted(Path gitRepo, int limit, int offset) {
        log.info("Executing get commits with limit={} offset={} on gitRepo={}", limit, offset, gitRepo);
        return command.execute(gitRepo, "git", "log",
                "--pretty="+GIT_LOG_FORMAT,
                "--date=iso-strict",
                "-"+limit,
                "--skip="+offset )
                    .stream()
                    .map(GitCommands::parse)
                    .collect(Collectors.toList());
    }

    /**
     * Executes the git clone command that clone a remote repo specified by a url to the specified local
     * directory with the depth flag.
     * The depth flag is used to replicate the pagination functionality like on a DB.
     *
     * @param url           the url
     * @param repoDirectory the repo directory
     * @param depth         the depth
     */
    public void cloneRemoteToDirWithDepth(final String url, final String repoDirectory, final int depth){
        log.info("Executing git clone repo {} to dir {} with depth {}", url, repoDirectory, depth);
        command.execute("git", "clone", "--depth" ,String.valueOf(depth), url, repoDirectory);
        log.info("Git clone completed.");
    }

    /**
     * Executes a the git fetch command with the depth flag.
     * The depth flag is used to replicate the pagination functionality like on a DB.
     *
     * @param repoDirectory the repo directory
     * @param depth         the depth
     */
    public void fetchWithDepth(final String repoDirectory, final int depth){
        log.info("Executing git pull repo {} with depth {} ...", repoDirectory, depth);
        command.execute(Paths.get(repoDirectory), "git", "fetch", "--depth" , String.valueOf(depth));
        log.info("Git pull completed.");
    }
}