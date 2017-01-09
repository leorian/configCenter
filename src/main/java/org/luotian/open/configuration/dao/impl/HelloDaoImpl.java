package org.luotian.open.configuration.dao.impl;

import org.luotian.open.configuration.dao.HelloDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@Repository
public class HelloDaoImpl implements HelloDao{
    public String test(String message) {
        return "你好, " + message;
    }
}
