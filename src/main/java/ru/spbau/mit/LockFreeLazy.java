package net.yeputons.spbau.java;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

public class LockFreeLazy<T> implements Lazy<T> {
    // Similarly to `SingletonLazy`, volatile here is optional.
    // If `supplier` is null, then we are guaranteed to see change of `result` as well
    private Supplier<T> supplier;
    private volatile Object result = stubResult;

    private static final Object stubResult = new Object();
    private static final AtomicReferenceFieldUpdater<LockFreeLazy, Object> resultUpdater =
            AtomicReferenceFieldUpdater.newUpdater(LockFreeLazy.class, Object.class, "result");

    public LockFreeLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        Supplier<T> currentSupplier = supplier;
        if (currentSupplier == null) {
            return (T) result;
        }
        T currentResult = currentSupplier.get();
        resultUpdater.compareAndSet(this, stubResult, currentResult);
        supplier = null;
        return (T) result;
    }
}
