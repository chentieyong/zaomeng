package com.kingpivot.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 15/11/12.
 */
public class MyMatcher {

    public static List<String> matcher(String input) {
        List<String> list = new ArrayList<>();
        //创建匹配的模式
        Pattern pattern = Pattern.compile("(.)\\1*");
        //匹配器
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            //返回匹配的子序列，并加入到list里面。
            list.add(matcher.group());
        }
        Collections.sort(list, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });
        return list;
    }
}
