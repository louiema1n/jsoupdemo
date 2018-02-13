package com.lm.jsoupdemo.Utils;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * @description 结果处理
 * @author&date Created by louiemain on 2018-02-13 15:50
 */
public class DataHandlePiple implements PageModelPipeline {

    public DataHandlePiple() {
    }

    @Override
    public void process(Object o, Task task) {
        System.out.println(o.toString());
    }
}
