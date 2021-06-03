package cacheServer.store;

import cacheServer.property.template.PropertyInstance;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
public final class StoreObject {
    @NotNull
    private final LinkedHashSet<PropertyInstance> propertyInstances;

    StoreObject(@NotNull LinkedHashSet<PropertyInstance> propertyInstances) {
        this.propertyInstances = propertyInstances;
    }

    public List<PropertyInstance> getPropertyInstances() {
        return propertyInstances.stream().toList();
    }

    @Override
    public String toString()
    {
        var builder = new StringBuilder();
        builder.append("{");
        if (!propertyInstances.isEmpty())
        {
            builder.append(propertyInstances.stream().findFirst().get());
        }
        for (var prop: propertyInstances.stream().skip(1).collect(Collectors.toList()))
        {
            builder.append(", ");
            builder.append(prop);
        }
        builder.append("}");
        return builder.toString();
    }
}
