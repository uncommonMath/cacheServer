package cacheServer.predicating;

import org.jetbrains.annotations.NotNull;

public abstract class ConstPredicate<T, J> implements Predicate<T> {
    @NotNull
    public J constantValue;

    public ConstPredicate(@NotNull J constantValue) {
        this.constantValue = constantValue;
    }
}
