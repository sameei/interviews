package io.github.sameei.interviews.quantcast.codingexercise.framework;

import org.javatuples.Pair;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public interface Counting {
    List<Pair<String, Integer>> apply(File file, LocalDate date);
}
