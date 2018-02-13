package com.lm.jsoupdemo.Utils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.CollectorPipeline;
import us.codecraft.webmagic.pipeline.JsonFilePageModelPipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @description
 * @author&date Created by louiemain on 2018/2/11 21:16
 */
public class GithubRepoPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等(百度spider+)
    private Site site = Site.me().
            setRetryTimes(3).
            setSleepTime(1000).
            setTimeOut(5000).
            addHeader("Accept-Encoding", "/").
            setUserAgent("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html）");

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    @Override
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()"));
//        if (page.getResultItems().get("name") == null) {
//            //skip this page
//            page.setSkip(true);
//        }
        page.addTargetRequests(page.getHtml().links().regex("http://zhibo.sogou.com/gameZone_英雄联盟.whtml").all());
        if (page.getUrl().regex("http://zhibo.sogou.com/gameZone_英雄联盟.whtml.*").match()) {
            // 是英雄联盟页面
            List<String> links = page.getHtml().links().regex("(/gameZone_英雄联盟\\.whtml\\?product=live&page=([1-9])&type=1&f=0#resultlist)").all();
//            List<String> links = page.getHtml().links().regex("(/gameZone_英雄联盟.whtml?product=live&page=2&type=1&f=0#resultlist)").all();
            page.addTargetRequests(links);
            page.putField("title", page.getHtml().xpath("[@id='targetContent']/div/div/p/a/@title").all());
            page.putField("page", page.getUrl().regex("(?<=page=)\\d").toString());
        }

        // 部分三：从页面发现后续的url地址来抓取
//        page.addTargetRequest(page.getHtml().links().regex("()").toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //从"https://github.com/code4craft"开始抓-5个线程
        Spider.create(new GithubRepoPageProcessor())
                .addUrl("https://zhibo.sogou.com/")
//                .addPipeline(new JsonFilePipeline(System.getProperty("user.dir") + "/result"))
                .thread(5)
                .run();
    }
}
