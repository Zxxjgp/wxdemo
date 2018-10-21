package com.wx.ioc.wxdemo.dao;

import com.wx.ioc.wxdemo.controller.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Random;

/**
 * @ProjectName: wxdemo
 * @Package: com.wx.ioc.wxdemo.dao
 * @ClassName: TestDao
 * @Description: java类作用描述
 * @Author: 焦关平
 * @CreateDate: 2018/9/3 20:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/9/3 20:39
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Mapper
public interface TestDao {
    /**
     *
     * @return
     */
    List<Test> list();
/*    public static void main(String[] args) {
        String str ="f96e2e494b1a41f4852d7c4e6f0608d2";
        System.out.println(str.length());


    }*/

}
