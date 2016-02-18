package net.yeputons.spbau.java.utils;

import net.yeputons.spbau.java.Lazy;

import java.util.function.Supplier;

public interface AbstractLazyFactory {
    public <T> Lazy<T> createLazy(Supplier<T> supplier);
}
