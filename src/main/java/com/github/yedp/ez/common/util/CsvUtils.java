package com.github.yedp.ez.common.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CsvUtils {
    private final static Logger log = LoggerFactory.getLogger(CsvUtils.class);

    /**
     * 读取csv文件
     *
     * @param file csv文件
     * @return 数据map列表
     * @throws IOException io异常
     */
    public static List<Map<String, String>> read(File file) throws IOException {
        if (file == null) {
            log.error("file is null");
            return new ArrayList<>();
        }
        Reader in = new FileReader(file);
        try {
            CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            if (csvParser == null) {
                log.error("csvParser is null");
                return new ArrayList<>();
            }
            List<String> headers = csvParser.getHeaderNames();
            List<CSVRecord> csvRecords = csvParser.getRecords();
            if (csvRecords == null) {
                log.info("csvRecords is null");
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
            return mapList;
        } finally {
            in.close();
        }
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
        if (file == null) {
            log.error("file is null");
            return new ArrayList<>();
        }
        Reader in = new FileReader(file);
        try {
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
                try {
                    T t = clazz.newInstance();
                    for (String header : headers) {
                        Object value = record.get(header);        // 属性值
                        ClassUtil.invoke(clazz, t, header, value);
                    }
                    tList.add(t);
                } catch (Exception e) {
                    log.error("read exception first column:{}; record:{}; {}", record.get(0), JsonUtil.toJsonString(record), e);
                }
            }
            csvParser.close();
            return tList;
        } finally {
            in.close();
        }
    }

//
//    public static void exportByList(String[] headers, List<List<String>> dataList) throws FileNotFoundException {
//        FileOutputStream fileos = new FileOutputStream("E:/abc.csv");
//        exportByList(headers, dataList, fileos);
//    }

    public static void exportByList(String[] headers, List<List<String>> dataList, OutputStream os) {
        OutputStreamWriter osw = null;
        CSVFormat csvFormat = null;
        CSVPrinter csvPrinter = null;
        try {
            osw = new OutputStreamWriter(os, "GBK");//如果是UTF-8时，WPS打开是正常显示，而微软的excel打开是乱码,
            csvFormat = CSVFormat.DEFAULT.withHeader(headers);
            csvPrinter = new CSVPrinter(osw, csvFormat);
            for (int i = 0; i < dataList.size(); i++) {
                List<String> values = dataList.get(i);
                csvPrinter.printRecord(values);
            }
        } catch (Exception e) {
        } finally {
            try {
                osw.close();
            } catch (Exception e) {
                log.error("osw close exception:{}", e);
            }
            try {
                csvPrinter.close();
            } catch (IOException e) {
                log.error("csvPrinter close exception:{}", e);
            }
            try {
                os.close();
            } catch (IOException e) {
                log.error("os close exception:{}", e);
            }
        }
    }


}
