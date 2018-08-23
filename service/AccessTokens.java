package com.wx.ioc.wxdemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wx.ioc.wxdemo.entity.AccessToken;
import com.wx.ioc.wxdemo.utils.NetWorkHelper;
import org.springframework.stereotype.Service;


/**
 * @ProjectName: wxdemo
 * @Package: com.wx.ioc.wxdemo.service
 * @ClassName: AccessToken
 * @Description: java类作用描述
 * @Author: 焦关平
 * @CreateDate: 2018/8/23 9:15
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/23 9:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class AccessTokens {

    /**
     * 获取token
     * @param appId
     * @param appSecret
     * @return
     */
    public AccessToken getAccessToken(String appId, String appSecret) {
        NetWorkHelper netHelper = new NetWorkHelper();
        /**
         * 接口地址为https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET，其中grant_type固定写为client_credential即可。
         */
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId, appSecret);
        //此请求为https的get请求，返回的数据格式为{"access_token":"ACCESS_TOKEN","expires_in":7200}
        String result = netHelper.getHttpsResponse(Url, "");
        System.out.println("获取到的access_token="+result);
        //使用FastJson将Json字符串解析成Json对象
        JSONObject json = JSON.parseObject(result);
        AccessToken token = new AccessToken();
        token.setAccessToken(json.getString("access_token"));
        token.setExpiresin(json.getInteger("expires_in"));
        return token;
    }

}
