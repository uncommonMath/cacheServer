package cacheServer.predicating;

import org.jetbrains.annotations.NotNull;

public abstract class UnaryPredicate<T> implements Predicate<T> {
    @NotNull
    protected final Predicate<T> op;

    protected UnaryPredicate(@NotNull Predicate<T> op) {
        this.op = op;
    }
}
