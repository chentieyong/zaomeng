package com.kingpivot.common.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.util.Map;
import java.util.WeakHashMap;

public class XStreamUtil {

    private static Map<Class<?>, XStream> xstreamMap = new WeakHashMap<>();

    /**
     * 转换过程中特殊字符转码
     */
    private static NameCoder nameCoder = new NameCoder() {
        public String encodeNode(String arg0) {
            return arg0;
        }

        public String encodeAttribute(String arg0) {
            return arg0;
        }

        public String decodeNode(String arg0) {
            return arg0;
        }

        public String decodeAttribute(String arg0) {
            return arg0;
        }
    };

    /**
     * 在xml中多余的节点生成bean时会抛出异常
     * 通过该mapperWrapper跳过不存在的属性
     *
     * @param mapper
     * @return MapperWrapper [返回类型说明]
     * @throws throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    private static MapperWrapper createSkipOverElementMapperWrapper(Mapper mapper) {
        MapperWrapper resMapper = new MapperWrapper(mapper) {
            @SuppressWarnings("rawtypes")
            @Override
            public Class realClass(String elementName) {
                Class res = null;
                try {
                    res = super.realClass(elementName);
                } catch (CannotResolveClassException e) {
                }
                return res;
            }
        };
        return resMapper;
    }

    /**
     * 获取xstream转换对象
     *
     * @param classType
     * @return XStream [返回类型说明]
     * @throws throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public static XStream getXstream(Class<?> classType) {
        return getXstream(classType, true);
    }

    /**
     * 获取xstream转换对象
     *
     * @param classType
     * @param isSkipOverElement
     * @return XStream [返回类型说明]
     * @throws throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public static XStream getXstream(Class<?> classType, boolean isSkipOverElement) {
        if (xstreamMap.containsKey(classType)) {
            return xstreamMap.get(classType);
        }
        XStream res = null;
        if (isSkipOverElement) {
            res = new XStream(new Xpp3DomDriver(nameCoder)) {
                protected MapperWrapper wrapMapper(MapperWrapper next) {
                    return createSkipOverElementMapperWrapper(next);
                }
            };
        } else {
            res = new XStream(new Xpp3DomDriver(nameCoder));
        }
        res.processAnnotations(classType);
        //增加xstream对既包含属性又包含内容的节点解析
        res.registerConverter(new AttributeValueConveter(res.getMapper()));
        xstreamMap.put(classType, res);

        return res;
    }

    /**
     * 将传入xml字符串转化为java对象
     * @param xmlstr
     * @param cls   xml对应的class类
     * @return T    xml对应的class类的实例对象
     * 调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr, PersonBean.class);
     */
    public static <T> T deSerizalizeFromXml(String xmlstr, Class<T> cls) throws Exception {
        //注意：不是new Xstream();否则报错：java.lang.NoClassDefFoundError:
        //org/xmlpull1/XmlPullParserFactory
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        T obj = (T) xstream.fromXML(xmlstr);
        return obj;
    }
}