package com.wx.ioc.wxdemo.controller;

import com.wx.ioc.wxdemo.entity.testentity.TestPostConstruct;
import com.wx.ioc.wxdemo.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjectName: wxdemo
 * @Package: com.wx.ioc.wxdemo.controller
 * @ClassName: AnnotationTest
 * @Description: java类作用描述
 * @Author: 焦关平
 * @CreateDate: 2018/8/31 20:19
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/31 20:19
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/annotation")
public class AnnotationTest {
    @Resource
    private TestService testService;


    @RequestMapping("test")
    public List<Test> list(){
        return testService.list();
    }

    @RequestMapping("index")
    public String index(){
        TestPostConstruct g =new  TestPostConstruct("生活的不容易啊");
        g.setValue("info");
        System.out.println(g.getValue());
        return "你好啊";
    }
    public String construct(){


        return "这是我的测试";
    }

    /**
     * 正则表达式
     * @param args
     */
    public static void main(String[] args) {
        String str = "hello.word.";
        Pattern pattern=Pattern.compile("hello.word.");
        Matcher matcher = pattern.matcher(str);
        System.out.println(matcher.matches());
        if (matcher.matches()){
            System.out.println("成功");
        }else {
            System.out.println("shibai ");
        }
    }
}
