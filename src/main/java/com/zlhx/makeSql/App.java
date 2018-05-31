package com.zlhx.makeSql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import com.zlhx.makeSql.entity.Phonearea;
import com.zlhx.makeSql.excel.UploadExcelUtils;
import com.zlhx.makeSql.utils.Common;
import com.zlhx.makeSql.utils.ImpUtil;
import com.zlhx.makeSql.utils.ReadExcel;
import antlr.collections.impl.LList;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	System.out.println("===start===");
     // TODO Auto-generated method stub
        //定义配置文件路径
        String path = "JdbcTemplateBeans.xml";
        //加载配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(path);
        //获取jdbcTemplate实例
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
        String sql = "select * from phonearea group by city";

        List<Phonearea>list = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<Phonearea>(Phonearea.class));
        Map<String, String>map = new HashMap<String, String>();
        for(Phonearea m : list) {
        	map.put(m.getCity(), m.getAreacode());
        }
        
        String filepath = "/Users/liu/Desktop/exchange.xlsx";
        String file_1_10 = "/Users/liu/Desktop/file_1_10.txt";
        String file_10_3 = "/Users/liu/Desktop/file_10_3.txt";
        String file_10_0 = "/Users/liu/Desktop/file_10_0.txt";
        
        File file = new File(filepath);
        
        
        List<Phonearea>list2 = ReadExcel.readExcel(filepath);
    
       
        
        System.out.println(String.format("====file_1_10:%s====file_10_3:%s======list.size:%d", file_1_10,file_10_3,list2.size()));
        
        for(int i =0; i< list2.size(); i++) {
        	Phonearea model = list2.get(i);
        	model.setAreacode(map.get(model.getCity()));
        	
        	model.getNetwork().indexOf(".");
        	String aaa = model.getNetwork().substring(0, model.getNetwork().indexOf("."));
        	
        	 String content = "";
             String content2 = "";
             String content3 ="";
            content = String.format("update mobile_number set type=10 where account_id=466  and area_code='%s' and type=1 ORDER BY RAND() LIMIT %d;", model.getAreacode(),Integer.valueOf(aaa));
        	content2 = String.format("update mobile_number set type=3 where account_id=466  and area_code='%s' and type=10 ORDER BY RAND() LIMIT %d;", model.getAreacode(),Integer.valueOf(aaa));
        	content3 = String.format("update mobile_number set type=0 where account_id=466  and area_code='%s' and type=10;", model.getAreacode());
        	method2(file_1_10,content);
        	method2(file_10_3,content2);
        	method2(file_10_0,content3);
        	
        	System.out.println("index:" + i);
        }
        System.out.println("===end===");
        
    }
    
    public static void method2(String file, String content) {
    	BufferedWriter out = null;
    	try {
	    	out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
	    	out.write(content+"\r\n");
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
	    	try {
	    		out.close();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
    }
    
    
}
