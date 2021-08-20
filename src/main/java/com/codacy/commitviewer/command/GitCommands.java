package com.codacy.commitviewer.command;

import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

@Component
@AllArgsConstructor
public class GitCommands {

    private final Command command;

    public void checkout(Path directory){
        command.execute();
    }
}
