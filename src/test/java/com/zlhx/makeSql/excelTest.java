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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zlhx.makeSql.entity.Phonearea;
import com.zlhx.makeSql.excel.UploadExcelUtils;
import com.zlhx.makeSql.utils.ImpUtil;

public class excelTest {
	/**
	 * 读取excel 数据导入
	 * 
	 * @throws Exception
	 */
	public static void excuteImp() throws Exception {
		System.out.println("===start===");
		// TODO Auto-generated method stub
		// 定义配置文件路径
		String path = "JdbcTemplateBeans.xml";
		// 加载配置文件
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(path);
		// 获取jdbcTemplate实例
		JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
		String sql = "select * from phonearea group by city";

		List<Phonearea> list = jdbcTemplate.query(sql, new Object[] {},
				new BeanPropertyRowMapper<Phonearea>(Phonearea.class));
		Map<String, String> map = new HashMap<String, String>();
		for (Phonearea m : list) {
			map.put(m.getCity(), m.getAreacode());
		}

		String filepath = "/Users/qianjianlei/share/1/exchange.xlsx";
		String file_1_10 = "/Users/qianjianlei/share/1/file_1_10.txt";
		String file_10_3 = "/Users/qianjianlei/share/1/file_10_3.txt";
		String file_10_0 = "/Users/qianjianlei/share/1/file_10_0.txt";

		File file = new File(filepath);

		InputStream is = new FileInputStream(filepath);
		Phonearea phonearea = new Phonearea();
		List<Phonearea> list2 = ImpUtil.readDateFromPath(filepath, is, phonearea, 2, 0);

		System.out.println(String.format("====file_1_10:%s====file_10_3:%s======list.size:%d", file_1_10, file_10_3,
				list2.size()));

		for (int i = 0; i < list2.size(); i++) {
			Phonearea model = list2.get(i);
			model.setAreacode(map.get(model.getCity()));

			model.getNetwork().indexOf(".");
			String aaa = model.getNetwork();// .substring(0, model.getNetwork().indexOf("."));
			String content = "";
			String content2 = "";
			String content3 = "";

			content = String.format(
					"update mobile_number set type=10 where account_id=466  and area_code='%s' and type=1 ORDER BY RAND() LIMIT %d;",
					model.getAreacode(), Integer.valueOf(aaa));
			content2 = String.format(
					"update mobile_number set type=3 where account_id=466  and area_code='%s' and type=10 ORDER BY RAND() LIMIT %d;",
					model.getAreacode(), Integer.valueOf(aaa));
			content3 = String.format(
					"update mobile_number set type=0 where account_id=466  and area_code='%s' and type=10;",
					model.getAreacode());
			method2(file_1_10, content);
			method2(file_10_3, content2);
			method2(file_10_0, content3);

			System.out.println("index:" + i);
		}
		System.out.println("===end===");
	}

	public static void method2(String file, String content) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(content + "\r\n");
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

	private void imp() {
		// *********************************************************************
		// ******************** Spring MVC 数据导入***************************
		// *********************************************************************
		//
		// ********************** 3.1 html *******************************
		// <input type="file" id="file" style="display: none" @change="fileImport">
		// <a v-if="hasPermission('companycustomer:imp')" class="btn btn-success"
		// @click="impShow"><i class="fa fa-level-down"></i>&nbsp;导入</a>
		// <div v-show="showImp && show0" class="panel panel-default">
		// <a v-if="hasPermission('companycustomer:imp')" class="btn btn-success"
		// @click="imp"><i class="fa fa-trash-o"></i>&nbsp;选择文件并导入</a>
		// 不知道数据格式？请<a href="客户资料导入模板.xlsx" style="color:#00008B;">下载模板</a>
		// </div>
		//
		// ********************** 3.2 js *******************************
		// imp: function(){
		// $("#file").click();
		// },
		// impShow: function(){
		// vm.showImp = true;
		// },
		// fileImport: function(e){
		// var path = $("#file").val();
		// if(path == ''){
		// alert("请选择excel,再上传");
		// }else if(path.lastIndexOf(".xls")<0){//可判断以.xls和.xlsx结尾的excel
		// alert("只能上传Excel文件");
		// }else{
		// var formData = new FormData();
		// formData.append('file', e.target.files[0]);
		// $("#file")[0].value = "";
		// $.ajax({
		// type: "POST",
		// url: url_imp,
		// data: formData,
		// cache: false,
		// processData: false,
		// contentType: false,
		// success: function(r){
		// vm.showImp = false;
		// if(r.code == 0){
		// alert('操作成功', function(index){
		// $("#jqGrid").trigger("reloadGrid");
		// });
		// }else{
		// alert(r.msg);
		// }
		// },
		// error: function(){
		// vm.showImp = false;
		// }
		// });
		// }
		// }
		// ********************** 3.3 controller *******************************
		// @RequestMapping("imp")
		// @RequiresPermissions("companycustomer:imp")
		// public R imp(MultipartFile file) {
		// try {
		// InputStream is = file.getInputStream();
		// Workbook wb = ImportExcelUtil.chooseWorkbook(file.getName(), is);
		// CompanyCustomer ca = new CompanyCustomer();
		// List<CompanyCustomer> readDateListT = null;
		// Map<String, Object> map = new HashMap<String,Object>();
		// try {
		// if (getType() == UserType.COMPANY.getValue()) {
		// ca.setCreateUserId(getCompanyId().longValue());
		// ca.setOwnerUserId(0L);
		// } else if (getType() == UserType.COMPANY_USER.getValue()) {
		// ca.setCreateUserId(getCompanyUserId());
		// ca.setOwnerUserId(getCompanyUserId());
		// }
		//
		// Date date = new Date();
		//
		// ca.setCompanyId(getCompanyId());
		// ca.setCreateTime(date);
		// ca.setUpdateTime(date);
		// ca.setType(0);
		//
		// map = ImportExcelUtil.readDateListTCompanyCustomer(wb, ca, 2, 0);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// if(map.get("code").toString().equals("0")) {
		// throw new RRException(map.get("msg").toString());
		// }else {
		// readDateListT = (List<CompanyCustomer>)map.get("list");
		// }
		//
		// if(readDateListT.size() > 5000) {
		// return R.error("单次导入最多5000条，请编辑后重新导入。");
		// }
		// if(readDateListT.size() >0) {
		// companyCustomerService.saveAll(readDateListT);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }catch(org.springframework.dao.DataIntegrityViolationException e) {
		// e.printStackTrace();
		// throw new RRException("codeStr不唯一，导入失败！");
		// }
		//
		// return R.ok();
		// }
	}

