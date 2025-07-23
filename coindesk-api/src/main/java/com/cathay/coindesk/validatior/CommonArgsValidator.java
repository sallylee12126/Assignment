package com.cathay.coindesk.validatior;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class CommonArgsValidator {

    public static void notNull(Object obj) {
        Optional.ofNullable(obj).orElseThrow(() -> new IllegalArgumentException("argument couldn't be null"));
    }

    public static void notNull(Object obj, String arg) {
        Optional.ofNullable(obj).orElseThrow(() -> new IllegalArgumentException(arg.concat(" couldn't be null")));
    }

    public static void notBlank(String obj) {
        if(StringUtils.isNoneBlank(obj)) {
            Optional.ofNullable(obj).orElseThrow(() -> new IllegalArgumentException("argument couldn't be null"));
        }
    }

    public static void notBlank(Collection<?> obj) {
        notNull(obj);

        if (obj.isEmpty()) {
            throw new IllegalArgumentException("collection couldn't be empty");
        }
    }

    public static void notBlank(String obj, String arg) {
        if(StringUtils.isNoneBlank(obj)) {
            Optional.ofNullable(obj).orElseThrow(() -> new IllegalArgumentException(arg.concat(" couldn't be null")));
        }
    }
}
