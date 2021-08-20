package com.codacy.commitviewer.service;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class CommitService {

    public void init(){

        Repository newlyCreatedRepo = FileRepositoryBuilder.create(
                new File("/tmp/new_repo/.git"));
        newlyCreatedRepo.readMergeHeads()

    }
}
