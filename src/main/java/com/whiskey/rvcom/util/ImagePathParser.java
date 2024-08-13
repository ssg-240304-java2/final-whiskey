package com.whiskey.rvcom.util;

public class ImagePathParser {
    private static final String IMAGEURL_PREFIX = "https://kr.object.ncloudstorage.com/whiskey-file/";
    public static String parse(String fileName) {
        return IMAGEURL_PREFIX + fileName;
    }
}
