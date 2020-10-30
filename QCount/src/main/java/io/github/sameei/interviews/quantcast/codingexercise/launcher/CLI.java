package io.github.sameei.interviews.quantcast.codingexercise.launcher;

import io.github.sameei.interviews.quantcast.codingexercise.counting.DataParser;
import io.github.sameei.interviews.quantcast.codingexercise.framework.BadDataFormatException;
import org.apache.commons.cli.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class CLI {

    public static final String APP_NAME = "QCount";

    public static final String OPT_FILE = "f";
    public static final String OPT_DATE = "d";
    public static final String OPT_NO_HEADER = "noheader";
    public static final String OPT_COUNTING_ENGINE = "e";
    public static final String OPT_FAIL_FAST = "failfast";

    public static Options options() {
        return new Options()
                .addOption(OPT_FILE, true, "Path to the File")
                .addOption(OPT_DATE, true, "Date to filter DataSet")
                .addOption(Option.builder()
                        .longOpt(OPT_NO_HEADER)
                        .hasArg(false)
                        .desc("If the csv-file has no header").build())
                .addOption(OPT_COUNTING_ENGINE, true,
                        "Select Counting Engine: SIMPLE, LAZY, WEIRED; Default: SIMPLE")
                .addOption(Option.builder()
                        .longOpt(OPT_FAIL_FAST)
                        .desc("FailFast: Fail if encountered malformed csv-record; Default=false")
                        .build());
    }

    protected static IllegalArgumentException illegalArg(String format, Object... args) {
        return new IllegalArgumentException(String.format(format, args));
    }

    public static void exitWithError(String message) {
        System.out.println(message);
        new HelpFormatter().printHelp(APP_NAME, options());
        System.exit(78); // TODO Check the Code: Whether it's for invalid config/options
    }

    public static void exitWithError(String message, Throwable cause) {
        System.out.println(message);
        System.out.println("Error in Detail:");
        cause.printStackTrace();
        System.exit(1);
    }

    public static Setting parseCli(String[] in) {
        try {
            CommandLineParser cliParser = new DefaultParser();
            CommandLine args = cliParser.parse(options(), in);

            if (!args.hasOption(OPT_FILE))
                throw illegalArg("File ('%s') is required!", OPT_FILE);

            Path filePath = Paths.get(args.getOptionValue(OPT_FILE));

            if (!Files.exists(filePath))
                throw illegalArg("Given file path ('%s') doesn't exists: %s", OPT_FILE, filePath);

            if (Files.isDirectory(filePath))
                throw illegalArg("Given file path ('%s') refers to a directory: %s", OPT_FILE, filePath);

            if (!Files.isReadable(filePath))
                throw illegalArg("Given file path ('%s') is not readable: %s", OPT_FILE, filePath);

            if (!args.hasOption(OPT_DATE))
                throw illegalArg("Date ('%s') is required!", OPT_DATE);

            DataParser dataParser = DataParser.withDefaults();

            LocalDate date = null;
            try {
                date = dataParser.parseDate(args.getOptionValue(OPT_DATE));
            } catch (BadDataFormatException cause) {
                throw illegalArg("Invalid Date ('%s') value : '%s'; " +
                                "date values should be compatible to" +
                                " this pattern: '%s'",
                        OPT_DATE, args.getOptionValue(OPT_DATE),
                        DataParser.DEFAULT_DATE_PATTERN);
            }

            boolean fileHasHeader = !args.hasOption(OPT_NO_HEADER);

            boolean failFast = args.hasOption(OPT_FAIL_FAST);

            CountingEngine.Type engine = CountingEngine.Type.SIMPLE;
            if (args.hasOption(OPT_COUNTING_ENGINE)) {
                engine = CountingEngine.Type.valueOf(args.getOptionValue(OPT_COUNTING_ENGINE).toUpperCase());
            }


            return new Setting(filePath, date, engine, fileHasHeader, failFast);

        } catch (MissingArgumentException cause) {
            throw illegalArg("Missing Option Value: '%s'", cause.getOption().getOpt());
        } catch (ParseException cause) {
            throw new RuntimeException(cause);
        }
    }

    public static Setting parseCliOrExit(String[] in) {
        try {
            return parseCli(in);
        } catch (IllegalArgumentException cause) {
            exitWithError("INVALID_ARG: " + cause.getMessage());
        } catch (RuntimeException cause) {
            exitWithError("ERROR: " + cause.getMessage());
        }

        exitWithError("NEVER SHALL HAPPEN");
        return null;
    }

    public static class Setting {
        private final Path file;
        private final LocalDate date;
        private final CountingEngine.Type countingEngine;
        private final boolean fileHasHeader;
        private final boolean failFast;

        public Setting(
                Path file, LocalDate date,
                CountingEngine.Type countingEngine,
                boolean fileHasHeader,
                boolean failFast) {
            this.file = file;
            this.date = date;
            this.countingEngine = countingEngine;
            this.fileHasHeader = fileHasHeader;
            this.failFast = failFast;
        }

        public Path getFile() {
            return file;
        }

        public LocalDate getDate() {
            return date;
        }

        public CountingEngine.Type getCountingEngine() {
            return countingEngine;
        }

        public boolean getFileHasHeader() {
            return fileHasHeader;
        }

        public boolean getFailFast() {
            return failFast;
        }
    }

}
