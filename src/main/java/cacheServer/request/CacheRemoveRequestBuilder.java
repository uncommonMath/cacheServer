package cacheServer.request;

import cacheServer.core.CacheManager;
import org.jetbrains.annotations.NotNull;

public final class CacheRemoveRequestBuilder extends CacheGetRequestBuilder {
    public CacheRemoveRequestBuilder(@NotNull CacheManager cacheManager) {
        super(cacheManager);
    }
}
