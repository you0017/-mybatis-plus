package com.yc.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> { }
