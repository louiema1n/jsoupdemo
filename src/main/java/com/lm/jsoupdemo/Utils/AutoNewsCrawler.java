package com.lm.jsoupdemo.Utils;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * @description
 * @author&date Created by louiemain on 2018-02-11 16:50
 */
public class AutoNewsCrawler extends BreadthCrawler {
    // 构造函数
    public AutoNewsCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        // 起始页面
        this.addSeed("http://news.163.com/");
        /*抓取链接(-.*) http://news.hfut.edu.cn/show-xxxxxx.html*/
//        this.addRegex("http://news.hfut.edu.cn/show-.*html");
        this.addRegex("http://news.163.com/.*html");
        /*不抓取(-.*.*) jpg|png|gif*/
        this.addRegex("-.*\\.(jpg|png|gif).*");
        /*不抓取 #*/
        this.addRegex("-.*#.*");
        // 设置线程数
        setThreads(5);
        // 设置线程间等待时间
        getConf().setExecuteInterval(1000);
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String url = page.url();
        /*if page is news page*/
        if (page.matchUrl("http://news.163.com/.*html")) {

            /*extract title and content of news by css selector*/
            String title = page.select("#targetContent>div:nth-child(1)>div>p>a").text();
            String content = page.select("#targetContent>div:nth-child(1)>div>p>a").attr("href");

            System.out.println("URL:\n" + url);
            System.out.println("title:\n" + title);
            System.out.println("content:\n" + content);

            /*If you want to add urls to crawl,add them to nextLink*/
            /*WebCollector automatically filters links that have been fetched before*/
            /*If autoParse is true and the link you add to nextLinks does not match the
              regex rules,the link will also been filtered.*/
            //next.add("http://xxxxxx.com");
        }
    }

    public static void main(String[] args) throws Exception {
        AutoNewsCrawler crawler = new AutoNewsCrawler("crawl", true);
        // 开启深度为n爬虫
        crawler.start(2);
    }
}
