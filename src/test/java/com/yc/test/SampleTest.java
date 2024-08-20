package com.yc.test;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.mapper.UserMapper;
import com.yc.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class SampleTest {
	@Autowired
	private UserMapper mapper;
	@Test
	public void testSelect() {
		List<User> list = mapper.selectList(null);
		assertEquals(5, list.size());
		list.forEach(System.out::println);
	}

	@Test
	public void test3() {
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.select("id","name","email").like("name","黄");
		List<Map<String, Object>> maps = mapper.selectMaps(wrapper);
		maps.forEach(System.out::println);
	}

	// 按照直属上级进行分组，查询每组的平均年龄，最大年龄，最小年龄
	/**
	 select avg(age) avg_age ,min(age) min_age, max(age) max_age from user group by manager_id having sum(age) < 500;
	 **/

	@Test
	public void test4() {
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.select("manager_id", "avg(age) avg_age", "min(age) min_age", "max(age) max_age")
				.groupBy("manager_id").having("sum(age) < {0}", 500);
		List<Map<String, Object>> maps = mapper.selectMaps(wrapper);
		maps.forEach(System.out::println);
	}

	@Test
	public void test5() {//只保留查询到的数据的第一列
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.select("id", "name").like("name", "黄");
		List<Object> objects = mapper.selectObjs(wrapper);
		objects.forEach(System.out::println);
	}


	@Test
	public void test6() {
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.like("name", "黄");

		long count = mapper.selectCount(wrapper);
		System.out.println(count);
	}

}
