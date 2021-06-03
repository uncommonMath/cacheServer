package cacheServer.property.typing;

import org.jetbrains.annotations.NotNull;

public final class IntegerProperty extends PropertyType {
    @Override
    @NotNull
    public PropertyValue convert(@NotNull String stringData) {
        return new PropertyValue(Integer.parseInt(stringData));
    }

    @Override
    @NotNull
    public String convert(@NotNull PropertyValue valueData) {
        return Integer.toString((Integer)valueData.propertyValue());
    }

    @Override
    public int compare(PropertyValue propertyValue, PropertyValue t1) {
        return Integer.compare((Integer)propertyValue.propertyValue(), (Integer)t1.propertyValue());
    }
}
