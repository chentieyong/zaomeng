package com.kingpivot.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/11/3.
 */
public class NumberUtils {

    /**
     * 格式化为指定位小数的数字,返回未使用科学计数法表示的具有指定位数的字符串。
     * 该方法舍入模式：向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。
     * <pre>
     *  "3.1415926", 1          --> 3.1
     *  "3.1415926", 3          --> 3.142
     *  "3.1415926", 4          --> 3.1416
     *  "3.1415926", 6          --> 3.141593
     *  "1234567891234567.1415926", 3   --> 1234567891234567.142
     * </pre>
     *
     * @param number    String类型的数字对象
     * @param precision 小数精确度总位数,如2表示两位小数
     * @return 返回数字格式化后的字符串表示形式(注意返回的字符串未使用科学计数法)
     */
    public static String keepPrecision(String number, int precision) {
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 格式化为指定位小数的数字,返回未使用科学计数法表示的具有指定位数的字符串。<br>
     * 该方法舍入模式：向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。<br>
     * 如果给定的数字没有小数，则转换之后将以0填充；例如：int 123  1 --> 123.0<br>
     * <b>注意：</b>如果精度要求比较精确请使用 keepPrecision(String number, int precision)方法
     *
     * @param number    String类型的数字对象
     * @param precision 小数精确度总位数,如2表示两位小数
     * @return 返回数字格式化后的字符串表示形式(注意返回的字符串未使用科学计数法)
     */
    public static String keepPrecision(Number number, int precision) {
        return keepPrecision(String.valueOf(number), precision);
    }

    /**
     * 对double类型的数值保留指定位数的小数。<br>
     * 该方法舍入模式：向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。<br>
     * <b>注意：</b>如果精度要求比较精确请使用 keepPrecision(String number, int precision)方法
     *
     * @param number    要保留小数的数字
     * @param precision 小数位数
     * @return double 如果数值较大，则使用科学计数法表示
     */
    public static double keepPrecision(double number, int precision) {
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double keepPrecisionNew(Double number, int precision) {
        if (null == number) {
            return 0.0d;
        }
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 对float类型的数值保留指定位数的小数。<br>
     * 该方法舍入模式：向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。<br>
     * <b>注意：</b>如果精度要求比较精确请使用 keepPrecision(String number, int precision)方法
     *
     * @param number    要保留小数的数字
     * @param precision 小数位数
     * @return float 如果数值较大，则使用科学计数法表示
     */
    public static float keepPrecision(float number, int precision) {
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).floatValue();
    }


    /**
     * 对double类型的数值保留指定位数的小数。<br>
     * 该方法舍入模式：向下舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。<br>
     * <b>注意：</b>如果精度要求比较精确请使用 keepPrecision(String number, int precision)方法
     *
     * @param number    要保留小数的数字
     * @param precision 小数位数
     * @return double 如果数值较大，则使用科学计数法表示
     */
    public static double keepPrecisionDown(double number, int precision) {
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 对金额格式化显示。<br>
     *
     * @param money     要格式化的金额
     * @param precision 小数位数
     */
    public static String formatMoney(Double money, int precision) {
        if (null == money || money <= 0) {
            return "0.00";
        }

        DecimalFormat format = new DecimalFormat();
        StringBuffer stringBuffer = new StringBuffer("##,###");
        if (precision > 0) {
            stringBuffer.append(".");
            for (int i = 0; i < precision; i++) {
                stringBuffer.append("0");
            }
        }
        format.applyPattern(stringBuffer.toString());
        return format.format(money);
    }

    /**
     * 检验是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String strFormat3(String s1) {
        StringBuilder st = new StringBuilder("0");
        for (Integer i = 0; i < (4 - s1.length()); i++) {
            st.append("0");
        }
        st.append(s1);
        return st.toString();
    }
}
