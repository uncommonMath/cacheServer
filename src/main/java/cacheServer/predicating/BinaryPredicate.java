package cacheServer.predicating;

import org.jetbrains.annotations.NotNull;

public abstract class BinaryPredicate<T> implements Predicate<T> {
    @NotNull
    protected final Predicate<T> lhs;
    @NotNull
    protected final Predicate<T> rhs;

    public BinaryPredicate(@NotNull Predicate<T> lhs, @NotNull Predicate<T> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
