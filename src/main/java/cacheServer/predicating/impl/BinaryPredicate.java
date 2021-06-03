package cacheServer.predicating.impl;

import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

public abstract class BinaryPredicate
extends cacheServer.predicating.BinaryPredicate<StoreObject> implements Predicate {
    public BinaryPredicate(@NotNull Predicate lhs, @NotNull Predicate rhs) {
        super(lhs, rhs);
    }
}
