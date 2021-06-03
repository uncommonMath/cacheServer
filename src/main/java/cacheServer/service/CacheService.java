package cacheServer.service;

import cacheServer.querying.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CacheService {
    @Nullable
    String executeQuery(@NotNull String query) throws ParseException;
}
