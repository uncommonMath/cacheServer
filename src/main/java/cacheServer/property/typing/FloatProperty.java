package cacheServer.property.typing;

import org.jetbrains.annotations.NotNull;

public final class FloatProperty extends PropertyType {
    @Override
    @NotNull
    public PropertyValue convert(@NotNull String stringData) {
        return new PropertyValue(Float.parseFloat(stringData));
    }

    @Override
    @NotNull
    public String convert(@NotNull PropertyValue valueData) {
        return Float.toString((Float)valueData.propertyValue());
    }

    @Override
    public int compare(PropertyValue propertyValue, PropertyValue t1) {
        return Float.compare((Float) propertyValue.propertyValue(), (Float)t1.propertyValue());
    }
}
