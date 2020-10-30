package io.github.sameei.interviews.quantcast.codingexercise.util;

import io.github.sameei.interviews.quantcast.codingexercise.launcher.Launcher;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class StdOutCollector extends OutputStream implements Closeable {

    private final ArrayList<String> lines;
    private StringBuilder buffer;
    private PrintStream priorOutput;

    public StdOutCollector() {
        lines = new ArrayList<>();
        buffer = null;
        priorOutput = System.out;
        System.setOut(new PrintStream(this));
    }

    @Override
    public void write(int b) throws IOException {

        if (b == '\n' && buffer == null) {
            lines.add("");
            return;
        }

        if (b == '\n' && buffer != null) {
            lines.add(buffer.toString());
            buffer = null;
            return;
        }

        if (buffer == null) {
            buffer = new StringBuilder();
        }
        buffer.append((char) b);
    }

    public List<String> lines() {
        return lines;
    }

    @Override
    public void close() throws IOException {
        System.setOut(priorOutput);
        super.close();
    }

    public static List<String> collect(Runnable runnable) {
        try (StdOutCollector collector = new StdOutCollector()) {
            runnable.run();
            return collector.lines();
        } catch (IOException cause) {
            throw new RuntimeException(cause);
        }
    }
}
