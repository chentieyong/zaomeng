package com.kingpivot.common.utils;

import org.apache.commons.lang3.StringUtils;

public class DistanceUtil {
    public static Integer getDistance(String x1,String y1,String x2,String y2) {
        if (StringUtils.isBlank(x1)|| StringUtils.isBlank(y1)|| StringUtils.isBlank(x2)|| StringUtils.isBlank(y2)){
            return null;
        }
        Double mapX1=Double.parseDouble(x1);
        Double mapX2=Double.parseDouble(x2);
        Double mapY1=Double.parseDouble(y1);
        Double mapY2=Double.parseDouble(y2);
        int distance = (int) Math.sqrt(Math.abs((mapX1 - mapX2) * (mapX1 - mapX2))
                + Math.abs((mapY1 - mapY2) * (mapY1 - mapY2)));
        return distance<0?-distance:distance;
    }
}
