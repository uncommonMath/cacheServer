package cacheServer.property.typing;

import cacheServer.property.PropertyParam;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PropertyTypeRepository {
    @NotNull
    public static final PropertyTypeRepository PROPERTY_TYPE_REPOSITORY = new PropertyTypeRepository();

    @NotNull
    private final HashMap<String, PropertyType> types;

    private PropertyTypeRepository()
    {
        this.types = new HashMap<>();
        this.types.put("Integer", new IntegerProperty());
        this.types.put("Float", new FloatProperty());
    }

    @NotNull
    public PropertyParam get(String name)
    {
        return this.types.get(name);
    }
}
