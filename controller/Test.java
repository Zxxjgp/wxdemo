package com.wx.ioc.wxdemo.controller;

import com.wx.ioc.wxdemo.entity.AccessToken;
import com.wx.ioc.wxdemo.service.AccessTokens;
import com.wx.ioc.wxdemo.utils.MessageHandlerUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: wxdemo
 * @Package: com.wx.ioc.wxdemo.controller
 * @ClassName: Test
 * @Description: java类作用描述
 * @Author: 焦关平
 * @CreateDate: 2018/8/22 9:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/22 9:42
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("index")
public class Test {

    @Resource
    private AccessTokens accessTokens;
    @RequestMapping("index")
    public String index(HttpServletRequest request) {

        return "nihao";
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @RequestMapping(value = "getWeiXinMethod", method = RequestMethod.GET)
    public void getWeiXinMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean validate = validate(request);
        if (validate) {
            response.getWriter().write(request.getParameter("echostr"));
            response.getWriter().close();
        }

    }

    private boolean validate(HttpServletRequest req) throws IOException {
        //接受参数
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        List<String> list = new ArrayList<String>();
        //见这个添加进去进行排序
        list.add("jgpxx");
        list.add(timestamp);
        list.add(nonce);
        Collections.sort(list);

        //将这添加进行字符串进行加密
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            s += (String) list.get(i);
        }

        System.out.println(encode("SHA1", s));
        System.out.println(signature);

        //进行SHA1的加密
        if (encode("SHA1", s).equalsIgnoreCase(signature)) {
            return true;
        } else {
            return false;
        }
    }

    public static String encode(String algorithm, String str) {
        if (str == null) {
            return null;
        }
        try {
            //Java自带的加密类
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            //转为byte
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch ( Exception e ) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        System.out.println(buf.toString());
        return buf.toString();

    }

    /**
     * token的获取
     * @return
     */
    @RequestMapping("getToken")
    private String token(){
        String appId = "wxb97266dfb271f554";
        String appSecret = "d9fa2b5707d343522dbd6e53841cbcd8";
        AccessToken accessToken = accessTokens.getAccessToken(appId, appSecret);
        return accessToken.getAccessToken()+accessToken.getExpiresin();
    }

    @RequestMapping(value = "getWeiXinMethod", method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("请求进入");
        String responseMessage;
        try {
            //解析微信发来的请求,将解析后的结果封装成Map返回
            Map<String,String> map = MessageHandlerUtil.parseXml(request);
            System.out.println("开始构造响应消息");
            responseMessage = MessageHandlerUtil.buildResponseMessage(map);
            System.out.println(responseMessage);
            if(responseMessage.equals("")){
                responseMessage ="未正确响应";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生异常："+ e.getMessage());
            responseMessage ="未正确响应";
        }
        //发送响应消息
        response.getWriter().println(responseMessage);
    }
}
