package com.yc.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.mapper.UserMapper;
import com.yc.pojo.User;
import com.yc.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
