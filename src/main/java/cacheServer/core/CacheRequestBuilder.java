package cacheServer.core;

import cacheServer.property.PropertyParam;
import org.jetbrains.annotations.NotNull;

public abstract class CacheRequestBuilder {
    @NotNull
    protected final CacheManager cacheManager;

    public CacheRequestBuilder(@NotNull CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }

    @NotNull
    public abstract CacheRequestBuilder with(@NotNull PropertyParam... params);
    @NotNull
    public abstract CacheRequestBuilder with(@NotNull Object obj);

    protected abstract void validate();

    public Object end()
    {
        this.validate();
        return this.cacheManager.handleCacheRequest(this);
    }
}
