package com.kingpivot.common.gaodeUtil;

import com.kingpivot.api.dto.weixin.HttpUtil;
import com.kingpivot.base.config.Config;
import com.kingpivot.common.gaodeUtil.gaodeDirection.ApiGaodeDirectionStatusDto;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.common.utils.NumberUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GaodeUtil {
    private static final String GAODE_DIRECTIONLINE_URL = "https://restapi.amap.com/v3/direction/driving";

    /**
     * 根据地址获取坐标
     *
     * @param address  地址
     * @param cityName 城市名
     * @return
     */
    public static String[] getLocations(String address, String cityName) {
        String[] locations = null;
        String url = String.format("https://restapi.amap.com/v3/place/text" +
                        "?keywords=%s&city=%s&output=json&offset=1&page=1&key=%s&extensions=base", address,
                cityName, Config.GAODE_WEBAPI_KEY);
        String result = null;
        try {
            result = HttpUtil.doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = JSONObject.fromObject(result);
        System.out.println(obj.get("status"));
        String status = (String) obj.get("status");
        if (status.equals("1")) {
            JSONArray poisList = (JSONArray) obj.get("pois");
            if (poisList != null && poisList.size() != 0) {
                JSONObject poidDetail = (JSONObject) poisList.get(0);
                String location = (String) poidDetail.get("location");
                locations = location.split(",");
            }
        }
        return locations;
    }

    /**
     * 计算环路路径
     *
     * @param lon1      起点经度
     * @param lat1      起点纬度
     * @param lon2      终点经度
     * @param lat2      终点纬度
     * @param waypoints 途经点
     * @return
     */
    public static Double getDrivingRoute(Double lon1, Double lat1, Double lon2, Double lat2, String waypoints) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", Config.GAODE_WEBAPI_KEY);
        map.put("output", "JSON");
        map.put("origin", String.format("%s,%s", lon1, lat1));
        map.put("destination", String.format("%s,%s", lon2, lat2));
        if (StringUtils.isNotBlank(waypoints)) {
            map.put("waypoints", waypoints);
        }
        String result = null;
        try {
            result = HttpUtil.doGet(GAODE_DIRECTIONLINE_URL, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ApiGaodeDirectionStatusDto apiGaodeDirectionStatusDto = JacksonHelper.fromJson(result, ApiGaodeDirectionStatusDto.class);
        if (apiGaodeDirectionStatusDto.getInfo().equals("OK")
                && apiGaodeDirectionStatusDto.getRoute() != null
                && apiGaodeDirectionStatusDto.getRoute().getPaths() != null
                && apiGaodeDirectionStatusDto.getRoute().getPaths().size() != 0
                && apiGaodeDirectionStatusDto.getRoute().getPaths().get(0).getDistance() != null) {
            return NumberUtils.keepPrecision(Double.parseDouble(apiGaodeDirectionStatusDto.getRoute().getPaths().get(0).getDistance()) / 1000, 2);
        }
        return 0d;
    }
}
