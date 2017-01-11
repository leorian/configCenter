package org.luotian.open.configuration.service.impl;

import javax.annotation.Resource;

import org.luotian.open.configuration.dao.IUserDao;
import org.luotian.open.configuration.entity.User;
import org.luotian.open.configuration.service.IUserService;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl implements IUserService {
	@Resource
	private IUserDao userDao;

	public User getUserById(int userId) {
		// TODO Auto-generated method stub
		return this.userDao.selectByPrimaryKey(userId);
	}

}
