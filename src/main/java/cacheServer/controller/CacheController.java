package cacheServer.controller;

import cacheServer.querying.ParseException;
import cacheServer.service.impl.CacheServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
public final record CacheController(@NotNull CacheServiceImpl cacheService) {
    public CacheController(@NotNull CacheServiceImpl cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/query/{query}")
    public String query(@PathVariable String query) throws ParseException {
        return this.cacheService.executeQuery(query);
    }
}
