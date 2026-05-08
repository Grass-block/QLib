package org.atcraftmc.qlib.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class Formatter {
    public static final ThreadLocal<DecimalFormat> DF5 = ThreadLocal.withInitial(() -> new DecimalFormat("0.00000"));
    public static final ThreadLocal<DecimalFormat> DF3 = ThreadLocal.withInitial(() -> new DecimalFormat("0.000"));
    public static final Map<Class<?>, Function<?, String>> MAPPERS = new HashMap<>();
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        register(Float.class, (v) -> float3(v));
        register(Double.class, Formatter::float5);
        register(Date.class, Formatter::date);
    }

    public static String date(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String float5(double v) {
        return DF5.get().format(v);
    }

    public static String float3(double v) {
        return DF3.get().format(v);
    }

    public static <T> void register(Class<T> clazz, Function<T, String> mapper) {
        MAPPERS.put(clazz, mapper);
    }

    public static <T> String format(T object) {
        if (object == null) {
            return "[null]";
        }

        Function<T, String> h = (Function<T, String>) MAPPERS.get(object.getClass());

        if (h == null) {
            return object.toString();
        }

        return h.apply(object);
    }
}
