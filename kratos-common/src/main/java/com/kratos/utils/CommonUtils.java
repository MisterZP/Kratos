/*
 * Pprun's Public Domain.
 */
package com.kratos.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CommonUtils {
    public static final String UTF8 = "UTF-8";
    public static final String GB18030 = "GB18030";
    public static final String GBK = "gbk";
    public static final String TIME_ZONE_UTC = "UTC";
    public static final String OUTKEY_CONNECTOR="-";
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    public static BigDecimal round(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public static String join(Collection<String> collectionOfStrings, String delimeter) {
    	if(null == collectionOfStrings || collectionOfStrings.isEmpty()) return null;
        StringBuilder result = new StringBuilder();
        for (String s : collectionOfStrings) {
            result.append(s);
            result.append(delimeter);
        }
        return result.substring(0, result.length() - 1);
    }

    public static String getFileSize(long bytes) {
        DecimalFormat df = new DecimalFormat("###.00");
        BigDecimal fileSize = new BigDecimal(bytes);
        BigDecimal molecule = new BigDecimal(1024);
        if (fileSize.compareTo(new BigDecimal(1024)) >=0
                && fileSize.compareTo(new BigDecimal(1024).multiply(molecule)) < 0) {
            return df.format(fileSize.divide(molecule)) + "KB";
        } else if (fileSize.compareTo(new BigDecimal(1024).multiply(molecule)) >= 0
                && fileSize.compareTo(new BigDecimal(1024).multiply(molecule).multiply(molecule)) < 0) {
            return df.format(fileSize.divide(molecule).divide(molecule)) + "MB";
        } else if (fileSize.compareTo(new BigDecimal(1024).multiply(molecule).multiply(molecule)) >= 0
                && fileSize.compareTo(new BigDecimal(1024).multiply(molecule).multiply(molecule).multiply(molecule)) < 0) {
            return  df.format(fileSize.divide(molecule).divide(molecule).divide(molecule)) + "GB";
        } else if (fileSize.compareTo(new BigDecimal(1024).multiply(molecule).multiply(molecule).multiply(molecule)) >= 0) {
            return df.format(fileSize.divide(molecule).divide(molecule).divide(molecule).divide(molecule)) + "TB";
        } else {
            return bytes + "Byte";
        }
    }

    public static Long getFileSize(String sizeStr) {
        Long size = 0l;
        try {
            if (StringUtils.isBlank(sizeStr)) {
                size = 0l;
            } else if (sizeStr.contains("KB")) {
                size = Math.round(Double.valueOf(sizeStr.replace("KB", "")) * 1024);
            } else if (sizeStr.contains("MB")) {
                size = Math.round(Double.valueOf(sizeStr.replace("MB", "")) * 1024 * 1024);
            } else if (sizeStr.contains("GB")) {
                size = Math.round(Double.valueOf(sizeStr.replace("GB", "")) * 1024 * 1024 * 1024);
            } else if (sizeStr.contains("Byte")) {
                size = Math.round(Double.valueOf(sizeStr.replace("Byte", "")));
            }
        } catch (Exception e) {
        }
        return size;
    }

    public static String buildFullDirPath(String path, String... dirs) {
        String p = path;
        if (StringUtils.endsWithIgnoreCase(p, "/") && StringUtils.endsWithIgnoreCase(p, "\\\\")) {
            p += File.separator;
        }
        for (String dir : dirs) {
            if (StringUtils.isNotBlank(dir)) {
                p += dir + File.separator;
            }
        }
        return p;
    }

    public static String buildHttpPath(String host, String... dirs) {
        String p = host;
        if (StringUtils.endsWithIgnoreCase(p, "/") && StringUtils.endsWithIgnoreCase(p, "\\\\")) {
            p += "/";
        }
        for (String dir : dirs) {
            if (StringUtils.isNotBlank(dir)) {
                p += dir + "/";
            }
        }
        return p;
    }

	public static boolean contains4Array(Object[] subStatusArray, Object status) {
		boolean result = false;
		if(null != subStatusArray && subStatusArray.length > 0 && null != status){
			for (Object object : subStatusArray) {
				if(null != object && object.equals(status)){
					return true;
				}
			}
		}
		return result;
	}

    public static String encode(String text) {
        try {
            return URLEncoder.encode(text, Constants.COMMON_CHARSET);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("never unSupport!");
            return text;
        }
    }

    public static String decode(String text) {
        try {
            return URLDecoder.decode(text, Constants.COMMON_CHARSET);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("never unSupport!");
            return text;
        }
    }

    public static List<String> splitToArray(String text, String separator) {
        String[] split = text.split(separator);
        return Arrays.asList(split);
    }


    public static String createSKU() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


    public static void close(Closeable... closeables) {
        for(Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error("close io error", e);
            }
        }
    }
}
