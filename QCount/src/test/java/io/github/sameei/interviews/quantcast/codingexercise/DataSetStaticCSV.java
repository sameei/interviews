package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.counting.DataParser;
import org.javatuples.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataSetStaticCSV {

    public static final String DATETIME_PATTERN = DataParser.DEFAULT_DATETIME_PATTERN;
    public static final String DATE_PATTERN = DataParser.DEFAULT_DATE_PATTERN;

    public final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    public final DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public final DataParser dataParser = new DataParser(dtf, df);

    public final Pair<String, String> header = Pair.with("cookie", "timestamp");
    public final ArrayList<Pair<String, String>> lines = new ArrayList<>() {{
        add(Pair.with("AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00"));
        add(Pair.with("SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00"));
        add(Pair.with("5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00"));
        add(Pair.with("AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00"));
        add(Pair.with("SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00"));
        add(Pair.with("4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00"));
        add(Pair.with("fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"));
        add(Pair.with("4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00"));
    }};

    public final Map<String, List<String>> resultSetPerDay = new HashMap<>(){{
        put("2018-12-09", Arrays.asList("AtY0laUfhglK3lC7"));
        put("2018-12-08", Arrays.asList("SAZuXPGUrfbcn5UA", "4sMM2LxV07bPJzwf", "fbcn5UAVanZf6UtG"));
        put("2018-12-07", Arrays.asList("4sMM2LxV07bPJzwf"));
    }};

    public final HashMap<String, Integer> allAggregated = new HashMap<>() {{
        put("AtY0laUfhglK3lC7", 2);
        put("SAZuXPGUrfbcn5UA", 2);
        put("5UAVanZf6UtGyKVS", 1);
        put("4sMM2LxV07bPJzwf", 2);
        put("fbcn5UAVanZf6UtG", 1);
    }};

    public final String content =
            "cookie,timestamp\n" +
                    "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00\n" +
                    "SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00\n" +
                    "5UAVanZf6UtGyKVS,2018-12-09T07:25:00+00:00\n" +
                    "AtY0laUfhglK3lC7,2018-12-09T06:19:00+00:00\n" +
                    "SAZuXPGUrfbcn5UA,2018-12-08T22:03:00+00:00\n" +
                    "4sMM2LxV07bPJzwf,2018-12-08T21:30:00+00:00\n" +
                    "fbcn5UAVanZf6UtG,2018-12-08T09:30:00+00:00\n" +
                    "4sMM2LxV07bPJzwf,2018-12-07T23:30:00+00:00";

    public File tempFile() throws IOException {
        File file = File.createTempFile("temp", ".csv");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(content);
        writer.close();
        return file;
    }
}
