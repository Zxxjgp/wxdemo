package com.wx.ioc.wxdemo.controller;

import com.wx.ioc.wxdemo.common.MessageType;
import com.wx.ioc.wxdemo.entity.AccessToken;
import com.wx.ioc.wxdemo.service.AccessTokens;
import com.wx.ioc.wxdemo.utils.MessageHandlerUtil;
import com.wx.ioc.wxdemo.utils.NetWorkHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.wx.ioc.wxdemo.utils.MessageHandlerUtil.buildTextMessage;

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
        return accessToken.getAccessToken();
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
            System.out.println(map);
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


    /**
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "send", method = RequestMethod.GET)
    public void sendMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = token();
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s", token);
        String toUser = "oeMVT0c8Lp01LFBMcBof9T62bAt4";
        String content  = "我在测试";

        String json = "{\"touser\": \""+toUser+"\",\"msgtype\": \"text\", \"text\": {\"content\": \""+content+"\"}}";
        netWorkHelper.connectWeiXinInterface(Url, json);
        /*System.out.println(s);*/

        response.getWriter().println(json);
    }

    public static String buildTextMessage() {

        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[oeMVT0c8Lp01LFBMcBof9T62bAt4]]></ToUserName>" +
                        "<FromUserName><![CDATA[gh_84cc2381bb1a]]></FromUserName>" +
                        "<CreateTime>1535165501</CreateTime>" +
                        "<MsgType><![CDATA[text]]></MsgType>" +
                            "<Content><![CDATA[11111111111111111]]></Content>" +
                        "</xml>");
    }
    /**
     * 向用户发送消息
     */
    @RequestMapping(value = "sendMessage", method = RequestMethod.GET)
    public void sendMessage( String content, String msgType){
        String token = token();
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s", token);
        String toUser = "oeMVT0c8Lp01LFBMcBof9T62bAt4";

        String json ="";


        String mediaId = "cBBNoABtEMws3YAoY5gr6lQH0JFcljNCe3ArUWnlN6bL5CmOsfF5VGJzpkysiWfF";

        String mediafId = "Qc8ZEIuXBK8QgRGhjoCNMFG_SjQfSiER9ACOWpRAqkhErbLfSRxHZn6koiPz-Qwt";
        String DS = "XpdxY3oyxJVSNJ3AGnUS0PWr4hZBSjNGKC1NcMUhoh8ouNbCtbmYn2RhqTcguiXL";
        String DSS = "VFLzI8jA6gJzTcvCmnBfV6vlw9WZml0Aoza-7VK7C-T6ZJT5iMEp2NB3aEUumkn8";
        String title = "test";
        String descri = "this is my video";
        if(msgType.equals("IMAGE")){

            json = "{\"touser\": \""+toUser+"\",\"msgtype\": \"image\", \"image\": {\"media_id\": \""+mediaId+"\"}}";

        }else if(msgType.equals("VOICE")){

            json = "{\"touser\": \""+toUser+"\",\"msgtype\": \"voice\", \"voice\": {\"media_id\": \""+mediafId+"\"}}";

        }
        else  if (msgType.equals("SHI")){
            json = "{\"touser\":\""+toUser+"\",\"msgtype\":\"video\",\"video\":{\"media_id\":\""+DS+"\",\"thumb_media_id\":\""+DSS+"\",\"title\":\""+title+"\",\"description\":\""+descri+"\"}}";
        }
        else if (msgType.equals("DH")){
            //发送图文消息（点击跳转到外链） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
            json = "{\"touser\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"msgtype\":\"news\",\"news\":{\"articles\":[{\"title\":\"Happy Day\",\"description\":\"Is Really A Happy Day\",\"url\":\"URL\",\"picurl\":\"PIC_URL\"},{\"title\":\"Happy Day\",\"description\":\"Is Really A Happy Day\",\"url\":\"URL\",\"picurl\":\"PIC_URL\"}]}}";
        }else if (msgType.equals("DHA")){
            //发送图文消息（点击跳转到图文消息页面） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
            json = "{\"touser\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"msgtype\":\"mpnews\",\"mpnews\":{\"media_id\":\"cBBNoABtEMws3YAoY5gr6lQH0JFcljNCe3ArUWnlN6bL5CmOsfF5VGJzpkysiWfF\"}}";
        }else if (msgType.equals("gj")){
            json = "{\"touser\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"msgtype\":\"mpnews\",\"mpnews\":{\"articles\":[{\"title\":\"Title\",\"thumb_media_id\":\"fpOPAa_m4aGJzZgp1b7dgs_jt1hFE8oDGmKaMdiOrUlwX3nUv8_4AOCKaXdp4-uk\",\"author\":\"Author\",\"content_source_url\":\"URL\",\"content\":\"Content\",\"digest\":\"Digest description\",\"show_cover_pic\":\"0\"},{\"title\":\"Title\",\"thumb_media_id\":\"fpOPAa_m4aGJzZgp1b7dgs_jt1hFE8oDGmKaMdiOrUlwX3nUv8_4AOCKaXdp4-uk\",\"author\":\"Author\",\"content_source_url\":\"URL\",\"content\":\"Content\",\"digest\":\"Digest description\",\"show_cover_pic\":\"0\"}]}}";

        }

       netWorkHelper.connectWeiXinInterface(Url, json);
/*        String token = token();
        String action = String.format("https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=%s",token);
        String json = "{\"articles\":[{\"thumb_media_id\":\"cBBNoABtEMws3YAoY5gr6lQH0JFcljNCe3ArUWnlN6bL5CmOsfF5VGJzpkysiWfF\",\"author\":\"xxx\",\"title\":\"Happy Day\",\"content_source_url\":\"www.qq.com\",\"content\":\"content\",\"digest\":\"digest\",\"show_cover_pic\":1},{\"thumb_media_id\":\"cBBNoABtEMws3YAoY5gr6lQH0JFcljNCe3ArUWnlN6bL5CmOsfF5VGJzpkysiWfF\",\"author\":\"xxx\",\"title\":\"Happy Day\",\"content_source_url\":\"www.qq.com\",\"content\":\"content\",\"digest\":\"digest\",\"show_cover_pic\":0}]}";
        NetWorkHelper netWorkHelper=  new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(action,json);*/
    }
    /**
     * 上传图文消息
     */
    @RequestMapping(value = "addTuWen", method = RequestMethod.POST)
    public void addTuWen(){
        String token = token();
        String action = String.format("https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=%s",token);
        String json = "{\"articles\":[{\"thumb_media_id\":\"cBBNoABtEMws3YAoY5gr6lQH0JFcljNCe3ArUWnlN6bL5CmOsfF5VGJzpkysiWfF\",\"author\":\"xxx\",\"title\":\"Happy Day\",\"content_source_url\":\"www.qq.com\",\"content\":\"content\",\"digest\":\"digest\",\"show_cover_pic\":1},{\"thumb_media_id\":\"cBBNoABtEMws3YAoY5gr6lQH0JFcljNCe3ArUWnlN6bL5CmOsfF5VGJzpkysiWfF\",\"author\":\"xxx\",\"title\":\"Happy Day\",\"content_source_url\":\"www.qq.com\",\"content\":\"content\",\"digest\":\"digest\",\"show_cover_pic\":0}]}";
        NetWorkHelper netWorkHelper=  new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(action,json);
    }



    /**
     * 获取微信服务器IP地址获取微信服务器的ip地址信息
     */

    @RequestMapping("getWxId")
    public void getWxId(){
        String token = token();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String httpsResponse = netWorkHelper.getHttpsResponse(url, "GET");
        System.out.println(httpsResponse);
    }

    /**
     * 客服管理
     */
    @RequestMapping(value = "addUserManager", method = RequestMethod.POST)
    public void userManager(){
        String token = token();
        String url = String.format("https://api.weixin.qq.com/customservice/kfaccount/add?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String json = "{\"kf_account\":\"jgpxx@test\",\"nickname\":\"客服2\",\"password\":\"123456\"}";
        netWorkHelper.connectWeiXinInterface(url,json);
    }

    /**
     * 消息的群发功能
     */
    @RequestMapping(value = "sendAll", method = RequestMethod.POST)
    public  void  sendAll(){
        String token = token();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String json = "{\"filter\":{\"is_to_all\":false,\"tag_id\":2},\"mpnews\":{\"media_id\":\"BrkWLPodFQHis0HeMhRpJFMU7E7F6cucboP963l3p6D31snAvY7lZQAJB0Ue8G25\"},\"msgtype\":\"mpnews\",\"send_ignore_reprint\":0}";
        netWorkHelper.connectWeiXinInterface(url,json);
    }

    /**
     * 设置行业信息
     */
    @RequestMapping(value = "setHangye", method = RequestMethod.POST)
    public void setHangye(){
        String token = token();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String json = "{\"industry_id1\":\"1\",\"industry_id2\":\"4\"}";
        netWorkHelper.connectWeiXinInterface(url,json);
    }

    /**
     * 得到该行业信息
     */
    @RequestMapping(value = "gettHangye", method = RequestMethod.GET)
    public void getHangYe(){
        String token = token();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String get = netWorkHelper.getHttpsResponse(url, "GET");
        System.out.println(get);
    }

    /**
     * 设置行业
     */
    @RequestMapping(value = "setHangyeMoBan", method = RequestMethod.POST)
    public void setHangyeMoBan(){
        String token = token();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String json = "{\"template_id_short\":\"TM00015\"}";
        netWorkHelper.connectWeiXinInterface(url,json);
    }

    /**
     * 得到该行业信息莫办消息
     */
    @RequestMapping(value = "gettHangyeMoBan", method = RequestMethod.GET)
    public void gettHangyeMoBan(){
        String token = token();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String get = netWorkHelper.getHttpsResponse(url, "GET");
        System.out.println(get);
    }

    /**
     * 发送魔板消息
     */
    @RequestMapping(value = "fasong", method = RequestMethod.POST)
    public  void setHangyeweMoBan(){
        String token = token();
        String url = String.format(" https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String json = "{\"touser\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"template_id\":\"p6dNxSlQCg3-rI0pVajl7pr-cPcLe4HWpmwWzDkYFzk\",\"url\":\"http://weixin.qq.com/download\",\"data\":{\"first\":{\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"巧克力\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\"39.8元\",\"color\":\"#173177\"},\"keyword3\":{\"value\":\"2014年9月22日\",\"color\":\"#173177\"},\"remark\":{\"value\":\"欢迎再次购买！\",\"color\":\"#173177\"}}}";
        netWorkHelper.connectWeiXinInterface(url,json);

    }
}
