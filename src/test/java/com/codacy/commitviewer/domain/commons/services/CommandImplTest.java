package com.codacy.commitviewer.domain.commons.services;

import com.codacy.commitviewer.domain.commons.exception.UnableToExecuteCommand;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


class CommandImplTest {

    private final CommandImpl victim = new CommandImpl();

    @Test
    void Assert_CommandOutputInListOfStrings(){
        List<String> output = victim.execute("ls");
        Assertions.assertNotNull(output);
    }

    @Test
    @SneakyThrows
    void Assert_CommandOutputInListOfStringsOnSpecifiedDir(){
        Path temp = Files.createTempDirectory("testCommand");

        List<String> output = Assertions.assertDoesNotThrow( () -> victim.execute(temp, "ls"));
        Assertions.assertNotNull(output);

        Files.delete(temp);
    }

    @Test
    void AssertThrow_OnErrorExecutingCommand(){
        Assertions.assertThrows(UnableToExecuteCommand.class, ()->victim.execute("unknownCommand"));
    }

}