package cacheServer.predicating.impl;

import cacheServer.store.StoreObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class PredicateNot extends UnaryPredicate {
    public PredicateNot(@NotNull Predicate op) {
        super(op);
    }

    @Override
    public boolean check(@NotNull StoreObject object) {
        return !this.op.check(object);
    }
}
