package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.launcher.CLI;
import io.github.sameei.interviews.quantcast.codingexercise.launcher.Launcher;
import io.github.sameei.interviews.quantcast.codingexercise.util.StdOutCollector;
import org.apache.commons.cli.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class CliTest {

    @Test
    public void howItWorks() throws ParseException {
        Options opts =
                new Options()
                        .addOption("f", true, "Path to the File")
                        .addOption("d", true, "Date to filter DataSet");

        CommandLineParser parser = new DefaultParser();
        CommandLine args =
                parser.parse(opts, new String[]{"-f", "/tmp/not-found", "-d", "2018-11-02"});

        assertThat(args.hasOption("f")).isTrue();
        assertThat(args.getOptionValue("f")).isEqualTo("/tmp/not-found");
        assertThat(args.hasOption("d")).isTrue();
        assertThat(args.getOptionValue("d")).isEqualTo("2018-11-02");
    }

    @Test
    public void invalidFilePath() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{}))
                .withMessage("File ('f') is required!");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{"-f", "-d", "2018-11-12"}))
                .withMessage("Missing Option Value: 'f'");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{"-f", "/nowhere"}))
                .withMessage("Given file path ('f') doesn't exists: /nowhere");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{"-f", "/tmp"}))
                .withMessage("Given file path ('f') refers to a directory: /tmp");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{"-f", "./data-sample/cookies.csv"}))
                .withMessage("Date ('d') is required!");
    }

    @Test
    public void invalidDate() {

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{"-f", "./data-sample/cookies.csv"}))
                .withMessage("Date ('d') is required!");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{
                        "-f", "./data-sample/cookies.csv", "-d"}))
                .withMessage("Missing Option Value: 'd'");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{
                        "-f", "./data-sample/cookies.csv", "-d", "2018"}))
                .withMessageStartingWith("Invalid Date ('d') value");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{
                        "-f", "./data-sample/cookies.csv", "-d", "2018-11"}))
                .withMessageStartingWith("Invalid Date ('d') value");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CLI.parseCli(new String[]{
                        "-f", "./data-sample/cookies.csv", "-d", "2018-XX-12"}))
                .withMessageStartingWith("Invalid Date ('d') value");

        assertThatNoException()
                .isThrownBy(() -> CLI.parseCli(new String[]{
                        "-f", "./data-sample/cookies.csv", "-d", "2018-11-09"}));
    }

    @Test
    public void runMain() throws IOException {

        DataSetStaticCSV dataset = new DataSetStaticCSV();
        File file = dataset.tempFile();
        String filePath = file.getPath();

        for (Map.Entry<String, List<String>> dateAndSet : dataset.resultSetPerDay.entrySet()) {
            List<String> lines = StdOutCollector.collect(() -> {
                Launcher.main(new String[]{"-f", filePath, "-d", dateAndSet.getKey()});
            });
            System.out.printf(
                    "For '%s' : %s; Expected: %s\n",
                    dateAndSet.getKey(),
                    lines,
                    dateAndSet.getValue());
            assertThat(lines).containsAll(dateAndSet.getValue());
        }
    }

    @Test
    public void runMainWithRemoteDate() throws IOException {

        DataSetStaticCSV dataset = new DataSetStaticCSV();
        File file = dataset.tempFile();
        String filePath = file.getPath();

        List<String> lines = StdOutCollector.collect(() -> {
            Launcher.main(new String[]{"-f", filePath, "-d", "2018-01-01", "-e", "simple"});
        });
        assertThat(lines).contains("NONE_EMPTY");

        lines = StdOutCollector.collect(() -> {
            Launcher.main(new String[]{"-f", filePath, "-d", "2018-01-01", "-e", "lazy"});
        });
        assertThat(lines).contains("NONE_EMPTY");

        lines = StdOutCollector.collect(() -> {
            Launcher.main(new String[]{"-f", filePath, "-d", "2018-01-01", "-e", "weired"});
        });
        assertThat(lines).contains("NONE_EMPTY");
    }

}
