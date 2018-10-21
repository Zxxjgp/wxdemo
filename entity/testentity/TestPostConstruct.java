package com.wx.ioc.wxdemo.entity.testentity;

import lombok.Builder;
import lombok.Data;

import javax.annotation.PostConstruct;

/**
 * @ProjectName: wxdemo
 * @Package: com.wx.ioc.wxdemo.entity.testentity
 * @ClassName: TestPostConstruct
 * @Description: java类作用描述
 * @Author: 焦关平
 * @CreateDate: 2018/8/31 20:24
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/31 20:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
public class TestPostConstruct {
    private String value;

    public TestPostConstruct(String value) {
        this.value = value;
    }
    @PostConstruct
    public void init() {
        System.out.println("INIT|pre-init()|value = " + value);
        value = "set in init()";
        System.out.println("INIT|post-init()|value = " + value);
    }

}
