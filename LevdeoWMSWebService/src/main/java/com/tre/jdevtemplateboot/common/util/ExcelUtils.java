package com.tre.jdevtemplateboot.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.tre.jdevtemplateboot.domain.po.StockMoveSpWork;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Execl工具类
 *
 * @author SongGuiFan
 */
public class ExcelUtils {

    /**
     * 导出excel
     *
     * @param datas      数据列表
     * @param titles     标题列表
     * @param columns    数据字段列表
     * @param prefixname 文件名前缀
     * @param response
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static void downloadExcel(
            List<Map<String, Object>> datas,
            List<String> titles,
            List<String> columns,
            String prefixname,
            HttpServletResponse response
    ) throws Exception {

        OutputStream out = null;
        try {
            //获取当前登录用户的userCode
            String usercode = CommonUtils.getUserCode();
            //获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String datetime = df.format(new Date());
            //文件名
            String filename = prefixname + "-" + usercode + "-" + datetime + ".xlsx";

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("sheet1");
            XSSFRow row = sheet.createRow(0);

            //表头样式：居中、灰色
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

            //表头
            XSSFCell cell;
            for (int i = 0; i < titles.size(); i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(titles.get(i));
            }

            //非表头样式：如果是数字，右对齐
            cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);

            //循环数据
            Map<String, Object> data;
            Object obj;//每个单元格的内容
            for (int i = 0; i < datas.size(); i++) {
                data = datas.get(i);
                row = sheet.createRow(i + 1);
                for (int j = 0; j < columns.size(); j++) {
                    cell = row.createCell(j);
                    obj = data.get(columns.get(j));

                    //数字靠右对齐
                    if (obj instanceof Double || obj instanceof Integer || obj instanceof Float || obj instanceof Long) {
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(Double.parseDouble(String.valueOf(obj)));
                    } else {
                        if (obj != null) {
                            cell.setCellValue(String.valueOf(obj));
                        }
                    }
                }
            }

            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");

            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 将Excel文件转化为List集合（暂时只有车辆移库使用，如果要通用，需要返回List）
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static List<StockMoveSpWork> excel2ListStockMoveSpWork(MultipartFile file) throws Exception {

        //为了兼容XLS和XLSX格式
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //读取第1个sheet
        Sheet sheet = workbook.getSheetAt(0);
        //遍历每行每列，转化为List集合
        List<StockMoveSpWork> spWorklist = new ArrayList<>();
        //沒有数据返回size=0的list
        if (sheet == null || sheet.getLastRowNum() < 1) {
            return spWorklist;
        }

        StockMoveSpWork stockMoveSpWork;//一行数据
        int lastRowNum = sheet.getLastRowNum() + 1;//行数
        Row row;//行数据
        for (int i = 1; i < lastRowNum; i++) {
            row = sheet.getRow(i);
            if (row == null || row.getLastCellNum() != 3) {//跳过空行数据不全的行
                continue;
            }

            //VIN码
            Cell cell = row.getCell(0);
            //跳过空单元格
            if (cell == null || StringUtils.isEmpty(cell.getStringCellValue())) {
                continue;
            }
            String vin = cell.getStringCellValue();

//            cell = row.getCell(1);
//            if (cell == null || StringUtils.isEmpty(cell.getStringCellValue())) {
//                continue;
//            }
//            String oldPosition = cell.getStringCellValue();

            //新库位号
            cell = row.getCell(2);
            if (cell == null || StringUtils.isEmpty(cell.getStringCellValue())) {
                continue;
            }
            String newPosition = cell.getStringCellValue();

            stockMoveSpWork = new StockMoveSpWork();
            stockMoveSpWork.setVin(vin.length() > 17 ? "0" : vin);
            stockMoveSpWork.setNewPosition(newPosition.length() > 50 ? "0" : newPosition);
            stockMoveSpWork.setRowNum(i);//追加行号
            stockMoveSpWork.setOperator(CommonUtils.getUserCode());
            spWorklist.add(stockMoveSpWork);
        }
        return spWorklist;
    }
}
