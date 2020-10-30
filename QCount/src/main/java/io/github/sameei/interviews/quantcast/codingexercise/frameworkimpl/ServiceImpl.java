package io.github.sameei.interviews.quantcast.codingexercise.frameworkimpl;

import io.github.sameei.interviews.quantcast.codingexercise.framework.OverEngineered;

import java.util.Iterator;
import java.util.function.Supplier;

public class ServiceImpl<T,S> implements OverEngineered.Service<T, S> {

    @Override
    public OverEngineered.Result<S> run(Iterable<T> iterable, Supplier<OverEngineered.Context<S>> ctxFactory, OverEngineered.Action<T, S> action) {
        OverEngineered.Context<S> ctx = ctxFactory.get();
        Iterator<T> iterator = iterable.iterator();
        try {
            while (iterator.hasNext()) {
                T t = iterator.next();
                action.apply(t, ctx);
                if (ctx.getResultState() != OverEngineered.ResultState.FUNCTIONAL)
                    break;
            }
            if (ctx.getResultState() == OverEngineered.ResultState.FUNCTIONAL)
                ctx.setFinished();
        } catch (RuntimeException cause) {
            ctx.fail(cause);
        }
        return ctx;
    }

    public static <T,S> OverEngineered.Service<T,S> instance() {
        return new ServiceImpl<>();
    }
}
