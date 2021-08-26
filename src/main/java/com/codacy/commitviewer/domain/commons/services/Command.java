package com.codacy.commitviewer.domain.commons.services;

import java.nio.file.Path;
import java.util.List;

public interface Command {

    List<String> execute(Path directory, String... commands);

    public List<String> execute(String... commands);
}
