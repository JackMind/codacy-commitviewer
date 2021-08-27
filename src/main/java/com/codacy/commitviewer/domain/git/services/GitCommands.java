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

    public static String format = "format:%H" +
            FIELD +
            "%s" +
            FIELD +
            "%ad" +
            FIELD +
            "%an";

    public static Commit parse(String output) {
        StringTokenizer stringTokenizer = new StringTokenizer(output, FIELD);
        String sha = stringTokenizer.nextToken();
        String message = stringTokenizer.nextToken();
        String date = stringTokenizer.nextToken();
        String author = stringTokenizer.nextToken();
        return new Commit(sha, message, OffsetDateTime.parse(date), author);
    }

    public List<Commit> gitLogFormatted(Path gitRepo, int limit, int offset) {
        log.debug("Executing get commits with limit={} offset={} on gitRepo={}", limit, offset, gitRepo);
        return command.execute(gitRepo, "git", "log",
                "--pretty=" + format, "--date=iso-strict", "-"+limit, "--skip="+offset)
                .stream()
                .map(GitCommands::parse)
                .collect(Collectors.toList());
    }

    public void cloneRemoteToDirWithDepth(final String url, final String repoDirectory, final int depth){
        log.info("Executing git clone repo {} to dir {} with depth {}", url, repoDirectory, depth);
        command.execute("git", "clone", "--depth" ,String.valueOf(depth), url, repoDirectory);
        log.info("Git clone completed.");
    }

    public void pullWithDepth(final String repoDirectory, final int depth){
        log.info("Executing git pull repo {} with depth {} ...", repoDirectory, depth);
        command.execute(Paths.get(repoDirectory), "git", "pull", "--depth" , String.valueOf(depth));
        log.info("Git pull completed.");
    }
}