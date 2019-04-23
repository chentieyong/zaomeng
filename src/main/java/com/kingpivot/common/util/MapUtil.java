package com.kingpivot.common.util;

import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MapUtil {
    public static <T> T toObject(Map<String, String> param, Class<T> clazz) {
        T requestBase = null;
        try {
            requestBase = JsonUtil.readObject(
                    JsonUtil.writeValueAsString(param),
                    clazz);
        } catch (Exception ex) {
        }
        return requestBase;
    }

    public static <T> T toObject(Map<String, String> param, Class<T> clazz, Boolean keyToLower) {
        Map<String, String> paramNew = new HashMap<String, String>();
        if (keyToLower) {
            for (Map.Entry<String, String> item : param.entrySet()) {
                paramNew.put(item.getKey().toLowerCase(), item.getValue());
            }
        }
        return toObject(paramNew, clazz);
    }

    public static <T> T convertBean(Class<T> clazz, String json) {
        T requestBase = JsonUtil.readObject(json, clazz);
        return requestBase;
    }

    //json2map
    public static Map<String, Object> parseJSON2Map(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            //如果内层还是数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    public static Map<String, Object> parseJSON2MapFL(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            //如果内层还是数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(captureNameLow(k.toString()), list);
            } else {
                map.put(captureNameLow(k.toString()), v);
            }
        }
        return map;
    }



    public static <T> T convertMap(Class<T> clazz, Map<String, Object> map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz); // 获取类属性
        T obj = clazz.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    public static String mapToJsonStr(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder("{");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String[] strs = (String[]) entry.getValue();
            if (null == strs || strs.length == 0) {
                continue;
            }
            stringBuilder.append("\'" + entry.getKey() + "\':\'" + strs[0] + "\',");
        }
        String jsonStr = stringBuilder.toString();
        if (jsonStr.endsWith(",")) {
            jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf(","));
        }
        jsonStr += "}";
        return jsonStr;
    }

    public static Map<String, Object> getParamMap(HttpServletRequest request) {
        Map paramMap = request.getParameterMap();
        String mapJsonStr = MapUtil.mapToJsonStr(paramMap);
        JSONObject jsonObject = JSONObject.fromObject(mapJsonStr);
        return MapUtil.parseJSON2Map(jsonObject);

    }

    public static Map<String, Object> getParamMapRegix(HttpServletRequest request) {
        Map paramMap = request.getParameterMap();
        String mapJsonStr = MapUtil.mapToJsonStr(paramMap);
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(mapJsonStr);
        mapJsonStr = m.replaceAll("");
        JSONObject jsonObject = JSONObject.fromObject(mapJsonStr);
        return MapUtil.parseJSON2Map(jsonObject);

    }

    public static Map<String, String> getParamMapS(HttpServletRequest request) {
        Map paramMap = request.getParameterMap();
        String mapJsonStr = MapUtil.mapToJsonStr(paramMap);
        JSONObject jsonObject = JSONObject.fromObject(mapJsonStr);
        Map<String,Object> map = MapUtil.parseJSON2Map(jsonObject);
        Map<String,String> backMap = null;
        if(null != map){
            backMap = Maps.newConcurrentMap();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                backMap.put(entry.getKey(),(String)entry.getValue());
            }
        }
        return backMap;
    }
    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map<String,Object> convertBean(Object bean) {
        Class type = bean.getClass();
        Map<String,Object> returnMap = new HashMap();
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
        }catch (Exception e){

        }
        return returnMap;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map(KEY 第一个字母大写)
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map<String,Object> convertBeanFUP(Object bean) {
        Class type = bean.getClass();
        Map<String,Object> returnMap = new HashMap();
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    propertyName = captureName(propertyName);
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
        }catch (Exception e){

        }
        return returnMap;
    }

    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);

    }

    public static String captureNameLow(String name) {
        char[] cs = name.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);

    }

    //将javabean转为map类型，然后返回一个map类型的值
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!StringUtils.equals(name, "class")) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    //将javabean转为map类型，然后返回一个map类型的值
    public static Map<String, String> beanToMapStr(Object obj) {
        Map<String, String> params = new HashMap<>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!StringUtils.equals(name, "class")) {
                    params.put(name, (String)propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public static Map<String,Object> mapToObjValue(Map<String,String> map){
        if(null == map || map.isEmpty()){
            return null;
        }
        Map<String,Object> param = Maps.newConcurrentMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            param.put(entry.getKey(),entry.getValue());
        }
        return param;
    }
}
