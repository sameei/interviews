package io.github.sameei.interviews.quantcast.codingexercise.counting;

import io.github.sameei.interviews.quantcast.codingexercise.framework.IterableRich;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Loader {

    // Rewrite it's usage to be configurable
    public static final int DEFAULT_BUFFER = 1000000;

    public static IterableRich<CSVRecord> lazy(File file, CSVFormat format) throws IOException {
        CSVParser parser =
                CSVParser.parse(file, StandardCharsets.UTF_8, format);

        return new IterableRich<>(parser);
    }

    public static SortedLinesDesc allSortedLinesDesc(
            File file, DataParser dataParser, boolean skipFirstLine) throws IOException {
        ArrayList<String> buffer = new ArrayList<>();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader, DEFAULT_BUFFER);
        String line = null;
        if(skipFirstLine) bufferedReader.readLine();
        while ((line = bufferedReader.readLine()) != null) {
            buffer.add(line);
        }
        return new SortedLinesDesc(buffer, dataParser);
    }


    public static void allSortedLinesDesc(File file, DataParser parser) {
    }
}
