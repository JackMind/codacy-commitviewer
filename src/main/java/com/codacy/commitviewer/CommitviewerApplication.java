package com.codacy.commitviewer;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class CommitviewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommitviewerApplication.class, args);
    }

}
