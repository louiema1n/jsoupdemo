package com.lm.jsoupdemo;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupdemoApplicationTests {

	@Test
	public void contextLoads() {
		// 加载cookie
		Map<String, String> cookie = simulationLogin();
		List<Map<String, String>> listId = getJsonFromFile(System.getProperty("user.dir") + "/json/id.xls");
		// 需要解析url地址
		if (cookie != null) {
			HSSFWorkbook wbOut = new HSSFWorkbook();
			HSSFSheet sheet = wbOut.createSheet("result");
			HSSFRow row;
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("id");
			row.createCell(1).setCellValue("result");
			for (int i = 0; i < listId.size(); i++) {
			    String id = listId.get(i).get("id");
				String url = "http://lims.qwings.cn/database/viewTestMajorPage.do?testMajor.id=" + id;
				// 使用Jsoup进行url资源获取
				Connection connect = Jsoup.connect(url);
				try {
					// 获取资源对象
					Document document = connect.cookies(cookie).get();
					// 解析资源
					Elements elements = document.select(".equip_div").select("p").select("a");
					row = sheet.createRow(i + 1);
					row.createCell(0).setCellValue(id);
					row.createCell(1).setCellValue(elements.get(1).text());

					// 写出到文件
					File distFile = new File(System.getProperty("user.dir") + "/json/id-result.xls");
					if (!distFile.getParentFile().exists()) {
						distFile.getParentFile().mkdirs();
					}
					wbOut.write(distFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				// 写出到文件
				File distFile = new File(System.getProperty("user.dir") + "/json/id-result.xls");
				if (!distFile.getParentFile().exists()) {
					distFile.getParentFile().mkdirs();
				}
				wbOut.write(distFile);
				wbOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void getJson() {
		List list = getJsonFromFile(System.getProperty("user.dir") + "/json/id.xls");
		System.out.println(list);
	}

	/**
	 * @description 模拟登陆，获取cookie
	 * @author louiemain
	 * @date Created on 2018-02-11 9:13
	 * @param
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 */
	public Map<String, String> simulationLogin() {
		Map<String, String> loginInfo = new HashMap<>();
		loginInfo.put("account.name", "admin");
		loginInfo.put("account.password", "www.qwings.cn");

		try {
			Connection.Response response = Jsoup.connect("https://passport.weibo.cn/signin/login")
					.data(loginInfo)
					.method(Connection.Method.POST)
					.timeout(20000)
					.execute();
			if (response.statusCode() == 200) {
				Map<String, String> cookies = response.cookies();
				return cookies;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 读取本地excel文件to json
	public List<Map<String, String>> getJsonFromFile(String filePath) {
		File file = new File(filePath);
		FileInputStream fis;
		HSSFWorkbook workbook;
		List<Map<String, String>> list = new ArrayList<>();
		Map<String, String> map = null;
		try {
			fis = new FileInputStream(file);
			workbook = new HSSFWorkbook(fis);
			// 读取第一个sheet
			HSSFSheet sheet = workbook.getSheetAt(0);
			if (sheet != null) {
				// 遍历行
				HSSFRow row;
				HSSFCell cell;
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
					// 遍历列
					row = sheet.getRow(i);
					map = new LinkedHashMap<>();
					for (int j = 0; j < row.getLastCellNum(); j++) {
						cell = row.getCell(j);
						String key = sheet.getRow(0).getCell(j).getStringCellValue();
						String value;
						// 设置单元格值格式为String
						if (cell == null || cell.equals("")) {
							value = "无";
						} else {
							cell.setCellType(CellType.STRING);
							value = cell.getStringCellValue();
							if (value == "") {
								value = "无";
							}
						}
						map.put(key, value);
					}
					list.add(map);
				}
			}
			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 下载文件
	public void down2File(HSSFWorkbook wbOut) {
		String fileName = System.getProperty("user.dir") + "/json/id-result.xls";

		// 下载到指定文件
		File distFile = new File(fileName);
		if (!distFile.getParentFile().exists()) {
			distFile.getParentFile().mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(distFile);
			wbOut.write(fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
