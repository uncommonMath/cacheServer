package cacheServer.service.impl;

import cacheServer.core.CacheManager;
import cacheServer.querying.CacheQuery;
import cacheServer.querying.ParseException;
import cacheServer.service.CacheService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@SuppressWarnings("unused")
@Service
public final class CacheServiceImpl implements CacheService {
    @NotNull
    private final CacheManager cacheManager;

    public CacheServiceImpl()
    {
        this.cacheManager = new CacheManager();
    }

    @Override
    @Nullable
    public String executeQuery(@NotNull String query) throws ParseException {
        var result = CacheQuery.executeQuery(this.cacheManager, query);
        if (result instanceof String s) return s;
        if (result instanceof ArrayList<?> a) return String.valueOf(a);
        return null;
    }
}
