package cacheServer.predicating.impl;

import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class PredicateOr extends BinaryPredicate {
    public PredicateOr(@NotNull Predicate lhr, @NotNull Predicate rhs) {
        super(lhr, rhs);
    }

    @Override
    public boolean check(@NotNull StoreObject object) {
        return this.lhs.check(object) || this.rhs.check(object);
    }
}
