package com.yc.test.update;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.yc.MybatisPlusApplication;
import com.yc.mapper.UserMapper;
import com.yc.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)//让JUnit运行spring的测试环境，获得spring环境的上下文支持
//获取启动类，加载配置，寻找主配置启动类(被@SpringBootApplication注解的类)
@SpringBootTest(classes = MybatisPlusApplication.class)
//不启动springboot
//@ContextConfiguration(classes = MybatisPlusApplication.class)
//@SpringJUnitConfig(classes = MybatisPlusApplication.class)
public class UpdateTest {
    @Autowired
    private UserMapper userMapper;


    /**
     * updateById(T entity)
     *
     * 根据入参entity的id（主键）进行更新，对于entity中非空的属性，会出现在UPDATE语句的SET后面，即entity中非空的属性，会被更新到数据库，示例如下
     */
    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(2L);
        user.setAge(18);
        userMapper.updateById(user);
    }

    /*根据实体entity和条件构造器wrapper进行更新，示例如下*/
    @Test
    public void testUpdate2() {
        User user = new User();
        user.setName("王三蛋");
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.between(User::getAge, 26,31).likeRight(User::getName,"吴");
        userMapper.update(user, wrapper);
    }

    /*额外演示一下，把实体对象传入Wrapper，即用实体对象构造WHERE条件的案例*/
    @Test
    public void testUpdate3() {
        User whereUser = new User();
        whereUser.setAge(40);
        whereUser.setName("小张");

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>(whereUser);
        User user = new User();
        user.setEmail("share@baomidou.com");
        user.setManagerId(3L);

        userMapper.update(user, wrapper);
    }

    @Test
    public void testUpdate5() {
        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.likeRight(User::getEmail, "share")
                .like(User::getName, "飞飞")
                .set(User::getEmail, "ff@baomidou.com")
                .update();
    }


}
