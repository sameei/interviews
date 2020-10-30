package io.github.sameei.interviews.quantcast.codingexercise.launcher;

import org.javatuples.Pair;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public interface CountingEngine {

    enum Type {
        SIMPLE, LAZY, WEIRED
    }

    List<Pair<String, Integer>> apply(Path file, LocalDate date, boolean fileHasHeader,
                                      boolean failfast);


}
