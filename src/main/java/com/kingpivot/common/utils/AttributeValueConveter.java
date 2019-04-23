package com.kingpivot.common.utils;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AttributeValueConveter implements Converter {
    private static Map<Class, Boolean> canConvertClasses = new HashMap<Class, Boolean>();
    private static Map<Class, String> classValueField = new HashMap<Class, String>();

    private Mapper mapper;

    public AttributeValueConveter(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Class clazz = source.getClass();
        String fieldName = "";
        if (classValueField.containsKey(clazz)) {
            fieldName = classValueField.get(clazz);

        } else {
            throw new ObjectAccessException("不能给类 " + clazz.getName() + "赋值应为不存在唯一的XStreamAttributeValue注解");
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean isValueField = false;
            if (field.getName().equals(fieldName)) {
                isValueField = true;
            }
            try {

                String currentFieldName = field.getName();
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object value = field.get(source);
                if (null == value) {
                    continue;
                }
                //内容
                if (isValueField) {
                    writer.setValue(value.toString());
                }
                //属性
                else {
                    if (Number.class.isAssignableFrom(value.getClass())
                            || value.getClass().isPrimitive()) {
                        writer.addAttribute(mapper.serializedMember(source.getClass()
                                , currentFieldName)
                                , String.valueOf(value));
                    } else {
                        writer.addAttribute(mapper.serializedMember(source.getClass()
                                , currentFieldName)
                                , value.toString());
                    }
                }
            } catch (Exception ex) {
                throw new ObjectAccessException("不能设置字段 " + clazz.getName() + "." + field.getName(), ex);
            }
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Object obj = context.currentObject();
        if (obj == null) {
            try {
                obj = context.getRequiredType().newInstance();
            } catch (Exception e) {
                throw new ObjectAccessException("反序列化失败，不能构造实体对象 " + context.getRequiredType().getName(), e);
            }
        }
        String fieldName = "";
        if (classValueField.containsKey(context.getRequiredType())) {
            fieldName = classValueField.get(context.getRequiredType());

        } else {
            throw new ObjectAccessException("获取内容失败" + context.getRequiredType().getName() + "，实体类不存在唯一的XStreamAttributeValue注解");
        }
        Iterator attNames = reader.getAttributeNames();
        while (attNames.hasNext()) {
            String attName = (String) attNames.next();
            if (attName.equals(fieldName)) {
                continue;
            }

            try {
                Field field = obj.getClass().getDeclaredField(mapper.realMember(obj.getClass(), attName));
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                String v = reader.getAttribute(attName);
                if (null == v || "".equals(v)) {
                    continue;
                }
                Class fieldType = field.getType();
                Constructor strnum = fieldType.getDeclaredConstructor(String.class);
                field.set(obj, strnum.newInstance(v));
            } catch (Exception e) {
                e.printStackTrace();
                throw new ObjectAccessException("Cannot construct " + obj.getClass(), e);
            }
        }

        try {
            String value = reader.getValue();
            Field field = obj.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (null == value || "".equals(value)) {
            } else {
                Class fieldType = field.getType();
                Constructor strnum = fieldType.getDeclaredConstructor(String.class);
                field.set(obj, strnum.newInstance(value));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ObjectAccessException("不能写入节点内容 " + obj.getClass(), ex);
        }
        return obj;
    }

    @Override
    public synchronized boolean canConvert(Class type) {
        if (canConvertClasses.containsKey(type)) {
            return canConvertClasses.get(type);
        }
        Field[] fields = type.getDeclaredFields();
        int attributeCount = 0;
        String filedName = "";
        for (Field field : fields) {
            XStreamAttributeValue attribute = field.getAnnotation(XStreamAttributeValue.class);
            if (attribute != null
                    && (field.getType().isAssignableFrom(String.class)
                    || Number.class.isAssignableFrom(field.getType())
                    || field.getType().isPrimitive())) {
                attributeCount++;
                filedName = field.getName();
            }
        }
        if (1 == attributeCount) {
            canConvertClasses.put(type, true);
            classValueField.put(type, filedName);
            return true;
        } else {

            canConvertClasses.put(type, false);
            return false;
        }
    }
}