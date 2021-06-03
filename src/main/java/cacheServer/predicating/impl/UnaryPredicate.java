package cacheServer.predicating.impl;

import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class UnaryPredicate
extends cacheServer.predicating.UnaryPredicate<StoreObject> implements Predicate {
    protected UnaryPredicate(@NotNull Predicate op) {
        super(op);
    }
}
