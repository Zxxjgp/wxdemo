package com.wx.ioc.wxdemo.entity.testentity;

import com.wx.ioc.wxdemo.entity.Misic;
import net.sf.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * @ProjectName: wxdemo
 * @Package: com.wx.ioc.wxdemo.entity.testentity
 * @ClassName: Templete
 * @Description: java类作用描述
 * @Author: 焦关平
 * @CreateDate: 2018/8/28 9:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/28 9:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Setter
@Getter
public class Templete {
    private String touser;
    private String template_id;
    private String url;
    private TestMusic date;

    public static void main(String[] args) {
        Templete templete = new Templete();
        TestMusic testMusic =new TestMusic();
        User user = new User();
        user.setColor("大红色");
        user.setValue("123464654");
        testMusic.setOrderMoneySum(user);

        Riqi riqi = new Riqi();
        riqi.setColor("red");
        riqi.setValue("dahongse");
        testMusic.setOrderProductName(riqi);


        Remark remark = new Remark();
        remark.setColor("greend");
        remark.setColor("wer");
        testMusic.setRemark(remark);

        TemShuxin temShuxin = new TemShuxin();
        temShuxin.setValue("8888888888888");
        temShuxin.setColor("hpmngse");
        testMusic.setFirst(temShuxin);

        templete.setTemplate_id("12121");
        templete.setTouser("秦朗");
        templete.setUrl("www.baidu.com");
        templete.setDate(testMusic);
        JSONObject object = JSONObject.fromObject(templete);
        System.out.println(object.toString());
    }
}
