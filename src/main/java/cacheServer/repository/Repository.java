package cacheServer.repository;

import cacheServer.predicating.impl.Predicate;
import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Repository {
    void add(@NotNull StoreObject storeObject);

    List<StoreObject> get(Predicate... predicates);

    void remove(Predicate... predicates);
}
