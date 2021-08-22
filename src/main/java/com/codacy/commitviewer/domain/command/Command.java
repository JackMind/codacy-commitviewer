package com.codacy.commitviewer.domain.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Command {

    default List<String> execute(Path directory, String... commands) throws IOException, InterruptedException {
        Objects.requireNonNull(directory, "directory");
        Objects.requireNonNull(commands, "commands");

        ProcessBuilder pb = new ProcessBuilder()
                .command(commands)
                .directory(directory.toFile());

        Process process = pb.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

        List<String> output = in.lines().collect(Collectors.toList());
        int exit = process.waitFor();

        in.close();

        if (exit != 0) {
            throw new AssertionError(String.format("runCommand returned %d", exit));
        }

        return output;
    }
}
