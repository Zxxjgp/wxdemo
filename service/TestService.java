package com.wx.ioc.wxdemo.service;

import com.wx.ioc.wxdemo.controller.Test;
import com.wx.ioc.wxdemo.dao.TestDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ProjectName: wxdemo
 * @Package: com.wx.ioc.wxdemo.service
 * @ClassName: TestService
 * @Description: java类作用描述
 * @Author: 焦关平
 * @CreateDate: 2018/9/3 20:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/9/3 20:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class TestService {
    @Resource
    private TestDao testDao;

    public List<Test> list(){
        return testDao.list();

    }
}
