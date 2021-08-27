package com.codacy.commitviewer.domain.commons.services;

import com.codacy.commitviewer.domain.commons.exception.UnableToExecuteCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CommandImpl implements Command {

    @Override
    public List<String> execute(Path directory, String... commands) {
        Objects.requireNonNull(directory, "directory");
        return executeCommand(directory, commands);
    }

    @Override
    public List<String> execute(String... commands) {
        return executeCommand(null, commands);
    }

    private List<String> executeCommand(Path directory, String... commands) {
        try {
            Objects.requireNonNull(commands, "commands");

            ProcessBuilder pb = new ProcessBuilder();

            if(Objects.nonNull(directory)){
                pb.directory(directory.toFile());
            }
            pb.command(commands);

            Process process = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            List<String> output = in.lines().collect(Collectors.toList());
            List<String> errors = error.lines().collect(Collectors.toList());

            int exit = process.waitFor();

            in.close();
            error.close();

            log.debug("output: {}", String.join("\n", output));

            if (exit != 0) {
                log.error("error: {}", String.join("\n", errors));
                throw new UnableToExecuteCommand(String.format("runCommand returned %d", exit));
            }
            return output;
        } catch (IOException | InterruptedException ex) {
            throw new UnableToExecuteCommand("Unable to execute specified commands " + String.join(" ", commands) +
                    " exception: " + ex.getMessage());
        }
    }

}
