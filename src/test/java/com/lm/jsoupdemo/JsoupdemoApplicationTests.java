package com.lm.jsoupdemo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupdemoApplicationTests {

	@Test
	public void contextLoads() {
		// 需要解析url地址
		String url = "http://zhibo.sogou.com/gameZone_%E8%8B%B1%E9%9B%84%E8%81%94%E7%9B%9F.whtml?product=live&page=1&type=1&f=0#resultlist";
		// 使用Jsoup进行url资源获取
		Connection connect = Jsoup.connect(url);
		try {
			// 获取资源对象
			Document document = connect.get();
			// 解析资源
			Elements elements = document.select("#targetContent").select("div");
			Elements as = elements.select("div").select("p").select("a");
			Elements ptms = elements.select("div").select("div").select("span.zb").select("i.ptm");
			Elements zbms = elements.select("div").select("div").select("span.zb").select("i.zbm");
			Elements nums = elements.select("div").select("div").select("span.num");
			for (int i = 0; i < as.size(); i++) {
				System.out.println(as.get(i).attr("title") + " " + as.get(i).attr("href") + " " + ptms.get(i).text() + "-" + zbms.get(i).text() + " " + nums.get(i).text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
