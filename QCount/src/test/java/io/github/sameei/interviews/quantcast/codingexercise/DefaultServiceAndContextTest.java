package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.framework.OverEngineered;
import io.github.sameei.interviews.quantcast.codingexercise.frameworkimpl.ContextDefaultImpl;
import io.github.sameei.interviews.quantcast.codingexercise.counting.Counter;
import io.github.sameei.interviews.quantcast.codingexercise.frameworkimpl.ServiceImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class DefaultServiceAndContextTest {

    @Test
    public void catchIllegalStateExceptionOnNonFunctionalContext() {

        OverEngineered.Context<Counter> ctx =
                ContextDefaultImpl.of(new Counter());

        ctx.cancel();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> ctx.cancel())
                .withMessage("Illegal TaskState Change: CANCELED -> CANCELED");
    }

    @Test
    public void runServiceWithEmptyIterable() {

        OverEngineered.Result<Counter> result =
                ServiceImpl.instance().run(
                        Collections.EMPTY_LIST,
                        ContextDefaultImpl.supplier(new Counter()),
                        (c, i) -> {
                        });

        assertThat(result.getResultState()).isEqualTo(OverEngineered.ResultState.FINISHED);
        assertThat(result.getFailCause()).isEmpty();
        assertThat(result.getTaskState()).matches(i -> i.size() == 0);
    }

    @Test
    public void runServiceAndCountNames() {

        OverEngineered.Result<Counter> result =
                ServiceImpl.<String, Counter>instance().run(
                        Arrays.asList("Reza", "Ali", "Moein", "Reza", "Hossein"),
                        ContextDefaultImpl.supplier(new Counter()),
                        (i, c) -> c.state().inc(i) );

        assertThat(result.getResultState()).isEqualTo(OverEngineered.ResultState.FINISHED);
        assertThat(result.getFailCause()).isEmpty();
        Counter counter = result.getTaskState();
        assertThat(counter).matches(i -> i.size() == 4);
        assertThat(counter.get("Reza")).isEqualTo(2);
        assertThat(counter.get("Ali")).isEqualTo(1);
        assertThat(counter.get("NonExists")).isEqualTo(0);
        assertThat(counter.keys()).containsAll(Arrays.asList("Reza", "Ali", "Moein", "Hossein"));
    }

    @Test
    public void cancelTheTaskInMiddle() {

        OverEngineered.Result<Counter> result =
                ServiceImpl.<String, Counter>instance().run(
                        Arrays.asList("Reza", "Ali", "CANCEL", "Reza", "Hossein"),
                        ContextDefaultImpl.supplier(new Counter()),
                        (i, c) -> {
                            if (i.equals("CANCEL")) c.cancel();
                            else c.state().inc(i);
                        });

        assertThat(result.getResultState()).isEqualTo(OverEngineered.ResultState.CANCELED);
        assertThat(result.getFailCause()).isEmpty();
        Counter counter = result.getTaskState();
        assertThat(counter).matches(i -> i.size() == 2);
        assertThat(counter.get("Reza")).isEqualTo(1);
        assertThat(counter.get("Ali")).isEqualTo(1);
        assertThat(counter.keys()).containsAll(Arrays.asList("Reza", "Ali"));
    }

    @Test
    public void failTheTaskInMiddle() {

        OverEngineered.Result<Counter> result =
                ServiceImpl.<String, Counter>instance().run(
                        Arrays.asList("Reza", "Ali", "CANCEL", "Reza", "Hossein"),
                        ContextDefaultImpl.supplier(new Counter()),
                        (i, c) -> {
                            if (i.equals("CANCEL")) throw new IllegalArgumentException("HAHA");
                            else c.state().inc(i);
                        });

        assertThat(result.getResultState()).isEqualTo(OverEngineered.ResultState.FAILED);
        assertThat(result.getFailCause())
                .isPresent()
                .get()
                .isInstanceOf(IllegalArgumentException.class)
                .matches(i -> i.getMessage().equals("HAHA"));
        Counter counter = result.getTaskState();
        assertThat(counter).matches(i -> i.size() == 2);
        assertThat(counter.get("Reza")).isEqualTo(1);
        assertThat(counter.get("Ali")).isEqualTo(1);
        assertThat(counter.keys()).containsAll(Arrays.asList("Reza", "Ali"));
    }


}
