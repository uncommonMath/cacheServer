package cacheServer.property.template;

import cacheServer.property.PropertyName;
import cacheServer.property.typing.PropertyRawValue;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public final class PropertySample extends PropertyTemplate {
    @NotNull
    protected final String propertyStringValue;

    public PropertySample(@NotNull PropertyName propertyIdentifier,
                             @NotNull String propertyStringValue) {
        super(propertyIdentifier);
        this.propertyStringValue = propertyStringValue;
    }

    @NotNull
    public static LinkedHashSet<PropertySample> makePropertySamples(
            @NotNull LinkedHashMap<PropertyName, PropertyRawValue> samples
    )
    {
        return samples.
                entrySet().
                stream().
                map(x -> new PropertySample(x.getKey(), x.getValue().propertyRawValue())).
                collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
