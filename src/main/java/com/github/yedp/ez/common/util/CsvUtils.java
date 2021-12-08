package com.github.yedp.ez.common.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtils {
    /**
     * 读取csv文件
     *
     * @param file csv文件
     * @return 数据map列表
     * @throws IOException io异常
     */
    public static List<Map<String, String>> read(File file) throws IOException {
        Reader in = new FileReader(file);
        CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        if (csvParser == null) {
            return new ArrayList<>();
        }
        List<String> headers = csvParser.getHeaderNames();
        List<CSVRecord> csvRecords = csvParser.getRecords();
        if (csvRecords == null) {
            return new ArrayList<>();
        }
        List<Map<String, String>> mapList = new ArrayList<>(csvRecords.size());
        for (CSVRecord record : csvRecords) {
            Map<String, String> map = new HashMap<>(headers.size());
            for (String header : headers) {
                map.put(header, record.get(header));
            }
            mapList.add(map);
        }
        csvParser.close();
        in.close();
        return mapList;
    }

    /**
     * 读取Csv到对象列表
     *
     * @param file  文件
     * @param clazz 类
     * @param <T>   类类型
     * @return 对象列表
     * @throws IOException io异常
     */
    public static <T> List<T> read(File file, Class<T> clazz) throws IOException {
        Reader in = new FileReader(file);
        CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        if (csvParser == null) {
            return new ArrayList<>();
        }
        List<String> headers = csvParser.getHeaderNames();
        List<CSVRecord> csvRecords = csvParser.getRecords();
        if (csvRecords == null) {
            return new ArrayList<>();
        }
        List<T> tList = new ArrayList<>(csvRecords.size());
        for (CSVRecord record : csvRecords) {
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            for (String header : headers) {
                String propertyName = StringUtils.lineToHump(header);    // 属性名,驼峰
                Object value = record.get(header);        // 属性值
                String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                Field field = ClassUtil.getClassField(clazz, propertyName);    //获取和map的key匹配的属性名称
                if (field == null) {
                    continue;
                }
                Class<?> fieldTypeClass = field.getType();
                value = ClassUtil.convertValType(value, fieldTypeClass);
                try {
                    clazz.getMethod(setMethodName, field.getType()).invoke(t, value);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    System.out.println(value);
                }
            }
            tList.add(t);
        }
        csvParser.close();
        in.close();
        return tList;
    }


}
