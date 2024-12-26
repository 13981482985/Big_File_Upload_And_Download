package com.yang.fileUpload.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;


/**
 * 这个对象不能作为单例使用
 */
public class DownloadProcessor {
    private final HttpServletResponse response;

    private SXSSFWorkbook workbook;
    private Sheet sheet;
    private int rowIndex = 0;

    private String tableName;

    public DownloadProcessor(HttpServletResponse response,String tableName) {
        this.tableName = tableName;
        this.response = response;
        String fileName = tableName + ".xlsx"; // 设置下载表名

        this.response.addHeader("Content-Type", "application/form-data");
        this.response.addHeader("Content-Disposition", "attachment; filename="+fileName);
        this.response.setCharacterEncoding("UTF-8");
        /**
         * SXSSFWorkbook：支持流式写入（即逐行写入 Excel 文件），避免将整个数据集加载到内存中
         * 设置保留的最大行数为100行
         */
        workbook = new SXSSFWorkbook(100);  //
        sheet = workbook.createSheet("Sheet1");  // 创建一个新的工作表
    }

    // 将 record 这一行数据写入到 xlsx 文件
    public void processData(LinkedHashMap<String, Object> record) throws IOException {

        // 如果是第一行，写入表头
        if (rowIndex == 0) {
            Row headerRow = sheet.createRow(rowIndex++);
            int headerCellIndex = 0;

            // 获取表头（record 的键作为列名）
            for (String key : record.keySet()) {
                Cell cell = headerRow.createCell(headerCellIndex++);
                cell.setCellValue(key);
            }
        }

        // 写入数据行
        Row dataRow = sheet.createRow(rowIndex++);
        int cellIndex = 0;

        // 将 record 的值写入每一列
        for (Object value : record.values()) {
            Cell cell = dataRow.createCell(cellIndex++);
            if (value != null) {
                cell.setCellValue(value.toString());
            } else {
                cell.setCellValue("");  // 如果值为空，则设置为空字符串
            }
        }

        // 在每次写入一定数量的行之后，调用 flushRows() 方法来将内存中的行数据刷新到磁盘，以释放内存
        if (rowIndex % 100 == 0) {  // 每100行清理一次
            ((SXSSFSheet) sheet).flushRows();
        }
    }
    public void finalizeDownload() {
        try {
            // 将工作簿内容写入到响应输出流（确保一次性输出）
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            try {
                workbook.dispose();  // 释放 SXSSFWorkbook 使用的临时文件
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}