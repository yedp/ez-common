package com.github.yedp.ez.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class PoiExportUtil {
    /**
     * 2003-2007格式
     */
    public static final String FILE_EXTENSION_XLS = "xls";

    /**
     * 2007以上格式
     */
    public static final String FILE_EXTENSION_XLSX = "xlsx";

    /**
     * 表头行样式
     */
    private static CellStyle headStyle;

    /**
     * 页脚样式
     */
    private static CellStyle footStyle;

    /**
     * 页脚样式
     */
    private static CellStyle negFootStyle;

    /**
     * 表头行字体
     */
    private static Font headFont;

    /**
     * 页脚字体
     */
    private static Font footFont;

    /**
     * 页脚字体
     */
    private static Font footFontRed;

    /**
     * 内容行样式
     */
    private static CellStyle contentStyle;

    /**
     * 数字内容字体
     */
    private static CellStyle numberStyle;

    /**
     * 负数-数字内容字体
     */
    private static CellStyle negNumberStyle;

    /**
     * 数据格式
     */
    private static DataFormat dataFormat;

    /**
     * 百分比格式
     */
    private static CellStyle percentStyle;

    /**
     * 内容行字体
     */
    private static Font contentFont;

    /**
     * 内容行字体
     */
    private static Font contentFontRed;

    /**
     * @param maps 表头
     * @param list 数据源
     * @param type 格式类型
     * @param <T>  t
     * @return Excel
     * 生成xls workbook.
     */
    public static <T> Workbook excelExport(Map<String, Object> maps, List<T> list, String type) {
        List<String> dateFields = new ArrayList<String>();
        List<String> numFields = null;
        return excelExport(maps, list, type, dateFields, numFields);
    }

    /**
     * @param maps       字段
     * @param list       数据
     * @param type       xls/xlsx
     * @param <T>        t
     * @param dateFields 日期列
     * @param numFields  汇总列
     * @return workbook
     * 生成xls workbook
     */
    public static <T> Workbook excelExport(Map<String, Object> maps, List<T> list, String type, List<String> dateFields,
                                           List<String> numFields) {
        List<String> percentFields = null;
        return excelExport(maps, list, type, dateFields, numFields, percentFields);
    }

    /**
     * @param maps          字段
     * @param list          数据
     * @param type          xls/xlsx
     * @param <T>           t
     * @param dateFields    日期列
     * @param numFields     汇总列
     * @param percentFields 百分比格式列
     * @return workbook
     * 生成xls workbook
     */
    public static <T> Workbook excelExport(Map<String, Object> maps, List<T> list, String type, List<String> dateFields,
                                           List<String> numFields, List<String> percentFields) {
        try {
            int sheetIndex = 1;
            int startRowIndex = 0;
            Workbook wb = null;
            Set<String> sets = maps.keySet();
            if (FILE_EXTENSION_XLS.equals(type)) {
                wb = new HSSFWorkbook();
            }
            if (FILE_EXTENSION_XLSX.equals(type)) {
                wb = new SXSSFWorkbook();
            }
            CreationHelper createHelper = wb.getCreationHelper();
            initWorkbook(wb);
            Sheet sheet = wb.createSheet("页-" + sheetIndex++);
            List<String> fields = initSheet(maps, wb, createHelper, sets, sheet);
            startRowIndex += 2; // 表头2行
            if (maps.containsKey("_TITLE_")) {
                startRowIndex++; // titile1行
            }
            int j = 0;
            int offsetIndex = 0; // 填充表单内容
            int totalCount = list.size(); // 总条数
            int splitNum = getSplitNum(totalCount);
            float avg = totalCount / 10f; // 分10分
            double percent = 1;
            Map<String, Double> totalMap = new LinkedHashMap<String, Double>();
            Map<String, Double> thisTotalMap = new LinkedHashMap<String, Double>();

            for (; j < list.size(); j++) {
                int finSplitNum = splitNum;
                if (offsetIndex > finSplitNum) { // 每隔10000条换sheet
                    sheet = wb.createSheet("页-" + sheetIndex++);
                    fields = initSheet(maps, wb, createHelper, sets, sheet);
                    offsetIndex = 0;
                    thisTotalMap = new LinkedHashMap<String, Double>();
                } else if (offsetIndex == finSplitNum) {
                    offsetIndex = endLine(startRowIndex, sheet, fields, offsetIndex, totalMap, thisTotalMap);
                    sheet = wb.createSheet("页-" + sheetIndex++);
                    fields = initSheet(maps, wb, createHelper, sets, sheet);
                    offsetIndex = 0;
                    thisTotalMap = new LinkedHashMap<String, Double>();
                }
                T p = list.get(j);
                Class<?> classType = p.getClass();
                int row = startRowIndex + (offsetIndex++);
                Row row1 = sheet.createRow(row);
                Integer cellIndex = 0;
                fillRow(fields, j, totalMap, thisTotalMap, p, classType, row1, cellIndex, dateFields, numFields,
                        percentFields);
                if ((j + 1) >= avg * percent) {
                    System.out.print(new BigDecimal((j / (totalCount + 0.0d)) * 100).setScale(0, BigDecimal.ROUND_HALF_UP)
                            + "% ");
                    percent++;
                }
                if ((j + 1) == totalCount) {
                    offsetIndex = endLine(startRowIndex, sheet, fields, offsetIndex, totalMap, thisTotalMap);
                    System.out.println("100%");
                }
            }
            return wb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一般列填充.
     *
     * @param fields        表头
     * @param j             当前数据索引位置
     * @param totalMap      累计汇总MAP
     * @param thisTotalMap  本页汇总MAP
     * @param p             类型
     * @param classType     类型
     * @param row1          行
     * @param cellIndex     单元格索引
     * @param <T>           <T>
     * @param dateFields    日期列
     * @param numFields     汇总列
     * @param percentFields 百分比列
     */
    private static <T> void fillRow(List<String> fields, int j, Map<String, Double> totalMap,
                                    Map<String, Double> thisTotalMap,
                                    T p, Class<?> classType, Row row1, Integer cellIndex, List<String> dateFields, List<String> numFields,
                                    List<String> percentFields) {
        for (Iterator<String> it = fields.iterator(); it.hasNext(); ) {
            String key = it.next();
            if ("_TITLE_".equals(key) || "_HAS_MERG_".equals(key)) {
                continue;
            }
            Object value = j + 1;
            if (!"_ORDER_".equals(key)) {
                String firstLetter = key.substring(0, 1).toUpperCase();
                try {
                    StringBuilder sb = new StringBuilder();
                    String getMethodName = sb.append("get").append(firstLetter).append(key.substring(1)).toString();
                    Method getMethod = classType.getMethod(getMethodName, new Class[]{});
                    value = getMethod.invoke(p, new Object[]{});
                } catch (Exception ex) {
                    ex.printStackTrace();
                    value = "N/A";
                }
            }
            int index = cellIndex++;

            Cell cell = row1.createCell(index);
            cell.setCellStyle(contentStyle);
            String valueStr = value == null ? "" : value.toString();
            String yyyyMMdd = "yyyy-MM-dd HH:mm:ss";
            if (value instanceof Date) {
                if (dateFields.contains(key)) {
                    yyyyMMdd = "yyyy-MM-dd";
                }
                valueStr = new SimpleDateFormat(yyyyMMdd).format(value);
                cell.setCellValue(valueStr);
            } else if (value instanceof Double || value instanceof Integer || value instanceof BigDecimal) {
                if (percentFields == null || !percentFields.contains(key)) {
                    boolean isneg = false;
                    if (value instanceof Double || value instanceof Integer) {
                        isneg = (Double.parseDouble(value.toString())) < 0;
                    } else {
                        isneg = ((BigDecimal) value).compareTo(BigDecimal.ZERO) < 0;
                    }
                    if (isneg) {
                        cell.setCellStyle(negNumberStyle);
                    } else {
                        cell.setCellStyle(numberStyle);
                    }
                } else {
                    cell.setCellStyle(percentStyle);
                }
                cell.setCellValue(Double.parseDouble(valueStr));
                if (numFields == null || numFields.contains(key)) {
                    if (!totalMap.containsKey(key)) {
                        totalMap.put(key, cell.getNumericCellValue());
                    } else {
                        totalMap.put(key, totalMap.get(key) + cell.getNumericCellValue());
                    }
                    if (!thisTotalMap.containsKey(key)) {
                        thisTotalMap.put(key, cell.getNumericCellValue());
                    } else {
                        thisTotalMap.put(key, thisTotalMap.get(key) + cell.getNumericCellValue());
                    }
                }
            } else {
                cell.setCellValue(valueStr);
            }
        }
    }

    /**
     * 插入页脚.
     *
     * @param startRowIndex 开始行索引
     * @param sheet         sheet
     * @param fields        列数组
     * @param offsetIndex   当前列索引
     * @param totalMap      总汇总MAP
     * @param thisTotalMap  当前页汇总map
     * @return offsetIndex 当前列索引
     */
    private static int endLine(int startRowIndex, Sheet sheet, List<String> fields, int offsetIndex,
                               Map<String, Double> totalMap, Map<String, Double> thisTotalMap) {
        int row2 = startRowIndex + (offsetIndex++);
        Row row3 = sheet.createRow(row2);
        int cellIndex1 = 0;
        for (Iterator<String> it = fields.iterator(); it.hasNext(); ) {
            String key = it.next();
            int index = cellIndex1++;
            Cell cell = row3.createCell(index);
            cell.setCellStyle(footStyle);
            if (thisTotalMap.containsKey(key)) {
                if (Double.parseDouble(thisTotalMap.get(key).toString()) < 0) {
                    cell.setCellStyle(negFootStyle);
                }
                cell.setCellValue(thisTotalMap.get(key));
            } else {
                if (index == 0) {
                    cell.setCellValue("本页汇总：");
                } else {
                    cell.setCellValue("");
                }
            }
        }
        int row = startRowIndex + (offsetIndex++);
        Row row1 = sheet.createRow(row);
        int cellIndex = 0;
        for (Iterator<String> it = fields.iterator(); it.hasNext(); ) {
            String key = it.next();
            int index = cellIndex++;
            Cell cell = row1.createCell(index);
            cell.setCellStyle(footStyle);
            if (totalMap.containsKey(key)) {
                if (Double.parseDouble(thisTotalMap.get(key).toString()) < 0) {
                    cell.setCellStyle(negFootStyle);
                }
                cell.setCellValue(totalMap.get(key));
            } else {
                if (index == 0) {
                    cell.setCellValue("累计汇总：");
                } else {
                    cell.setCellValue("");
                }
            }
        }

        return offsetIndex;
    }

    /**
     * @param maps 表头信息
     * @param list 数据
     * @param file 文件
     * @param <T>  泛型
     * @return 是否成功
     * 导出工具类
     */
    public static <T> boolean excelExport(Map<String, Object> maps, List<T> list, File file) {
        try {
            String filename = file.getName();
            String type = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            Workbook wb = excelExport(maps, list, type);
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
            //            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } catch (OutOfMemoryError e) {
            throw new RuntimeException("内存溢出");
        }
        return true;
    }

    /**
     * 初始化sheet.
     *
     * @param maps         字段
     * @param wb           workbook
     * @param createHelper helper
     * @param sets         set
     * @param sheet        sheet
     * @return 过滤字段
     */
    private static List<String> initSheet(Map<String, Object> maps, Workbook wb, CreationHelper createHelper,
                                          Set<String> sets, Sheet sheet) {
        List<String> fields = fillHeader(maps, createHelper, sets, sheet, wb);
        if (maps.containsKey("_TITLE_")) {
            fillTitle(maps, createHelper, sheet, wb);
            CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, fields.size() - 1);
            sheet.addMergedRegion(titleRange);
            setRegionBorder(titleRange, sheet);
        }
        // adjustWidth(sheet, fields);
        return fields;
    }

    /**
     * @param wb workbook
     * 初始化.
     */
    private static void initWorkbook(Workbook wb) {

        headStyle = wb.createCellStyle();
        footStyle = wb.createCellStyle();
        negFootStyle = wb.createCellStyle();
        headFont = wb.createFont();
        footFont = wb.createFont();
        footFontRed = wb.createFont();
        contentStyle = wb.createCellStyle();
        numberStyle = wb.createCellStyle();
        negNumberStyle = wb.createCellStyle();
        percentStyle = wb.createCellStyle();
        dataFormat = wb.createDataFormat();
        contentFont = wb.createFont();
        contentFontRed = wb.createFont();
        initHeadCellStyle();
        initHeadFont();
        initContentCellStyle();
        initContentFont();
    }

    /**
     * @param maps         表头
     * @param createHelper 工具
     * @param sheet        sheet
     * @param wb           workbook
     * 填充标题.
     */
    private static void fillTitle(Map<String, Object> maps, CreationHelper createHelper, Sheet sheet, Workbook wb) {
        if (maps.containsKey("_TITLE_")) {
            Row titleRow = sheet.createRow(0);
            titleRow.setHeight((short) 400);
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.cloneStyleFrom(headStyle);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(cellStyle);
            titleCell.setCellValue(createHelper.createRichTextString(maps.get("_TITLE_").toString()));
        }
    }

    /**
     * @param maps         表头
     * @param createHelper 工具
     * @param sets         集合
     * @param sheet        sheet
     * @param wb           工作薄s
     * @return fields
     * 填充表头.
     */
    private static List<String> fillHeader(Map<String, Object> maps, CreationHelper createHelper, Set<String> sets,
                                           Sheet sheet, Workbook wb) {
        List<String> fields = new ArrayList<String>();
        Row row = maps.containsKey("_TITLE_") ? sheet.createRow(1) : sheet.createRow(0);
        int i = 0;
        // 定义表头
        Row row2 = maps.containsKey("_TITLE_") ? sheet.createRow(2) : sheet.createRow(1);
        int startRowIndex = maps.containsKey("_TITLE_") ? 1 : 0;
        List<CellRangeAddress> cras = new ArrayList<CellRangeAddress>();
        boolean hasMerg = (Boolean) (maps.get("_HAS_MERG_") == null ? false : maps.get("_HAS_MERG_")); // 是否需要合并/支持两级
        for (Iterator<String> it = sets.iterator(); it.hasNext(); ) {
            String key = it.next();
            if ("_TITLE_".equals(key) || "_HAS_MERG_".equals(key)) {
                continue;
            }
            Object value = maps.get(key);
            Cell cell;
            if (hasMerg && !(value instanceof Map)) { // 需要合并,但没有子单元格,则合并行
                CellRangeAddress titleRange = new CellRangeAddress(startRowIndex, startRowIndex + 1, i, i);
                sheet.addMergedRegion(titleRange);
                cell = row.createCell(i++);
                cell.setCellStyle(headStyle);
                cell.setCellValue(createHelper.createRichTextString(maps.get(key).toString()));
                setRegionBorder(titleRange, sheet);
                fields.add(key);
                int length = maps.get(key).toString().getBytes().length;
                sheet.setColumnWidth((short) i - 1, (short) (length * 256));
            } else if (hasMerg && value instanceof Map) { // 需要合并，有子单元格，需要合并列
                @SuppressWarnings("unchecked")
                Map<String, Object> subMap = (Map<String, Object>) value;
                Set<String> subKeys = subMap.keySet();
                CellRangeAddress titleRange = new CellRangeAddress(startRowIndex, startRowIndex, i,
                        i + subKeys.size() - 1);
                sheet.addMergedRegion(titleRange);
                cras.add(titleRange);
                Cell faCell = row.createCell(i);
                CellStyle faCellStyle = wb.createCellStyle();
                faCellStyle.cloneStyleFrom(headStyle);
                faCell.setCellStyle(faCellStyle);
                faCell.setCellValue(createHelper.createRichTextString(key));
                for (Iterator<String> subIt = subKeys.iterator(); subIt.hasNext(); ) {
                    String subKey = subIt.next();
                    fields.add(subKey);
                    cell = row2.createCell(i++);
                    cell.setCellStyle(headStyle);
                    cell.setCellValue(createHelper.createRichTextString(subMap.get(subKey).toString()));
                    int length = subMap.get(subKey).toString().getBytes().length;
                    sheet.setColumnWidth((short) i - 1, (short) (length * 256));
                }
            } else if (!hasMerg) {
                cell = row.createCell(i++);
                cell.setCellStyle(headStyle);
                cell.setCellValue(createHelper.createRichTextString(maps.get(key).toString()));
                fields.add(key);
                int length = maps.get(key).toString().getBytes().length;
                sheet.setColumnWidth((short) i - 1, (short) (length * 256));
            }
        }
        if (cras != null && cras.size() > 0) {
            for (CellRangeAddress cra : cras) {
                setRegionBorder(cra, sheet);
            }
        }
        sheet.createFreezePane(0, startRowIndex + 2, 0, startRowIndex + 2);
        sheet.setAutoFilter(new CellRangeAddress(startRowIndex + 1, startRowIndex + 1, 0, fields.size() - 1));
        return fields;
    }

    /**
     * @param sheet  sheet
     * @param fields 字段
     * 调整列宽
     */
    private static void adjustWidth(Sheet sheet, List<String> fields) {
        int i = 0;
        for (Iterator<String> it = fields.iterator(); it.hasNext(); ) {
            it.next();
            sheet.autoSizeColumn(i++, true);
        }
    }

    /**
     * @Description: 初始化内容行字体
     */
    private static void initContentFont() {
        contentFont.setFontName("Arial");
        contentFont.setFontHeightInPoints((short) 10);
//        contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        contentFont.setCharSet(Font.DEFAULT_CHARSET);
        contentFont.setColor(IndexedColors.BLACK.index);

        contentFontRed.setFontName("Arial");
        contentFontRed.setFontHeightInPoints((short) 10);
//        contentFontRed.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        contentFontRed.setCharSet(Font.DEFAULT_CHARSET);
        contentFontRed.setColor(IndexedColors.RED.index);
    }

    /**
     * @Description: 初始化表头行字体
     */
    private static void initHeadFont() {
        headFont.setFontName("Arial");
        headFont.setFontHeightInPoints((short) 11);
//        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headFont.setCharSet(Font.DEFAULT_CHARSET);
        headFont.setColor(IndexedColors.BLACK.index);

        footFont.setFontName("Arial");
        footFont.setFontHeightInPoints((short) 10);
//        footFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        footFont.setCharSet(Font.DEFAULT_CHARSET);
        footFont.setColor(IndexedColors.BLUE.index);

        footFontRed.setFontName("Arial");
        footFontRed.setFontHeightInPoints((short) 10);
//        footFontRed.setBoldweight(Font.BOLDWEIGHT_BOLD);
        footFontRed.setCharSet(Font.DEFAULT_CHARSET);
        footFontRed.setColor(IndexedColors.RED.index);
    }

    /**
     * 获取excel导出分sheet时的页面大小数据.
     *
     * @param size 数组大小
     * @return long 每个sheet数据量
     */
    private static int getSplitNum(int size) {

        if (size >= 0 && size < 60000) { // 1
            return 60000;
        } else if (size < 100000) { // 5
            return 20000;
        }
        return 10000;
    }

    /**
     * @Description: 初始化内容行样式
     */
    private static void initContentCellStyle() {
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentStyle.setFont(contentFont);
        contentStyle.setWrapText(false); // 字段换行

        numberStyle.setAlignment(HorizontalAlignment.CENTER);
        numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        numberStyle.setFont(contentFont);
        numberStyle.setWrapText(false); // 字段换行
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        negNumberStyle.setAlignment(HorizontalAlignment.CENTER);
        negNumberStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        negNumberStyle.setFont(contentFontRed);
        negNumberStyle.setWrapText(false); // 字段换行
        negNumberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        percentStyle.setAlignment(HorizontalAlignment.CENTER);
        percentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        percentStyle.setFont(contentFont);
        percentStyle.setWrapText(false); // 字段换行
        percentStyle.setDataFormat(dataFormat.getFormat("0.00%"));
    }

    /**
     * 设置合并了单元格的格式.
     *
     * @param region 合并
     * @param sheet  表格
     */
    private static void setRegionBorder(CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.index, region, sheet);
        RegionUtil.setLeftBorderColor(IndexedColors.BLACK.index, region, sheet);
        RegionUtil.setRightBorderColor(IndexedColors.BLACK.index, region, sheet);
        RegionUtil.setTopBorderColor(IndexedColors.BLACK.index, region, sheet);
    }

    /**
     * @Description: 初始化表头行样式
     */
    private static void initHeadCellStyle() {
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setFont(headFont);

        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setTopBorderColor(IndexedColors.BLACK.index);
        headStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        headStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        headStyle.setRightBorderColor(IndexedColors.BLACK.index);

        // 脚
        footStyle.setAlignment(HorizontalAlignment.CENTER);
        footStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        footStyle.setFont(footFont);
        footStyle.setBorderTop(BorderStyle.DOUBLE);
        footStyle.setTopBorderColor(IndexedColors.BLACK.index);
        footStyle.setWrapText(false); // 字段换行
        footStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        negFootStyle.setAlignment(HorizontalAlignment.CENTER);
        negFootStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        negFootStyle.setFont(footFontRed);
        negFootStyle.setBorderTop(BorderStyle.DOUBLE);
        negFootStyle.setTopBorderColor(IndexedColors.BLACK.index);
        negFootStyle.setWrapText(false); // 字段换行
        negFootStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
    }
}
