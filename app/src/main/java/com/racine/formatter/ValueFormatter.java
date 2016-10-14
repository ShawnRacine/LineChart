package com.racine.formatter;

/**
 * Created by sunrx on 2016/10/14.
 */
public interface ValueFormatter<T> {
    String getFormattedValue(T value);
}
