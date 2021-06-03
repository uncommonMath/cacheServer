package cacheServer.property.template;

import cacheServer.property.PropertyName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class PropertyTemplate {
    @NotNull
    protected final PropertyName propertyIdentifier;

    public PropertyTemplate(@NotNull PropertyName propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyTemplate that)) return false;
        return propertyIdentifier.equals(that.propertyIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyIdentifier);
    }

    @NotNull
    public PropertyName getPropertyIdentifier() {
        return propertyIdentifier;
    }

    @NotNull
    public <T extends PropertyTemplate> Optional<T> getSimilarTemplateByName(
            @NotNull Iterable<T> templates
    )
    {
        return StreamSupport.stream(templates.spliterator(), false).
                filter(x -> Objects.equals(x.propertyIdentifier, this.propertyIdentifier)).
                findAny();
    }
}
