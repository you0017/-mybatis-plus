package com.yc.test.select;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yc.mapper.UserMapper;
import com.yc.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ConditionTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        // 案例先展示需要完成的SQL语句，后展示Wrapper的写法

        // 1. 名字中包含佳，且年龄小于25
        // SELECT * FROM user WHERE name like '%佳%' AND age < 25
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<User> lambda = new LambdaQueryWrapper<>();
        wrapper.like("name", "佳").lt("age", 25);
        lambda.like(User::getName, "佳").lt(User::getAge, 25);
        List<User> users = userMapper.selectList(wrapper);


        // 下面展示SQL时，仅展示WHERE条件；展示代码时, 仅展示Wrapper构建部分
        // 2. 姓名为黄姓，且年龄大于等于20，小于等于40，且email字段不为空
        // name like '黄%' AND age BETWEEN 20 AND 40 AND email is not null
        wrapper.likeRight("name", "黄").between("age", 20, 40).isNotNull("email");
        lambda.likeRight(User::getName, "黄").between(User::getAge, 20, 40).isNotNull(User::getEmail);

        // 3. 姓名为黄姓，或者年龄大于等于40，按照年龄降序排列，年龄相同则按照id升序排列
        // name like '黄%' OR age >= 40 order by age desc, id asc
        wrapper.likeRight("name", "黄").or().ge("age", 40).orderByDesc("age").orderByAsc("id");
        lambda.likeRight(User::getName, "黄").or(i -> i.ge(User::getAge, 40)).orderByDesc(User::getAge).orderByAsc(User::getId);

        // 4.创建日期为2021年3月22日，并且直属上级的名字为李姓
        // date_format(create_time,'%Y-%m-%d') = '2021-03-22' AND manager_id IN (SELECT id FROM user WHERE name like '李%')
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", "2021-03-22")  // 建议采用{index}这种方式动态传参, 可防止SQL注入
                .inSql("manager_id", "SELECT id FROM user WHERE name like '李%'");
        lambda.apply("date_format(create_time, '%Y-%m-%d') = {0}", "2021-03-22")
                .inSql(User::getManagerId, "SELECT id FROM user WHERE name like '李%'");
        // 上面的apply, 也可以直接使用下面这种方式做字符串拼接，但当这个日期是一个外部参数时，这种方式有SQL注入的风险
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = '2021-03-22'");
        lambda.apply("date_format(create_time, '%Y-%m-%d') = '2021-03-22'");

        // 5. 名字为王姓，并且（年龄小于40，或者邮箱不为空）
        // name like '王%' AND (age < 40 OR email is not null)
        wrapper.likeRight("name", "王").and(q -> q.lt("age", 40).or().isNotNull("email"));
        lambda.likeRight(User::getName,"王").and(q -> q.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        // 6. 名字为王姓，或者（年龄小于40并且年龄大于20并且邮箱不为空）
        // name like '王%' OR (age < 40 AND age > 20 AND email is not null)
        wrapper.likeRight("name", "王").or(
                q -> q.lt("age", 40)
                        .gt("age", 20)
                        .isNotNull("email")
        );

        // 7. (年龄小于40或者邮箱不为空) 并且名字为王姓
        // (age < 40 OR email is not null) AND name like '王%'
        wrapper.nested(q -> q.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "王");

        // 8. 年龄为30，31，34，35
        // age IN (30,31,34,35)
        wrapper.in("age", Arrays.asList(30, 31, 34, 35));
        // 或
        wrapper.inSql("age", "30,31,34,35");

        // 9. 年龄为30，31，34，35, 返回满足条件的第一条记录
        // age IN (30,31,34,35) LIMIT 1
        wrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("LIMIT 1");

        // 10. 只选出id, name 列 (QueryWrapper 特有)
        // SELECT id, name FROM user;
        wrapper.select("id", "name");

        // 11. 选出id, name, age, email, 等同于排除 manager_id 和 create_time
        // 当列特别多, 而只需要排除个别列时, 采用上面的方式可能需要写很多个列, 可以采用重载的select方法，指定需要排除的列
        wrapper.select(User.class, info -> {
            String columnName = info.getColumn();
            return !"create_time".equals(columnName) && !"manager_id".equals(columnName);
        });

    }

    @Test
    public void test2() {
        String name = "黄"; // 假设name变量是一个外部传入的参数
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), "name", name);
        // 仅当 StringUtils.hasText(name) 为 true 时, 会拼接这个like语句到WHERE中
        // 其实就是对下面代码的简化
        if (StringUtils.hasText(name)) {
            wrapper.like("name", name);
        }

    }
    @Test
    public void test3() {
        User user = new User();
        user.setName("黄主管");
        user.setAge(28);
        QueryWrapper<User> wrapper = new QueryWrapper<>(user);
        List<User> users = userMapper.selectList(wrapper);

        users.forEach(System.out::println);
    }


    @Test
    public void test4() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, Object> param = new HashMap<>();
        param.put("age", 40);
        param.put("name", "黄飞飞");
        wrapper.allEq(param);
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

}
