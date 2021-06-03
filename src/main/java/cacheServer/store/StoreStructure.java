package cacheServer.store;

import cacheServer.property.PropertyName;
import cacheServer.property.template.PropertyDefinition;
import cacheServer.property.template.PropertyInstance;
import cacheServer.property.template.PropertySample;
import cacheServer.property.template.PropertyTemplate;
import cacheServer.property.typing.PropertyRawValue;
import cacheServer.property.typing.PropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Optional;

public final class StoreStructure {
    @NotNull
    private final LinkedHashSet<PropertyDefinition> propertyTemplates;
    @NotNull
    private final PropertyDefinition propertyTemplateKey;

    public StoreStructure(@NotNull LinkedHashMap<PropertyName, PropertyType> properties,
                          @NotNull PropertyName propertyReplacementIdentifier) {
        this.propertyTemplates = PropertyDefinition.makePropertyDefinitions(properties);
        Optional<PropertyDefinition> propertyTemplateKey =
                new PropertyTemplate(propertyReplacementIdentifier).
                        getSimilarTemplateByName(this.propertyTemplates);
        assert propertyTemplateKey.isPresent();
        this.propertyTemplateKey = propertyTemplateKey.get();
    }

    @NotNull
    public StoreObject createInstance(@NotNull LinkedHashMap<PropertyName, PropertyRawValue> samples)
    {
        var propertySamples = PropertySample.makePropertySamples(samples);
        return new StoreObject(PropertyInstance.makePropertyInstances(this.propertyTemplates, propertySamples));
    }

    @NotNull
    public PropertyDefinition getPropertyTemplateKey() {
        return propertyTemplateKey;
    }
}
