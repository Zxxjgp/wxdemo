package com.wx.ioc.wxdemo.controller;

import com.wx.ioc.wxdemo.common.MessageType;
import com.wx.ioc.wxdemo.entity.AccessToken;
import com.wx.ioc.wxdemo.service.AccessTokens;
import com.wx.ioc.wxdemo.utils.MessageHandlerUtil;
import com.wx.ioc.wxdemo.utils.NetWorkHelper;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
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
        String json = "{\"filter\":{\"is_to_all\":true,\"tag_id\":3},\"text\":{\"content\":\"我是测试啊\"},\"msgtype\":\"text\"}";
       // String json = "{\"filter\":{\"is_to_all\":true,\"tag_id\":5},\"mpnews\":{\"media_id\":\"DRvUUFbSAOx23VGADGZqMfgjcWmA0O2f1SDwtPxKPSn4EQjSTf_sOMAmwkn_GWHN\"},\"msgtype\":\"mpnews\",\"send_ignore_reprint\":0}";
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
     * 发送模板消息
     * 发送魔板消息
     */
    @RequestMapping(value = "fasong", method = RequestMethod.POST)
    public  void setHangyeweMoBan(){
        String token = token();
        String url = String.format(" https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
       // String json = "{\"touser\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"template_id\":\"p6dNxSlQCg3-rI0pVajl7pr-cPcLe4HWpmwWzDkYFzk\",\"url\":\"http://weixin.qq.com/download\",\"data\":{\"first\":{\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"巧克力\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\"39.8元\",\"color\":\"#173177\"},\"keyword3\":{\"value\":\"2014年9月22日\",\"color\":\"#173177\"},\"remark\":{\"value\":\"欢迎再次购买！\",\"color\":\"#173177\"}}}";
        String json = "{\"touser\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"template_id\":\"p6dNxSlQCg3-rI0pVajl7pr-cPcLe4HWpmwWzDkYFzk\",\"url\":\"https://mp.weixin.qq.com/mp/subscribemsg?action=get_confirm&appid=wxb97266dfb271f554&scene=1000&template_id=XlA_F9qdil8GJLLp5wMFfe_-n1KOaJnArdk6ZqaPbCE&redirect_url=http://h8z8qr.natappfree.cc&reserved=test#wechat_redirect\",\"data\":{\"first\":{\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},\"orderProductName\":{\"value\":\"巧克力\",\"color\":\"#173177\"},\"orderMoneySum\":{\"value\":\"39.8元\",\"color\":\"#173177\"},\"keyword3\":{\"value\":\"2014年9月22日\",\"color\":\"#173177\"},\"Remark\":{\"value\":\"欢迎再次购买！\",\"color\":\"#173177\"}}}";
        netWorkHelper.connectWeiXinInterface(url,json);

    }

    /**
     * 一次性订阅消息
     */
    @RequestMapping(value = "buyMessae", method = RequestMethod.GET)
    public void buyMessae(){
        String url = "https://mp.weixin.qq.com/mp/subscribemsg?action=get_confirm&appid=wxb97266dfb271f554&scene=1000&template_id=XlA_F9qdil8GJLLp5wMFfe_-n1KOaJnArdk6ZqaPbCE&redirect_url=http://h8z8qr.natappfree.cc&reserved=test#wechat_redirect";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String post = netWorkHelper.getHttpsResponse(url, "GET");
        System.out.println(post);
    }
    /**
     * 获取code
     */
    @RequestMapping(value = "huoquCode", method = RequestMethod.GET)
    public  void huoquCode(){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxb97266dfb271f554&secret=d9fa2b5707d343522dbd6e53841cbcd8&code=0818DbAa2C4s9P0hPsza2jhtAa28DbAL&grant_type=authorization_code";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String post = netWorkHelper.getHttpsResponse(url, "GET");
        System.out.println(post);
    }
    /**
     * 获取用户信息
     */
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    public void getUserInfo(){
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=13_juo20hZjcvkPh5iPHAVHQ6YvwxYWp_wZ3sGH4KBGwf0hwpOPlmm0APM8VCEYZoBt0jkcfa5tAc7F05WR4A9hjA5DNsEijeAXywqr4v64L7s&openid=oeMVT0c8Lp01LFBMcBof9T62bAt4&lang=zh_CN";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String post = netWorkHelper.getHttpsResponse(url, "GET");
        System.out.println(post);
    }
    /**
     *
     * 用户标签管理
     */
    @RequestMapping(value = "userLableManager", method = RequestMethod.POST)
    public void userLableManager(){
        String json = "{   \"tag\" : {     \"name\" : \"广东\",\"count\":5   } }";
        String token = token();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/tags/create?access_token=%s",token);
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/tags/get?access_token=%s",token);

        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(url,json);

    }
    @RequestMapping(value = "getUserLableInfo", method = RequestMethod.GET)
    private void getLableUserInfo(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/tags/get?access_token=%s",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String response = netWorkHelper.getHttpsResponse(actionUrl, "GET");
        System.out.println(response);
    }
    /**
     * 更新标签update删除delete
     */
    @RequestMapping(value = "updateUserLableInfo", method = RequestMethod.POST)
    public void updateUserLableInfo(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/tags/update?access_token=%s",token);
        String json = "{\"tag\":{\"id\":100,\"name\":\"广东人\"}}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(actionUrl,json);
    }
    /**
     * 获取粉丝
     */
    @RequestMapping(value = "getInfo", method = RequestMethod.GET)
    public void getInfo(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=%s",token);
        String json = "{\"tagid\":100,\"next_openid\":\"\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(actionUrl,json);

    }

    /**
     * 批量为用户打标签和删除
     * https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=ACCESS_TOKEN 这个表示批量的取消
     * 格式都是一样的
     *
     * {   "openid_list" : [//粉丝列表
     * "ocYxcuAEy30bX0NXmGn4ypqx3tI0",
     * "ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"   ],
     * "tagid" : 134 }
     *
     *
     */
    @RequestMapping(value = "setLable", method = RequestMethod.POST)
    public  void setLable(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=%s",token);
        String json = "{\"openid_list\":[\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"oeMVT0aOF-CzWeCB4aCUIBBb8eyQ\"],\"tagid\":100}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(actionUrl,json);
    }
    /**
     * 获取用户身上的标签列表
     *
     *
     * {   "openid" : "ocYxcuBt0mRugKZ7tGAHPnUaOW7Y" }  opendid这个表示用户的唯一标识符
     *
     * https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=ACCESS_TOKEN
     */
    @RequestMapping(value = "getlableinfo", method = RequestMethod.POST)
    public void getlableinfo(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=%s",token);
        String json = "{\"openid\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(actionUrl,json);
    }

    /**
     * 设置用户的备注名称
     */

    @RequestMapping(value = "setUserRemark", method = RequestMethod.POST)
    public void setUserRemark(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=%s",token);
        String json = "{\"openid\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"remark\":\"pangzi\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(actionUrl,json);
    }
    /**
     * 单个用户信息的获取
     *
     * 接口调用请求说明
     * http请求方式: GET
     * https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
     * 获取用户的基本信息
     *
     * 返回参数
     *
     *  这里就可以获取到我们上面得到的用户的remark
     */
    @RequestMapping(value = "getBaseInfo", method = RequestMethod.GET)
    public void getBaseInfo(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=oeMVT0c8Lp01LFBMcBof9T62bAt4&lang=zh_CN",token);
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        System.out.println(actionUrl);
        String get = netWorkHelper.getHttpsResponse(actionUrl, "GET");
        System.out.println(get);
    }
    /**
     * 批量的获取用户信息
     * http请求方式: POST
     * https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN
     *
     * 获取的json包
     * {
     *     "user_list": [
     *         {
     *             "openid": "otvxTs4dckWG7imySrJd6jSi0CWE",
     *             "lang": "zh_CN"
     *         },
     *         {
     *             "openid": "otvxTs_JZ6SEiP0imdhpi50fuSZg",
     *             "lang": "zh_CN"
     *         }
     *     ]
     * }
     *
     */
    @RequestMapping(value = "getBaseInfoList", method = RequestMethod.POST)
    public void getBaseInfoList(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=%s",token);
        String json = "{\"user_list\":[{\"openid\":\"oeMVT0c8Lp01LFBMcBof9T62bAt4\",\"lang\":\"zh_CN\"},{\"openid\":\"oeMVT0Ru0PM934dMLjQuUPtH37EQ\",\"lang\":\"zh_CN\"}]}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(actionUrl,json);
    }
    /**
     * 获取用户列表
     * http请求方式: GET（请使用https协议）
     * https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID
     *
     * 参数说明   next_openid	是	第一个拉取的OPENID，不填默认从头开始拉取
     *
     *
     */
    @RequestMapping(value = "getUserList", method = RequestMethod.GET)
    public void getUserList(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s",token);
        String json = "";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String get = netWorkHelper.getHttpsResponse(actionUrl, "GET");
        System.out.println(get);
    }

    /**
     * 用户的黑名单处理
     * 1，获取公众号的黑名单列表
     *   http请求方式：POST（请使用https协议）
     *   https://api.weixin.qq.com/cgi-bin/tags/members/getblacklist?access_token=ACCESS_TOKEN
     *   {"begin_openid":"OPENID1"
     *    }
     *
     *   2,用户的拉黑
     *   http请求方式：POST（请使用https协议）
     *   https://api.weixin.qq.com/cgi-bin/tags/members/batchblacklist?access_token=ACCESS_TOKEN
     *   json格式
     *   {
     *  "openid_list":["OPENID1”,” OPENID2”]
     * }
     *   3，取消拉黑
     *   http请求方式：POST（请使用https协议）
     * https://api.weixin.qq.com/cgi-bin/tags/members/batchunblacklist?access_token=ACCESS_TOKEN
     *
     * {
     *  "openid_list":["OPENID1”,” OPENID2”]
     * }
     *
     *
     *
     *
     */
    @RequestMapping(value = "laheilist", method = RequestMethod.POST)

    public void laheilist(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/tags/members/getblacklist?access_token=%s",token);
        String json = "{\"begin_openid\":\"\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(actionUrl,json);
        //用户的拉黑
/*        String actionUrl1 = String.format("https://api.weixin.qq.com/cgi-bin/tags/members/batchblacklist?access_token=%s",token);
        String json1 = "{\"openid_list\":[\"oeMVT0Ru0PM934dMLjQuUPtH37EQ\"]}";
        netWorkHelper.connectWeiXinInterface(actionUrl1,json1);*/

        //取消用户拉黑
/*        String actionUrl2 = String.format("https://api.weixin.qq.com/cgi-bin/tags/members/batchunblacklist?access_token=%s",token);
        String json2 = "{\"openid_list\":[\"oeMVT0Ru0PM934dMLjQuUPtH37EQ\"]}";
        netWorkHelper.connectWeiXinInterface(actionUrl2,json2);*/

    }
    /**
     * 微信用户请求创建二维码
     * 1 ， 生成临时二维码
     * http请求方式: POST
     * URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
     * POST数据格式：json
     * POST数据例子：{"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
     *
     * 或者也可以使用以下POST数据创建字符串形式的二维码参数：
     * {"expire_seconds": 604800, "action_name": "QR_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
     *
     * 2，创建永久二维码
     * http请求方式: POST
     * URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
     * POST数据格式：json
     * POST数据例子：{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}}
     *
     * 或者也可以使用以下POST数据创建字符串形式的二维码参数：
     * {"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
     *
     *
     * 过ticket换取二维码 ticket:gQGv8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyNWFYY1JEVTVkamgxRWRxZk5yY28AAgSNX4ZbAwSAOgkA
     */

    @RequestMapping(value = "qrcode", method = RequestMethod.POST)
    public void qrcode(){
        String token = token();
        String actionUrl = String.format("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s",token);
        String json = "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"test\"}}}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(actionUrl,json);
    }

    /**
     * 换区二维码gQGv8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyNWFYY1JEVTVkamgxRWRxZk5yY28AAgSNX4ZbAwSAOgkA
     * 这个直接返回一个页面，我们在开发的时候直接用这个连接得到我们自己的二维码
     *
     */
    @RequestMapping(value = "getqrcode", method = RequestMethod.GET)
    public void getqrcode(){
        String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGv8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyNWFYY1JEVTVkamgxRWRxZk5yY28AAgSNX4ZbAwSAOgkA";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String get = netWorkHelper.getHttpsResponse(url, "GET");
        System.out.println(get);
    }
    /**
     * 长短连接的改变（可以进行很多的连接转换）
     * http请求方式: POST
     * https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN
     * long_url 这个是我的需要转化的地址，而会在我的返回结果里得到想想要的短连接
     * 返回结果如下所示：
     *    请求返回结果:{"errcode":0,"errmsg":"ok","short_url":"https:\/\/w.url.cn\/s\/ABBlyvT"}
     *
     */
    @RequestMapping(value = "longLink", method = RequestMethod.POST)
    public void longLink(){
        String token = token();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/shorturl?access_token=%s",token);
        String json = "{\"action\":\"long2short\",\"long_url\":\"https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGv8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyNWFYY1JEVTVkamgxRWRxZk5yY28AAgSNX4ZbAwSAOgkA\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(url,json);
    }
    /**
     * 微信认证时间的推送
     * 事件的推送会推送到开发者的服务中心
     * 这个都会推送到你开的时候填写的url上面，你的的这个url会，干吼需要你去解析得到相对应的结果
     */

    /**
     * 用户的数据分析（请求必须是以POST）
     * 用户分析数据接口
     * 接口名称	                     最大时间跨度	     接口调用地址（必须使用https）
     * 获取用户增减数据（getusersummary）	7	         https://api.weixin.qq.com/datacube/getusersummary?access_token=ACCESS_TOKEN
     * 获取累计用户数据（getusercumulate）	7	         https://api.weixin.qq.com/datacube/getusercumulate?access_token=ACCESS_TOKEN
     *
     *
     *
     *
     * 返回参数说明
     *  ref_date	数据的日期
     *  user_source	用户的渠道，数值代表的含义如下： 0代表其他合计 1代表公众号搜索 17代表名片分享 30代表扫描二维码 43代表图文页右上角菜单 51代表支付后关注（在支付完成页） 57代表图文页内公众号名称 75代表公众号文章广告 78代表朋友圈广告
     *  new_user	新增的用户数量
     *  cancel_user	取消关注的用户数量，new_user减去cancel_user即为净增用户数量
     *  cumulate_user	总用户量
     *
     */
    @RequestMapping(value = "getUserAnalisis", method = RequestMethod.POST)
    public void getUserAnalisis(){
        String token = token();
        String url1 = String.format("https://api.weixin.qq.com/datacube/getusersummary?access_token=%s",token);
        String url2 = String.format("https://api.weixin.qq.com/datacube/getusercumulate?access_token=%s",token);
        String json = "{\"begin_date\":\"2018-08-27\",\"end_date\":\"2018-08-28\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(url1,json);
        netWorkHelper.connectWeiXinInterface(url2,json);
    }

    /**
     * 图文消息数据接口
     *  这里只测试一个
     *
     *
     接口名称	                            最大时间跨度	  接口调用地址（必须使用https）
     获取图文群发每日数据（getarticlesummary）	1          https://api.weixin.qq.com/datacube/getarticlesummary?access_token=ACCESS_TOKEN
     获取图文群发总数据（getarticletotal）	    1	       https://api.weixin.qq.com/datacube/getarticletotal?access_token=ACCESS_TOKEN
     获取图文统计数据（getuserread）	        3	       https://api.weixin.qq.com/datacube/getuserread?access_token=ACCESS_TOKEN
     获取图文统计分时数据（getuserreadhour）	    1          https://api.weixin.qq.com/datacube/getuserreadhour?access_token=ACCESS_TOKEN
     获取图文分享转发数据（getusershare）	    7	       https://api.weixin.qq.com/datacube/getusershare?access_token=ACCESS_TOKEN
     获取图文分享转发分时数据（getusersharehour）	1          https://api.weixin.qq.com/datacube/getusersharehour?access_token=ACCESS_TOKEN
     */

    @RequestMapping(value = "getUserTuwen", method = RequestMethod.POST)
    public void getUserTuwen(){
        String token = token();
        String url1 = String.format("https://api.weixin.qq.com/datacube/getuserread?access_token=%s",token);
        String url2 = String.format("https://api.weixin.qq.com/datacube/getusershare?access_token=%s",token);
        String json = "{\"begin_date\":\"2018-08-27\",\"end_date\":\"2018-08-28\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(url1,json);
        netWorkHelper.connectWeiXinInterface(url2,json);
    }

    /**
     *消息分析数据接口
     * 测试部分数据
     *
     *
     接口名称	最大时间跨度	接口调用地址（必须使用https）
     获取消息发送概况数据（getupstreammsg）	7	https://api.weixin.qq.com/datacube/getupstreammsg?access_token=ACCESS_TOKEN
     获取消息分送分时数据（getupstreammsghour）	1	https://api.weixin.qq.com/datacube/getupstreammsghour?access_token=ACCESS_TOKEN
     获取消息发送周数据（getupstreammsgweek）	30	https://api.weixin.qq.com/datacube/getupstreammsgweek?access_token=ACCESS_TOKEN
     获取消息发送月数据（getupstreammsgmonth）	30	https://api.weixin.qq.com/datacube/getupstreammsgmonth?access_token=ACCESS_TOKEN
     获取消息发送分布数据（getupstreammsgdist）	15	https://api.weixin.qq.com/datacube/getupstreammsgdist?access_token=ACCESS_TOKEN
     获取消息发送分布周数据（getupstreammsgdistweek）	30	https://api.weixin.qq.com/datacube/getupstreammsgdistweek?access_token=ACCESS_TOKEN
     获取消息发送分布月数据（getupstreammsgdistmonth）	30	https://api.weixin.qq.com/datacube/getupstreammsgdistmonth?access_token=ACCESS_TOKEN
     *
     *
     */
    @RequestMapping(value = "getUserText", method = RequestMethod.POST)
    public void getUserText(){
        String token = token();
        String url1 = String.format("https://api.weixin.qq.com/datacube/getupstreammsg?access_token=%s",token);
        String url2 = String.format("https://api.weixin.qq.com/datacube/getupstreammsgdistweek?access_token=%s",token);
        String url3 = String.format("https://api.weixin.qq.com/datacube/getupstreammsgweek?access_token=%s",token);
        String url4 = String.format("https://api.weixin.qq.com/datacube/getupstreammsgdist?access_token=%s",token);
        String json = "{\"begin_date\":\"2018-08-24\",\"end_date\":\"2018-08-28\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(url1,json);
        netWorkHelper.connectWeiXinInterface(url2,json);
        netWorkHelper.connectWeiXinInterface(url4,json);
        netWorkHelper.connectWeiXinInterface(url3,json);
    }

    /**
     *接口分析数据接口
     * 测试
     *
     *
     *
     *
     接口名称	最大时间跨度	接口调用地址（必须使用https）
     获取接口分析数据（getinterfacesummary）	30	https://api.weixin.qq.com/datacube/getinterfacesummary?access_token=ACCESS_TOKEN
     获取接口分析分时数据（getinterfacesummaryhour）	1	https://api.weixin.qq.com/datacube/getinterfacesummaryhour?access_token=ACCESS_TOKEN
     */
    @RequestMapping(value = "getWxInterface", method = RequestMethod.POST)
    public void getWxInterface(){
        String token = token();
        String url1 = String.format("https://api.weixin.qq.com/datacube/getinterfacesummary?access_token=%s",token);
        String url2 = String.format("https://api.weixin.qq.com/datacube/getinterfacesummaryhour?access_token=%s",token);
        String json = "{\"begin_date\":\"2018-08-24\",\"end_date\":\"2018-08-28\"}";
        String json2 = "{\"begin_date\":\"2018-08-28\",\"end_date\":\"2018-08-28\"}";
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        netWorkHelper.connectWeiXinInterface(url1,json);
        netWorkHelper.connectWeiXinInterface(url2,json2);
    }

}
