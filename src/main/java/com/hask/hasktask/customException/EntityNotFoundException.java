package com.hask.hasktask.customException;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, String... searchParamsMap) {
        super(EntityNotFoundException.generateMessage(
                clazz.getSimpleName(),
                // toMap(String.class, String.class, searchParamsMap)
                toMap(searchParamsMap)
        ));
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
                " was not found for parameters:-> " +
                searchParams;
    }
    private static <E> Map<String, String> toMap(E[] entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");

        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put((String) entries[i], (String) entries[i + 1]),
                        Map::putAll);
    }
    /* * private static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");

        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }*/


}
/*
*
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, String... searchParamsMap) {
        super(EntityNotFoundException.generateMessage(
                clazz.getSimpleName(),
                toMap(searchParamsMap)
        ));
    }

    private static String generateMessage(String model, Map<String, String> searchParams) {
        return StringUtils.capitalize(model) +
                " was not found for parameters:-> " +
                searchParams;
    }

    // private static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {

    private static <E> Map<String, String> toMap(E[] entries) {
        if (entries.length % 2 == 1)
        throw new IllegalArgumentException("Invalid entries");

        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
        .collect(HashMap::new,
        (m, i) -> m.put((String) entries[i], (String) entries[i + 1]),
        Map::putAll);
        }

        }

        */
