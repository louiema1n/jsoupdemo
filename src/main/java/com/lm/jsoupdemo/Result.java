package com.lm.jsoupdemo;

import com.lm.jsoupdemo.Utils.DataHandlePiple;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.JsonFilePageModelPipeline;

/**
 * @description
 * @author&date Created by louiemain on 2018-02-13 10:10
 */
@HelpUrl("http://zhibo.sogou.com/gameZone_英雄联盟.whtml")
@TargetUrl("http://zhibo.sogou.com/gameZone_英雄联盟.whtml\\?product=live&page=([1-9])&type=1&f=0#resultlist")
@ExtractBy(value = "[@id='targetContent']/div[@class='item game_right_dirk']", multi = true)
public class Result {

    @ExtractByUrl("(?<=page=)\\d")
    private int page;

    @ExtractBy(value = "div/p/a/@title")
    private String res;

    @ExtractBy(value = "div/p/a/@href")
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return "Result{" +
                "page=" + page +
                ", res='" + res + '\'' +
                ", href='" + href + '\'' +
                '}';
    }

    public static void main(String[] args) {
        OOSpider.create(Site.me()
                .setRetryTimes(3).
                setSleepTime(1000).
                setTimeOut(5000).
                addHeader("Accept-Encoding", "/").
                setUserAgent("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html）"), new DataHandlePiple(), Result.class)
                .addUrl("https://zhibo.sogou.com/")
                .thread(5)
                .run();
    }
}
