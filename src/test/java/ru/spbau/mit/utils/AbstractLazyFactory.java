package ru.spbau.mit.utils;

import ru.spbau.mit.Lazy;

import java.util.function.Supplier;

public interface AbstractLazyFactory {
    public <T> Lazy<T> createLazy(Supplier<T> supplier);
}
