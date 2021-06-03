package cacheServer.property.template;

import cacheServer.property.PropertyName;
import cacheServer.property.typing.PropertyType;
import cacheServer.property.typing.PropertyValue;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public final class PropertyInstance extends PropertyTemplate {
    @NotNull
    protected final PropertyType propertyType;
    @NotNull
    protected final PropertyValue propertyValue;

    PropertyInstance(@NotNull PropertyType propertyType,
                     @NotNull PropertyName propertyIdentifier,
                     @NotNull PropertyValue propertyValue) {
        super(propertyIdentifier);
        this.propertyType = propertyType;
        this.propertyValue = propertyValue;
    }

    public boolean isEqualWith(PropertySample propertySample)
    {
        assert this.propertyIdentifier == propertySample.propertyIdentifier;
        return this.propertyType.compare(
                this.propertyValue,
                this.propertyType.convert(propertySample.propertyStringValue)
        ) == 0;
    }

    public boolean isGreaterWith(PropertySample propertySample)
    {
        assert this.propertyIdentifier == propertySample.propertyIdentifier;
        return this.propertyType.compare(
                this.propertyValue,
                this.propertyType.convert(propertySample.propertyStringValue)
        ) > 0;
    }

    public boolean isLessWith(PropertySample propertySample)
    {
        assert this.propertyIdentifier == propertySample.propertyIdentifier;
        return this.propertyType.compare(
                this.propertyValue,
                this.propertyType.convert(propertySample.propertyStringValue)
        ) < 0;
    }

    @Override
    public String toString()
    {
        //noinspection StringBufferReplaceableByString
        var builder = new StringBuilder();
        builder.append("'");
        builder.append(propertyIdentifier.propertyName());
        builder.append("'");
        builder.append(": ");
        builder.append(propertyType.convert(propertyValue));
        return builder.toString();
    }

    @NotNull
    public static LinkedHashSet<PropertyInstance> makePropertyInstances(
            @NotNull LinkedHashSet<PropertyDefinition> propertyTemplates,
            @NotNull LinkedHashSet<PropertySample> propertySamples
    )
    {
        return propertyTemplates.
                stream().
                map(x -> x.createInstance(x.getSimilarTemplateByName(propertySamples).
                        orElseThrow(() ->
                                new IllegalArgumentException(x.getPropertyIdentifier().propertyName())))).
                collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
