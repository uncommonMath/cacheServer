package cacheServer.predicating;

import org.jetbrains.annotations.NotNull;

public interface Predicate<T> {
    boolean check(@NotNull T object);
}
