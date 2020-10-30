package io.github.sameei.interviews.quantcast.codingexercise.launcher;

import io.github.sameei.interviews.quantcast.codingexercise.framework.BadDataFormatException;
import io.github.sameei.interviews.quantcast.codingexercise.counting.DataParser;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Launcher {

    private static Logger LOG = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] in) {
        try {
            LOG.info("Init ...");

            CLI.Setting args = CLI.parseCli(in);

            LOG.info("Setting: {}", args);

            LOG.info("CountingEngine Instantiating ...");
            CountingEngine engine =
                    CountingEngines.getEngine(args.getCountingEngine(), DataParser.withDefaults());

            LOG.info("Start Processing ...");
            List<Pair<String, Integer>> result =
                    engine.apply(args.getFile(), args.getDate(), args.getFileHasHeader(), args.getFailFast());

            if (result.isEmpty())
                System.out.println("NONE_EMPTY");
            else
                result.forEach(i -> System.out.println(i.getValue0()));

        } catch (BadDataFormatException cause) {
            LOG.error("Process Failed", cause);
            CLI.exitWithError("BAD_DATA_FILE:" + cause.getMessage(), cause);
        } catch (IllegalArgumentException cause) {
           CLI.exitWithError("INVALID_ARG: " + cause.getMessage());
        } catch (RuntimeException cause) {
            LOG.error("Process Failed", cause);
            CLI.exitWithError("ERROR: " + cause.getMessage(), cause);
        }
    }

}
