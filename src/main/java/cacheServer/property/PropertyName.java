package cacheServer.property;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record PropertyName(@NotNull String propertyName) implements PropertyParam {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyName that = (PropertyName) o;
        return propertyName.equals(that.propertyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName);
    }
}
