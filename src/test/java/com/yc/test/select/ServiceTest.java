package com.yc.test.select;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yc.mapper.UserMapper;
import com.yc.pojo.User;
import com.yc.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest
public class ServiceTest {
	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;
	@Test
	public void testGetOne() {
		LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
		wrapper.gt(User::getAge, 28);
		User one = userService.getOne(wrapper, false); // 第二参数指定为false,使得在查到了多行记录时,不抛出异常,而返回第一条记录
		System.out.println(one);
	}

	@Test
	public void testChain() {
		List<User> list = userService.lambdaQuery()
				.gt(User::getAge, 39)
				.likeRight(User::getName, "王")
				.list();
		list.forEach(System.out::println);
	}

	@Test
	public void testChain2() {
		userService.lambdaUpdate()
				.gt(User::getAge, 39)
				.likeRight(User::getName, "小")
				.set(User::getName, "小张")
				.set(User::getEmail, "123@qq.com").update();
	}

	@Test
	public void testChain3() {
		userService.lambdaUpdate()
				.like(User::getName, "青蛙")
				.remove();
	}
}
