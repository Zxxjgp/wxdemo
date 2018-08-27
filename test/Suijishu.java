package com.wx.ioc.wxdemo.test;

import com.wx.ioc.wxdemo.utils.NetWorkHelper;

/**
 * @ProjectName: wxdemo
 * @Package: com.wx.ioc.wxdemo.test
 * @ClassName: Suijishu
 * @Description: java类作用描述
 * @Author: 焦关平
 * @CreateDate: 2018/8/23 15:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/23 15:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Suijishu {
    public static void main(String[] args) {
      /*  String a = "1";
        String b = "1";
        String c = "11";
        System.out.println(c.equals(a+b));*/
       // String token = "";
        //String type = "thumb";
        NetWorkHelper n =  new NetWorkHelper();
       // String Url = String.format("https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=%s&type=%s", token,type);
        String j = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=13_Q-hodd12iAX_Sma3fLDZgegmijnCq43pZjKQLJ0nLbE6bEvHTtguD_ZCxA_ovGAvzga1UnElxHsU3rNLvjYAgsNdO4qBbPL7jNsQS8cDLrNGs1SPej7IH_6dPicXOJdADABSN&type=thumb";
        String get = n.getHttpsResponse(j, "GET");
        System.out.println(get);
    }
}
