package io.github.sameei.interviews.quantcast.codingexercise.frameworkimpl;

import io.github.sameei.interviews.quantcast.codingexercise.framework.OverEngineered;

import java.util.Optional;
import java.util.function.Supplier;

public class ContextDefaultImpl<S> implements OverEngineered.Context<S> {

    private final S taskState;
    private OverEngineered.ResultState resultState;
    private Throwable failCause;


    public ContextDefaultImpl(S state) {
        this.taskState = state;
        this.resultState = OverEngineered.ResultState.FUNCTIONAL;
    }

    @Override
    public OverEngineered.ResultState getResultState() {
        return resultState;
    }

    public boolean isFunctional() {
        return this.resultState == OverEngineered.ResultState.FUNCTIONAL;
    }

    public boolean isCanceled() {
        return this.resultState == OverEngineered.ResultState.CANCELED;
    }


    public boolean isFinished() {
        return this.resultState == OverEngineered.ResultState.FINISHED;
    }

    public boolean isFailed() {
        return this.resultState == OverEngineered.ResultState.FAILED;
    }

    @Override
    public OverEngineered.TaskState<S> cancel() {
        if (!isFunctional())
            throw new IllegalStateChange(this.resultState, OverEngineered.ResultState.CANCELED);
        this.resultState = OverEngineered.ResultState.CANCELED;
        return this;
    }

    @Override
    public OverEngineered.TaskState<S> fail(Exception cause) {
        if (!isFunctional())
            throw new IllegalStateChange(this.resultState, OverEngineered.ResultState.FAILED);
        this.resultState = OverEngineered.ResultState.FAILED;
        this.failCause = cause;
        return this;
    }

    @Override
    public S state() {
        if(!this.isFunctional()) throw notFunctional();
        return this.taskState;
    }

    @Override
    public S getTaskState() {
        return this.taskState;
    }

    @Override
    public Optional<Throwable> getFailCause() {
        return Optional.ofNullable(failCause);
    }

    @Override
    public OverEngineered.Context<S> setFinished() {
        if(!this.isFunctional())
            throw new IllegalStateChange(this.resultState, OverEngineered.ResultState.FINISHED);
        this.resultState = OverEngineered.ResultState.FINISHED;
        return this;
    }

    // ===

    public static class IllegalStateChange extends IllegalStateException {
        private final OverEngineered.ResultState current;
        private final OverEngineered.ResultState demanded;

        public IllegalStateChange(OverEngineered.ResultState current, OverEngineered.ResultState demanded) {
            super(String.format("Illegal TaskState Change: %s -> %s", current, demanded));
            this.current = current;
            this.demanded = demanded;
        }
    }

    protected static IllegalStateException notFunctional() {
        return new IllegalStateException("Not Functional Context");
    }

    public static <S> OverEngineered.Context<S> of(S state) {
        return new ContextDefaultImpl<>(state);
    }

    public static <S> Supplier<OverEngineered.Context<S>> supplier(S state) {
        return () -> new ContextDefaultImpl<>(state);
    }

    public static <S> Supplier<OverEngineered.Context<S>> fromSupplier(Supplier<S> state) {
        return () -> new ContextDefaultImpl<>(state.get());
    }

}
