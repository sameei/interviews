package io.github.sameei.interviews.quantcast.codingexercise.counting;

import io.github.sameei.interviews.quantcast.codingexercise.framework.BadDataFormatException;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataParser {

    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssxxx";

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static DataParser withDefaults() {
        return new DataParser(
                DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN),
                DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)
        );
    }

    private final DateTimeFormatter dtf;
    private final DateTimeFormatter df;

    @Override
    public String toString() {
        return "DataParser{" +
                "dtf=" + dtf +
                ", df=" + df +
                '}';
    }

    public DataParser(DateTimeFormatter dtf, DateTimeFormatter df) {
        this.dtf = dtf;
        this.df = df;
    }

    public LocalDate parseDate(String str) {
        try {
            return LocalDate.from(df.parse(str));
        } catch (DateTimeParseException cause) {
            throw BadDataFormatException.format(cause, "Invalid Date: %s", str);
        }
    }

    public OffsetDateTime parseDateTime(String str) {
        try {
            return OffsetDateTime.from(dtf.parse(str));
        } catch (DateTimeParseException cause){
            throw BadDataFormatException.format(cause, "Invalid DateTime: %s", str);
        }
    }

    public Pair<String, OffsetDateTime> parseCsvRecord(CSVRecord r) {
        try {
            return Pair.with(r.get(0).trim(), parseDateTime(r.get(1).trim()));
        } catch (BadDataFormatException cause) {
            throw BadDataFormatException.format(cause, "Invalid CSV Record: %s", r);
        }
    }


    public Pair<String, OffsetDateTime> parseLine(String line) {
        try {
            String[] splits = line.split(",");
            if (splits.length != 2)
                // TODO : Provide more information on exception! probably a custom exception!
                throw new IllegalArgumentException(String.format("Invalid Line: '%s'", line));
            return Pair.with(
                    splits[0].trim(),
                    parseDateTime(splits[1].trim())
            );
        } catch (RuntimeException cause) {
            throw BadDataFormatException.format(cause, "Invalid CSV Line: %s", line);
        }
    }

}
