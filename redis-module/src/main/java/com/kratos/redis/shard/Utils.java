package com.kratos.redis.shard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author David on 16/4/1.
 */
public class Utils {

    private static final Pattern integerGroupPattern = Pattern.compile("\\d+");

    public static Integer findInt(String text) {
        Matcher matcher = integerGroupPattern.matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return null;
    }
}
