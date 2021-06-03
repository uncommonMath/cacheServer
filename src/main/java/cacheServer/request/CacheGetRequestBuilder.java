package cacheServer.request;

import cacheServer.core.CacheManager;
import cacheServer.core.CacheRequestBuilder;
import cacheServer.predicating.impl.Predicate;
import cacheServer.predicating.impl.PredicateEqual;
import cacheServer.property.PropertyName;
import cacheServer.property.PropertyParam;
import cacheServer.property.template.PropertySample;
import cacheServer.property.typing.PropertyRawValue;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;

public class CacheGetRequestBuilder extends CacheRequestBuilder {
    @NotNull
    private final LinkedHashSet<Predicate> predicates;

    public CacheGetRequestBuilder(@NotNull CacheManager cacheManager) {
        super(cacheManager);
        this.predicates = new LinkedHashSet<>();
    }

    @Override
    @NotNull
    public CacheGetRequestBuilder with(@NotNull PropertyParam... params) {
        this.predicates.add(new PredicateEqual(
                new PropertySample((PropertyName)params[0], ((PropertyRawValue)params[1]).propertyRawValue())
        ));
        return this;
    }

    @Override
    @NotNull
    public CacheGetRequestBuilder with(@NotNull Object obj) {
        if (obj instanceof Predicate p){
            this.predicates.add(p);
        } else {
            assert false;
        }
        return this;
    }

    @Override
    protected void validate() {
    }

    @NotNull
    public LinkedHashSet<Predicate> getPredicates() {
        return predicates;
    }
}
