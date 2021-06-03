package cacheServer.predicating.impl;

import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

public abstract class ConstPredicate<J>
extends cacheServer.predicating.ConstPredicate<StoreObject, J> implements Predicate {
    public ConstPredicate(@NotNull J constantValue) {
        super(constantValue);
    }
}
