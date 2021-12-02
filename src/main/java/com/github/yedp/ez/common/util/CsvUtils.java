package com.github.yedp.ez.common.util;

import com.github.yedp.ez.common.annotations.CsvField;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtils {
    /**
     * 读取csv文件  (一次性读取文件不宜过大)
     *
     * @param filePath 文件路径
     * @param headers  csv列头
     * @param tClass   返回对象的类型
     * @param <T>      对象
     * @return CSVRecord 列表
     **/
    public static <T> List<T> readCSV(String filePath, String[] headers, Class<T> tClass) {
        //创建CSVFormat
        CSVFormat format = CSVFormat.Builder.create().build().builder().setHeader(headers).build();

        // 获取对象的PropertyDescriptor
        List<T> tList = new ArrayList<>();
        try {
            Map<String, PropertyDescriptor> descriptorMap = CsvUtils.getCsvFieldMapPropertyDescriptor(tClass);
            FileReader fileReader = new FileReader(filePath);
            //创建CSVParser对象
            CSVParser parser = new CSVParser(fileReader, format);
            try {
                Map<String, Integer> map = parser.getHeaderMap();
                for (CSVRecord record : parser) {
                    T t = tClass.newInstance();
                    for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()) {
                        if (descriptorMap.containsKey(stringIntegerEntry.getKey()) && record.size() > stringIntegerEntry.getValue()) {
                            descriptorMap.get(stringIntegerEntry.getKey()).getWriteMethod().invoke(t, record.get(stringIntegerEntry.getValue()));
                        }
                    }
                    tList.add(t);
                }
            } finally {
                parser.close();
                fileReader.close();
            }
        } catch (Exception e) {

        }
        return tList;
    }


    /**
     * 获取对应对象中包含CsvCsvField字段的 PropertyDescriptor
     *
     * @param tClass 对象的class
     * @return Map
     * @throws IntrospectionException 异常
     * @throws NoSuchFieldException   异常
     */
    public static Map<String, PropertyDescriptor> getCsvFieldMapPropertyDescriptor(Class tClass) throws IntrospectionException, NoSuchFieldException {
        Map<String, PropertyDescriptor> descriptorMap = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(tClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            // 获取该字段赋值过来的  字段名称
            if (propertyDescriptor.getWriteMethod() == null) {
                continue;
            }
            Field field = tClass.getDeclaredField(propertyDescriptor.getName());
            CsvField csvField = field.getAnnotation(CsvField.class);
            if (csvField == null) {
                continue;
            }
            String fieldMetaName = csvField.name();
            if (StringUtils.isEmpty(fieldMetaName)) {
                continue;
            }
            descriptorMap.put(fieldMetaName, propertyDescriptor);
        }
        return descriptorMap;
    }

}
