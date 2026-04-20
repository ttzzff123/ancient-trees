package com.example.ancienttreesystem.util;

import com.example.ancienttreesystem.entity.AncientTree;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 工具类
 */
public class ExcelUtil {

    /**
     * 导出古树数据到 Excel
     */
    public static void exportTreesToExcel(List<AncientTree> trees, HttpServletResponse response) throws IOException {
        // 1. 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("古树名木清单");

        // 2. 创建表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // 3. 创建表头
        String[] headers = {
                "ID", "编号", "树种", "树龄", "保护等级", "生长状况",
                "树高(m)", "胸径(cm)", "所在乡镇", "村", "林区",
                "经度", "纬度", "海拔(m)", "权属", "调查日期"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 4. 填充数据（全部转为 String 避免类型错误）
        int rowNum = 1;
        for (AncientTree tree : trees) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(String.valueOf(tree.getId()));
            row.createCell(1).setCellValue(tree.getTreeCode() != null ? tree.getTreeCode() : "");
            row.createCell(2).setCellValue(tree.getSpecies() != null ? tree.getSpecies() : "");
            row.createCell(3).setCellValue(tree.getTreeAge() != null ? String.valueOf(tree.getTreeAge()) : "");
            row.createCell(4).setCellValue(tree.getProtectionLevel() != null ? tree.getProtectionLevel() : "");
            row.createCell(5).setCellValue(tree.getGrowthStatus() != null ? tree.getGrowthStatus() : "");
            row.createCell(6).setCellValue(tree.getHeight() != null ? tree.getHeight().toString() : "");
            row.createCell(7).setCellValue(tree.getDbh() != null ? tree.getDbh().toString() : "");
            row.createCell(8).setCellValue(tree.getTown() != null ? tree.getTown() : "");
            row.createCell(9).setCellValue(tree.getVillage() != null ? tree.getVillage() : "");
            row.createCell(10).setCellValue(tree.getForestArea() != null ? tree.getForestArea() : "");
            row.createCell(11).setCellValue(tree.getLongitude() != null ? tree.getLongitude().toString() : "");
            row.createCell(12).setCellValue(tree.getLatitude() != null ? tree.getLatitude().toString() : "");
            row.createCell(13).setCellValue(tree.getAltitude() != null ? tree.getAltitude().toString() : "");
            row.createCell(14).setCellValue(tree.getOwnership() != null ? tree.getOwnership() : "");
            row.createCell(15).setCellValue(tree.getSurveyDate() != null ? tree.getSurveyDate().toString() : "");
        }

        // 5. 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 6. 设置响应头（告诉浏览器下载文件）
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = URLEncoder.encode("古树名木清单_" + System.currentTimeMillis() + ".xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 7. 写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * 从Excel导入古树数据
     */
    public static List<AncientTree> importTreesFromExcel(InputStream inputStream) throws IOException {
        List<AncientTree> trees = new ArrayList<>();

        // 1. 读取工作簿
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);  // 获取第一个表

        // 2. 从第2行开始读取（第1行是表头）
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;  // 跳过空行

            AncientTree tree = new AncientTree();

            // 读取各列数据（注意：列号从0开始）
            // 第0列ID通常不用读，数据库自动生成
            tree.setTreeCode(getCellValue(row.getCell(1)));   // 编号
            tree.setSpecies(getCellValue(row.getCell(2)));    // 树种
            tree.setTreeAge(parseInt(getCellValue(row.getCell(3))));  // 树龄
            tree.setProtectionLevel(getCellValue(row.getCell(4)));    // 保护等级
            tree.setGrowthStatus(getCellValue(row.getCell(5)));       // 生长状况
            tree.setHeight(parseBigDecimal(getCellValue(row.getCell(6))));   // 树高
            tree.setDbh(parseBigDecimal(getCellValue(row.getCell(7))));      // 胸径
            tree.setTown(getCellValue(row.getCell(8)));       // 乡镇
            tree.setVillage(getCellValue(row.getCell(9)));    // 村
            tree.setForestArea(getCellValue(row.getCell(10))); // 林区
            tree.setLongitude(parseBigDecimal(getCellValue(row.getCell(11))));  // 经度
            tree.setLatitude(parseBigDecimal(getCellValue(row.getCell(12))));   // 纬度
            tree.setAltitude(parseBigDecimal(getCellValue(row.getCell(13))));   // 海拔
            tree.setOwnership(getCellValue(row.getCell(14)));  // 权属
            // 第15列调查日期暂时不处理，或者可以解析

            trees.add(tree);
        }

        workbook.close();
        return trees;
    }

    /**
     * 获取单元格字符串值
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // 如果是日期类型
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // 数字类型，转字符串（去掉小数点后的0）
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * 字符串转Integer
     */
    private static Integer parseInt(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 字符串转BigDecimal
     */
    private static BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
