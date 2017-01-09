package org.luotian.open.configuration.service.impl;

import org.luotian.open.configuration.dao.HelloDao;
import org.luotian.open.configuration.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@Service
public class HelloServiceImpl implements HelloService{

    @Autowired
    private HelloDao helloDao;

    public String test(String message) {
        return helloDao.test(message);
    }
}
