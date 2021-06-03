package cacheServer.request;

import cacheServer.core.CacheManager;
import cacheServer.core.CacheRequestBuilder;
import cacheServer.property.PropertyName;
import cacheServer.property.PropertyParam;
import cacheServer.property.typing.PropertyRawValue;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public final class CacheAddRequestBuilder extends CacheRequestBuilder {
    @NotNull
    private final LinkedHashMap<PropertyName, PropertyRawValue> samples;

    public CacheAddRequestBuilder(@NotNull CacheManager cacheManager) {
        super(cacheManager);
        this.samples = new LinkedHashMap<>();
    }

    @Override
    @NotNull
    public CacheAddRequestBuilder with(@NotNull PropertyParam... params) {
        this.samples.put((PropertyName)params[0], (PropertyRawValue)params[1]);
        return this;
    }

    @Override
    @NotNull
    public CacheAddRequestBuilder with(@NotNull Object obj) {
        throw new UnsupportedOperationException("with(Object)");
    }

    @Override
    protected void validate() {
    }

    @NotNull
    public LinkedHashMap<PropertyName, PropertyRawValue> getSamples() {
        return samples;
    }
}
