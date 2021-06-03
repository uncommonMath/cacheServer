package cacheServer.predicating.impl;

import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

public interface Predicate extends cacheServer.predicating.Predicate<StoreObject> {
    boolean check(@NotNull StoreObject object);
}
