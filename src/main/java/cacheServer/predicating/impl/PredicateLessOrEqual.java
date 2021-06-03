package cacheServer.predicating.impl;

import cacheServer.property.template.PropertySample;
import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class PredicateLessOrEqual extends ConstPredicate<PropertySample> {
    public PredicateLessOrEqual(PropertySample constantValue) {
        super(constantValue);
    }

    @Override
    public boolean check(@NotNull StoreObject object) {
        var propertyInstance =
                this.constantValue.getSimilarTemplateByName(object.getPropertyInstances());
        assert propertyInstance.isPresent();
        return propertyInstance.get().isLessWith(this.constantValue) ||
               propertyInstance.get().isEqualWith(this.constantValue);
    }
}
