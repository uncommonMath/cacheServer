package cacheServer.predicating.impl;

import cacheServer.property.template.PropertySample;
import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class PredicateEqual extends ConstPredicate<PropertySample> {
    public PredicateEqual(PropertySample constantValue) {
        super(constantValue);
    }

    @Override
    public boolean check(@NotNull StoreObject object) {
        var propertyInstance =
                this.constantValue.getSimilarTemplateByName(object.getPropertyInstances());
        assert propertyInstance.isPresent();
        return propertyInstance.get().isEqualWith(this.constantValue);
    }
}
