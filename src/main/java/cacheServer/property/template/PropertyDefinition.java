package cacheServer.property.template;

import cacheServer.property.PropertyName;
import cacheServer.property.typing.PropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public final class PropertyDefinition extends PropertyTemplate {
    @NotNull
    protected final PropertyType propertyType;

    public PropertyDefinition(@NotNull PropertyType propertyType,
                              @NotNull PropertyName propertyIdentifier) {
        super(propertyIdentifier);
        this.propertyType = propertyType;
    }

    @NotNull
    public PropertyInstance createInstance(PropertySample propertySample)
    {
        assert this.propertyIdentifier == propertySample.propertyIdentifier;
        return new PropertyInstance(
                this.propertyType,
                this.propertyIdentifier,
                this.propertyType.convert(propertySample.propertyStringValue)
        );
    }

    @NotNull
    public static LinkedHashSet<PropertyDefinition> makePropertyDefinitions(
            @NotNull LinkedHashMap<PropertyName, PropertyType> properties
    )
    {
        return properties.
                entrySet().
                stream().
                map(x -> new PropertyDefinition(x.getValue(), x.getKey())).
                collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
