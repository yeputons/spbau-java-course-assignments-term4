package net.yeputons.spbau.java;

import java.util.function.Supplier;

class SingletonLazy<T> implements Lazy<T> {
    // Volatile here is optional - if it's not here, we may skip change of `supplier` to `null`
    // and we will just spend extra time acquiring monitor
    private Supplier<T> supplier;

    // No need in volatile `result` because we either:
    // 1. Read it in the first `if`, which should happen after `supplier=null`, which happens after write to `result`
    // 2. Return it in the end of get(), which happens after write to `result`
    private T result;

    public SingletonLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (supplier == null) {
            return result;
        }
        synchronized (this) {
            if (supplier != null) {
                result = supplier.get();
                supplier = null;
            }
        }
        return result;
    }
}
