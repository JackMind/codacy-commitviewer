package com.codacy.commitviewer.command;

import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.nio.file.Path;

public interface Command {
    String execute(Path directory, String... commands) throws IOException, InterruptedException;
}
