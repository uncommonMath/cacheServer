package cacheServer.request;

import cacheServer.core.CacheManager;
import cacheServer.core.CacheRequestBuilder;
import cacheServer.property.PropertyName;
import cacheServer.property.PropertyParam;
import cacheServer.property.typing.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

public final class CacheInitRequestBuilder extends CacheRequestBuilder {
    @NotNull
    private final LinkedHashMap<PropertyName, PropertyType> properties;

    @Nullable
    private PropertyName propertyKeyIdentifier;

    public CacheInitRequestBuilder(@NotNull CacheManager cacheManager) {
        super(cacheManager);
        this.properties = new LinkedHashMap<>();
    }

    @Override
    @NotNull
    public CacheInitRequestBuilder with(@NotNull PropertyParam... params) {
        this.properties.put((PropertyName)params[1], (PropertyType)params[0]);
        return this;
    }

    @Override
    @NotNull
    public CacheInitRequestBuilder with(@NotNull Object obj) {
        if (obj instanceof PropertyName p){
            assert this.properties.containsKey(p);
            this.propertyKeyIdentifier = p;
        } else {
            assert false;
        }
        return this;
    }

    @Override
    protected void validate() {
        if (propertyKeyIdentifier == null)
        {
            throw new IllegalStateException("propertyKeyIdentifier");
        }
    }

    @NotNull
    public LinkedHashMap<PropertyName, PropertyType> getProperties() {
        return properties;
    }

    @NotNull
    public PropertyName getPropertyKeyIdentifier() {
        assert propertyKeyIdentifier != null;
        return propertyKeyIdentifier;
    }
}
