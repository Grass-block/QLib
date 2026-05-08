package org.atcraftmc.qlib.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ConfigurationValue extends Number {
    public boolean empty;
    public Object value;

    public boolean isEmpty() {
        return empty;
    }

    public boolean isType(Class<?> type) {
        return type.isInstance(this.value);
    }

    public ConfigurationValue updateValue(Object value) {
        this.value = value;
        this.empty = value == null;
        return this;
    }

    //other types
    public <I> I value(Class<I> type, I fallback) {
        if (isEmpty()) {
            return fallback;
        }

        return type.cast(this.value);
    }

    public <I> I value(Class<I> type) {
        return value(type, null);
    }


    public boolean bool(boolean fallback) {
        return value(Boolean.class, fallback);
    }

    public boolean bool() {
        return bool(false);
    }

    public String string(String fallback) {
        return value(String.class, fallback);
    }

    public String string() {
        return value(String.class, "");
    }

    public <I extends Enum<I>> Enum<I> getEnum(Class<I> type, I fallback) {
        for (var i = 0; i < type.getEnumConstants().length; i++) {
            if (type.getEnumConstants()[i].name().equals(string())) {
                return type.getEnumConstants()[i];
            }
        }

        return null;
    }

    public Pattern getRegex() {
        return Pattern.compile(string());
    }

    public ConfigurationSection section() {
        return value(ConfigurationSection.class);
    }


    //numbers
    public int intValue() {
        return intValue(0);
    }

    public long longValue() {
        return longValue(0);
    }

    public float floatValue() {
        return floatValue(0);
    }

    public double doubleValue() {
        return doubleValue(0.0);
    }

    public int intValue(int fallback) {
        return value(Number.class, fallback).intValue();
    }

    public long longValue(long fallback) {
        return value(Number.class, fallback).longValue();
    }

    public float floatValue(float fallback) {
        return value(Number.class, fallback).floatValue();
    }

    public double doubleValue(double fallback) {
        return value(Number.class, fallback).doubleValue();
    }


    public <I> List<I> list(Class<I> type) {
        if (isEmpty()) {
            return List.of();
        }

        return (List<I>) this.value;
    }

    public <K, V> Map<K, V> map(Class<K> key, Class<V> value) {
        if (isEmpty()) {
            return Map.of();
        }

        return (Map<K, V>) this.value;
    }
}