	/**
	 * 数据导出 写入excel
	 */
	private void exp() {
		// ********************************************************
		// *************** Spring MVC数据导出 **********************
		// ********************************************************
		//
		// *************** 3.1 html ***************
		// <a class="btn btn-info" @click="expexcel"><i class="fa
		// fa-plus"></i>&nbsp;excel导出</a>
		//
		// *************** 3.2 js ***************
		// expexcel: function(){
		// var ids = getSelectedRows();
		// if(ids == null){
		// return ;
		// }
		//
		// window.open('../company/cdr/export?ids=' + ids);
		// }
		//
		// *************** 3.3 controller ***************
		// package com.zlhx.makeSql.excel;
		//
		// @RequestMapping("/export")
		// @RequiresPermissions("company:cdr:export")
		// public void export(Integer[] ids,HttpServletResponse response) throws
		// Exception, IOException {
		// //获取数据
		// Map<String, Object> map = new HashMap<>();
		// map.put("ids", ids);
		// List<CompanyCdrModel> list =
		// companyDepartUserMenuService.findByCompanyCdrIds(map);
		//
		// //导出操作
		// String[] firstRowValue = new
		// String[]{"客户姓名","开始时间","结束时间","通话时长","通话状态","话单类型","录音","分组"};
		// List<Object[]> objList = new ArrayList<Object[]>();
		// for(int i=0;i<list.size();i++){
		// CompanyCdrModel cc =(CompanyCdrModel) list.get(i);
		// Object[] obj = new Object[]
		// {cc.getCustomerName(),DateUtils.formateDate(cc.getBeginTime(),"yyyy-MM-dd
		// HH-mm-ss"),DateUtils.formateDate(cc.getReleaseTime(),"yyyy-MM-dd
		// HH-mm-ss"),cc.getCallDuration(),cc.getCallResult(),cc.getCdrType(),cc.getRecordFileUrl(),cc.getGroupName()};
		// Object[] newObj = new Object[firstRowValue.length];
		// for(int k=0;k<firstRowValue.length;k++){
		// newObj[k]=obj[k];
		// }
		// objList.add(newObj);
		// }
		// response.setContentType("octets/stream");
		// response.addHeader("Content-Disposition", "attachment;filename=CallCdr.xls");
		// UploadExcelUtils.exportExcel("CompanyCdr", objList, firstRowValue, response);
		// }
	}

	/**
	 * 写入excel
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static void executeOut() throws UnsupportedEncodingException, FileNotFoundException {
		List<Phonearea> list = new ArrayList<Phonearea>();
		Phonearea m1 = new Phonearea();
		m1.setAreacode("1");
		m1.setPhone(1);
		Phonearea m2 = new Phonearea();
		m2.setAreacode("2");
		m2.setPhone(2);
		list.add(m1);
		list.add(m2);

		// 导出操作
		String[] firstRowValue = new String[] { "code", "phone" };
		List<Object[]> objList = new ArrayList<Object[]>();
		for (int i = 0; i < list.size(); i++) {
			Phonearea cc = (Phonearea) list.get(i);
			Object[] obj = new Object[] { cc.getAreacode(), cc.getPhone() };
			Object[] newObj = new Object[firstRowValue.length];
			for (int k = 0; k < firstRowValue.length; k++) {
				newObj[k] = obj[k];
			}
			objList.add(newObj);
		}
		String filepath = "/Users/qianjianlei/share/1/exchange.xlsx";
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		UploadExcelUtils.exportExcel("CompanyCdr", objList, firstRowValue, filepath);
	}
}
