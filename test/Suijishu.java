package com.wx.ioc.wxdemo.test;

import com.wx.ioc.wxdemo.utils.Identities;
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

        System.out.println(Identities.uuid());
        System.out.println(Identities.uuid2());
    }
}
