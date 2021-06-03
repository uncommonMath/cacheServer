package cacheServer.repository.impl;

import cacheServer.dataStructures.RTree;
import cacheServer.repository.Repository;
import cacheServer.predicating.impl.Predicate;
import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class RTreeRepository implements Repository {
    @NotNull
    private final RTree<StoreObject> objects;

    public RTreeRepository()
    {
        this.objects = new RTree<>();
    }

    @Override
    public void add(@NotNull StoreObject storeObject) {
        objects.insert(new float[]{0, 0}, new float[]{0, 0}, storeObject);
    }

    @Override
    public List<StoreObject> get(Predicate... predicates) {
        return objects.search(new float[]{}, new float[]{}).
                stream().
                filter(x -> Arrays.
                        stream(predicates).
                        allMatch(y -> y.check(x))
                ).
                collect(Collectors.toList());
    }

    @Override
    public void remove(Predicate... predicates) {
        this.get(predicates).forEach(x ->objects.delete(new float[]{}, x));
    }
}
