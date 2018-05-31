package com.zlhx.makeSql.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UploadExcelUtils {

	/**
	 * @param excelTitle
	 * @param paramName
	 * @param work
	 * @param cls
	 */
	public static List<?> importExcel(String[] excelTitle,String[] paramName,InputStream inputStream,Class<?> cls) throws Exception{
		Workbook work = null;
		try {
			work = new XSSFWorkbook(inputStream);
        } catch (Exception ex) {
        	work = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = null;
		Row row = null;
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < work.getNumberOfSheets(); i++) { 
			sheet = work.getSheetAt(i);
			if(excelTitle != null){
				row = sheet.getRow(0);
				if (row == null) break;
				for(int j=0;j<excelTitle.length;j++){
					Cell cell = row.getCell((short)j);
					if(!excelTitle[j].equals(cell.getRichStringCellValue().toString())){
						throw new Exception("模板格式不匹配");
					}
				}
			}
			for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
				row = sheet.getRow(j); 
				//判断是否还存在需要导入的数据
				if (row == null) { 
					System.out.println("这里已没有数据，在第"+i+"列,第"+j+"行"); 
					break;
				}
				Object newObj = cls.getConstructor(new Class[]{}).newInstance(new Object[]{});
				for(int k = 0;k<paramName.length;k++){
					String fieldName = paramName[k];
					Field field = cls.getDeclaredField(fieldName);
					String setMethodName = "set"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
					try {
						Method setMethod = cls.getMethod(setMethodName, new Class[] {field.getType()});
						Object value = null;
						Cell cell = null;
						if((cell = row.getCell((short)k)) != null){
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								value = cell.getRichStringCellValue().toString();
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if(field.getType().isAssignableFrom(Date.class))
									value = cell.getDateCellValue();
								else value = new DecimalFormat("#").format(cell.getNumericCellValue());
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								value = cell.getBooleanCellValue();
								break;
							default:
								value = cell.getRichStringCellValue().toString();
								break;
							}
							if(null != value && field.getType().isAssignableFrom(String.class)){
								value = value.toString();
							}else if(null != value && field.getType().isAssignableFrom(Date.class)){
								String dateStr = (String) value;
								if(dateStr.length() == 10) value = DateUtils.stringToTime(dateStr, "yyyy-MM-dd");
								else value = DateUtils.stringToTime(dateStr, "yyyy-MM-dd HH:mm:ss");
							}else if(null != value && field.getType().isAssignableFrom(Boolean.class)){
								value = Boolean.valueOf(value.toString());
							}else if(null != value && field.getType().isAssignableFrom(Integer.class)){
								value = Integer.valueOf(value.toString());
							}else if(null != value && field.getType().isAssignableFrom(Long.class)){
								value = Long.valueOf(value.toString());
							}
						}
						if(null != value)
							setMethod.invoke(newObj, value);
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
				list.add(newObj);
			}
		}
		return list;
	}
	
	
	public static void exportExcel(String sheetName,List<Object[]> list, String[] firstRowValue,String outpath) throws UnsupportedEncodingException, FileNotFoundException {
		String downLoadName = new String(sheetName.getBytes("utf-8"), "iso8859-1");
		ExcelWorkBook work = new ExcelWorkBook(sheetName);
		work.getSheet().setDefaultColumnWidth((short) 15);
        ExcelCellStyleUtils util = new ExcelCellStyleUtils(work);  
        ExcelSheetCell.createCurrRowTitle(work, firstRowValue, util.getTitleStyle());
        ExcelSheetCell.createCurrRowRecord(work, list, util.getNameStyle());
       
     // 写入文件中
        FileOutputStream out = new FileOutputStream(outpath);
       
		try {
			work.getWorkbook().write(out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 导出excel 
	 * @param sheetName 工作表名称
	 * @param list 需要导出的数据Object[]
	 * @param firstRowValue 标题行项目名称
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	public static void exportExcel(String sheetName,List<Object[]> list, String[] firstRowValue,HttpServletResponse response) throws UnsupportedEncodingException{
		response.setContentType("octets/stream");
		String downLoadName = new String(sheetName.getBytes("utf-8"), "iso8859-1");
		response.addHeader("Content-Disposition", "attachment;filename="+downLoadName+".xls");
		ExcelWorkBook work = new ExcelWorkBook(sheetName);
		work.getSheet().setDefaultColumnWidth((short) 15);
        ExcelCellStyleUtils util = new ExcelCellStyleUtils(work);  
        ExcelSheetCell.createCurrRowTitle(work, firstRowValue, util.getTitleStyle());
        ExcelSheetCell.createCurrRowRecord(work, list, util.getNameStyle());
        OutputStream out = null;
		try {
			out = response.getOutputStream();
			work.getWorkbook().write(out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
}
