package cacheServer.core;

import cacheServer.predicating.impl.Predicate;
import cacheServer.repository.Repository;
import cacheServer.repository.RepositoryFactory;
import cacheServer.request.CacheAddRequestBuilder;
import cacheServer.request.CacheGetRequestBuilder;
import cacheServer.request.CacheInitRequestBuilder;
import cacheServer.request.CacheRemoveRequestBuilder;
import cacheServer.store.StoreObject;
import cacheServer.store.StoreStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class CacheManager {
    @NotNull
    private final Map<Class<? extends CacheRequestBuilder>, Function<? super CacheRequestBuilder, Object>>
            cacheRequestHandlers =
            Map.ofEntries(
                    Map.entry(CacheInitRequestBuilder.class, (x) ->
                            this.handleCacheInitRequest((CacheInitRequestBuilder)x)),
                    Map.entry(CacheAddRequestBuilder.class, (x) ->
                            this.handleCacheAddRequest((CacheAddRequestBuilder)x)),
                    Map.entry(CacheGetRequestBuilder.class, (x) ->
                            this.handleCacheGetRequest((CacheGetRequestBuilder)x)),
                    Map.entry(CacheRemoveRequestBuilder.class, (x) ->
                            this.handleCacheRemoveRequest((CacheRemoveRequestBuilder)x))
    );

    @Nullable
    private StoreStructure storeStructure;

    @NotNull
    private final Repository repository;

    public CacheManager()
    {
        this.repository = Objects.requireNonNull(RepositoryFactory.create());
    }

    @Nullable
    Object handleCacheRequest(@NotNull CacheRequestBuilder cacheRequestBuilder)
    {
        return this.cacheRequestHandlers.get(cacheRequestBuilder.getClass()).apply(cacheRequestBuilder);
    }

    @NotNull
    public CacheInitRequestBuilder beginInit()
    {
        return new CacheInitRequestBuilder(this);
    }

    @Nullable
    Object handleCacheInitRequest(@NotNull CacheInitRequestBuilder cacheInitRequestBuilder)
    {
        if (storeStructure != null) {
            throw new IllegalStateException("storeStructure");
        }
        this.storeStructure = new StoreStructure(
                cacheInitRequestBuilder.getProperties(),
                cacheInitRequestBuilder.getPropertyKeyIdentifier()
        );
        return null;
    }

    @NotNull
    public CacheAddRequestBuilder beginAdd()
    {
        return new CacheAddRequestBuilder(this);
    }

    @Nullable
    Object handleCacheAddRequest(@NotNull CacheAddRequestBuilder cacheAddRequestBuilder)
    {
        if (storeStructure == null) {
            throw new IllegalStateException("storeStructure");
        }
        this.repository.add(
                this.storeStructure.createInstance(
                        cacheAddRequestBuilder.getSamples()
                )
        );
        return null;
    }

    @NotNull
    public CacheGetRequestBuilder beginGet()
    {
        return new CacheGetRequestBuilder(this);
    }

    @NotNull
    List<StoreObject> handleCacheGetRequest(@NotNull CacheGetRequestBuilder cacheAddRequestBuilder)
    {
        if (storeStructure == null) {
            throw new IllegalStateException("storeStructure");
        }
        return this.repository.get(cacheAddRequestBuilder.getPredicates().toArray(Predicate[]::new));
    }

    @NotNull
    public CacheRemoveRequestBuilder beginRemove()
    {
        return new CacheRemoveRequestBuilder(this);
    }

    @Nullable
    List<StoreObject> handleCacheRemoveRequest(@NotNull CacheRemoveRequestBuilder cacheRemoveRequestBuilder)
    {
        this.repository.remove(cacheRemoveRequestBuilder.getPredicates().toArray(Predicate[]::new));
        return null;
    }
}
