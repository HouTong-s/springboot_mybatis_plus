package com.houtong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtong.dao.UserDao;
import com.houtong.domain.User;
import com.houtong.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
