package com.kingpivot.common.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtil {

    private static Pattern emailPattern = null;
    private static Pattern mobilePattern = null;

    static {
        String regEx = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
        emailPattern = Pattern.compile(regEx);

        regEx = "^1\\d{10}$";
        mobilePattern = Pattern.compile(regEx);
    }

    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查对象是否为数字型字符串,包含负数开头的。
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        char[] chars = obj.toString().toCharArray();
        int length = chars.length;
        if (length < 1)
            return false;

        int i = 0;
        if (length > 1 && chars[0] == '-')
            i = 1;

        for (; i < length; i++) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }


    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areEqual(String value1, String value2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 != null) {
            return value1.equals(value2);
        }
        if (value2 != null) {
            return value2.equals(value1);
        }
        return true;
    }

    /**
     * 将货币字符串转化为数字字符串
     * @param Currency
     * @return
     */
    public static String parseCurrency(String Currency){
        String realStr = "";
        if(Character.isDigit(Currency.charAt(0))){
            realStr = Currency;
        }else{
            realStr = Currency.substring(1);
        }
        String subStr[] = realStr.split(",");
        String finalStr = "";
        for(String str : subStr){
            finalStr += str;
        }
        return finalStr;
    }

    public static String nullDefaultValue(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static String nullDefaultValue(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static Boolean isEmail(String value) {
        if (isEmpty(value)) {
            return false;
        }
        Matcher matcher = emailPattern.matcher(value);
        return matcher.find();
    }

    public static Boolean isMobile(String value) {
        if (isEmpty(value)) {
            return false;
        }
        Matcher matcher = mobilePattern.matcher(value);
        return matcher.find();
    }

    public static String join(List<String> list, String split) {
        if (split == null) {
            split = "";
        }
        StringBuilder values = new StringBuilder();
        if (list != null && (!list.isEmpty())) {
            String nickFormater = "%s";
            for (String item : list) {
                if (!StringUtil.isEmpty(item)) {
                    values.append(String.format(nickFormater, item.trim()));
                    nickFormater = split+"%s";
                }
            }
        }
        return values.toString();
    }
}
