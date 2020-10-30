package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.counting.DataParser;
import org.javatuples.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Random;

public class DataSetDynamic {

    public static final int DEFAULT_YEAR = 2020;
    public static final int DEFAULT_MN = 1;
    public static final int DEFAULT_SEC = 4;

    public String stringDT(int y, int m, int d, int h, int mn, int ss) {
        return String.format("%04d-%02d-%02dT%02d:%02d:%02d+00:00", y, m, d, h, mn, ss);
    }

    public String stringDT(int m, int d, int h) {
        return stringDT(DEFAULT_YEAR, m, d, h, DEFAULT_MN, DEFAULT_SEC);
    }

    public LocalDate dt(int y, int m, int d) {
        return LocalDate.of(y, m, d);
    }

    public LocalDate dt( int m, int d) {
        return dt(DEFAULT_YEAR, m, d);
    }

    private Random random = new Random();
    public String randomCookie(int bound) {
        return String.format("%04d", random.nextInt(bound));
    }

    public class GeneratedSet {
        public final ArrayList<String> lines = new ArrayList<>();
        public final ArrayList<Pair<String, OffsetDateTime>> records = new ArrayList<>();
    }


    public DataParser parser = DataParser.withDefaults();
    public GeneratedSet generate(
            int ma, int mb,
            int da, int db,
            int ha, int hb,
            int cc
    ) {
        GeneratedSet gs = new GeneratedSet();
        for (int mi=ma; mi >= mb; mi--) {
            for(int di=da; di >= db; di--) {
                for (int hi=ha; hi >= hb; hi--) {
                    String cookie = randomCookie(cc);
                    String str = stringDT(mi, di, hi);
                    OffsetDateTime dt = parser.parseDateTime(str);
                    gs.lines.add(String.format("%s, %s", cookie, str));
                    gs.records.add(Pair.with(cookie, dt));
                }
            }
        }
        return gs;
    }

    public File toTempFile(GeneratedSet gs, boolean withHeader) throws IOException {
            File file = File.createTempFile("temp", ".csv");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            if (withHeader) {
                writer.write("cookie, timestamp\n");
            }
            for(String s: gs.lines) {
                writer.write(s);
                writer.write('\n');
            }
            writer.close();
            return file;
    }

    public File toTempFile(GeneratedSet gs) throws IOException {
        return toTempFile(gs, false);
    }


}
