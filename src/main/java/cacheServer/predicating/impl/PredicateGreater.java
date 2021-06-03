package cacheServer.predicating.impl;

import cacheServer.property.template.PropertySample;
import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class PredicateGreater extends ConstPredicate<PropertySample> {
    public PredicateGreater(PropertySample constantValue) {
        super(constantValue);
    }

    @Override
    public boolean check(@NotNull StoreObject object) {
        var propertyInstance =
                this.constantValue.getSimilarTemplateByName(object.getPropertyInstances());
        assert propertyInstance.isPresent();
        return propertyInstance.get().isGreaterWith(this.constantValue);
    }
}
