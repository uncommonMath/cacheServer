package cacheServer.property.typing;

import cacheServer.property.PropertyParam;
import org.jetbrains.annotations.NotNull;

public record PropertyRawValue(@NotNull String propertyRawValue) implements PropertyParam {
}
