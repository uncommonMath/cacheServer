package cacheServer.property.typing;

import cacheServer.property.PropertyParam;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public abstract class PropertyType implements PropertyParam, Comparator<PropertyValue> {
    PropertyType()
    {
    }

    @NotNull
    public abstract PropertyValue convert(@NotNull String stringData);

    @NotNull
    public abstract String convert(@NotNull PropertyValue valueData);
}
