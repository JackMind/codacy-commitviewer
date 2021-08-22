package com.codacy.commitviewer.domain.git;

import com.codacy.commitviewer.domain.command.Command;
import com.codacy.commitviewer.domain.entity.Commit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GitLogCommand {

    private final Command command;

    static String FIELD = "==FIELD==";

    static String format = "format:\"%H" +
            FIELD +
            "%s" +
            FIELD +
            "%ad" +
            FIELD +
            "%an\"";

    //--log-size
    public static Commit parse(String output){
        StringTokenizer stringTokenizer = new StringTokenizer(output, FIELD);
        String sha = stringTokenizer.nextToken();
        String message = stringTokenizer.nextToken();
        String date = stringTokenizer.nextToken();
        String author = stringTokenizer.nextToken();
        return new Commit(sha, message, OffsetDateTime.parse(date), author);
    }

    public List<Commit> execute(Path dir) {
        try {
            return command.execute(dir, "git", "log", "--pretty="+format, "--date=iso-strict")
                    .stream()
                    .map(GitLogCommand::parse)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
