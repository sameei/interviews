package io.github.sameei.interviews.quantcast.codingexercise.framework;

import java.util.Optional;
import java.util.function.Supplier;

public interface OverEngineered {

    interface TaskState<S> {
        TaskState<S> cancel();
        TaskState<S> fail(Exception cause);
        S state();
    }

    enum ResultState {
        FUNCTIONAL, CANCELED, FINISHED, FAILED,
    }

    interface Result<S> {
        S getTaskState();
        ResultState getResultState();
        Optional<Throwable> getFailCause();
    }

    interface Context<S> extends TaskState<S>, Result<S> {
        Context<S> setFinished();
    }

    @FunctionalInterface
    interface Action <T, S> {
        void apply(T t, TaskState<S> ctx);
    }

    @FunctionalInterface
    interface Service<T,S> {
        Result<S> run(Iterable<T> iterable, Supplier<Context<S>> ctxFactory, Action<T,S> action);
    }
}
