package com.houtong;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houtong.dao.UserDao;
import com.houtong.domain.User;
import com.houtong.domain.query.UserQuery;
import com.houtong.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MybatisPlusStartApplicationTests {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Test
    void testSelect() {
        System.out.println(userDao.selectById(1));
        System.out.println(userDao.selectList(null));
    }
    @Test
    void testInsert() {
        User user = new User();
        user.setName("张三");
        user.setPassword("1234");
        user.setBirthday(LocalDate.of(2001,8,15));
        userDao.insert(user);
        System.out.println((user.getId()));
    }
    @Test
    void testUpdate() {
        User user = new User();
        user.setId(1674346433634885634L);
        user.setName("李四");
        user.setPassword("164");
        user.setBirthday(LocalDate.of(2002,2,27));
        //必须设置version才能开启乐观锁
        user.setVersion(1);
        userDao.updateById(user);
    }
    //测试乐观锁
    @Test
    void testLockUpdate() {
        //两个用户先后几乎同时操作（会拿到相同的version值），这里因为是user2先更新，所以只有user2更新成功
        User user = userDao.selectById(1674346433634885634L);
        User user2 = userDao.selectById(1674346433634885634L);

        user2.setBirthday(LocalDate.of(2002,4,27));
        userDao.updateById(user2);

        user.setBirthday(LocalDate.of(2002,5,27));
        userDao.updateById(user);
    }
    @Test
    void testDelete() {
        userDao.deleteById(1674347112814321666L);
    }
    //需配置MpConfig类才能成功运行如下的分页操作
    @Test
    void testGetByPage(){
        IPage page = new Page(2,2);
        userDao.selectPage(page,null);
        System.out.println("当前页码为："+page.getCurrent());
        System.out.println("每页显示数："+page.getSize());
        System.out.println("一共的页数："+page.getPages());
        System.out.println("数据的条数："+page.getTotal());
        System.out.println("数据如下："+page.getRecords());
    }
    //测试mybatisplus的条件查询
    @Test
    void testConditionSelect(){
        //方法一：按条件查询
//        QueryWrapper queryWrapper = new QueryWrapper();
//        //如下表示生日早于2001-1-1的条件。column参数是实体类中的属性名，第二个参数是数值。lt函数是少于，gt函数是大于
//        queryWrapper.lt("birthday",LocalDate.of(2001,1,1));
//        List<User> users = userDao.selectList(queryWrapper);
//        System.out.println(users);
        //方法二：lambda格式的条件查询
        //这里使用lambda方法需要指定泛型
//        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        //如下也表示生日早于2001-1-1的条件。第一个参数是查询属性的getter方法，第二个参数是数值。lt函数是少于，gt函数是大于
//        queryWrapper.lambda().lt(User::getBirthday,LocalDate.of(2001,1,1));
//        List<User> users = userDao.selectList(queryWrapper);
//        System.out.println(users);
        //方法三：lambda格式的条件查询
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
//        //如下表示生日晚于2000-1-1并早于2002-1-1的条件。第一个参数是查询属性的getter方法，第二个参数是数值。lt函数是少于，gt函数是大于
//        //也可以写成链式编程：
//        queryWrapper.lt(User::getBirthday,LocalDate.of(2002,1,1))
//        .gt(User::getBirthday,LocalDate.of(2000,1,1));
////        queryWrapper.lt(User::getBirthday,LocalDate.of(2002,1,1));
////        queryWrapper.gt(User::getBirthday,LocalDate.of(2000,1,1));
//        //如下是逻辑或的写法
//        queryWrapper.gt(User::getBirthday,LocalDate.of(2002,1,1))
//        .or().lt(User::getBirthday,LocalDate.of(2000,1,1));


        //使用查询对象来做
        UserQuery userQuery = new UserQuery();
        userQuery.setBirthday(LocalDate.of(2000,1,1));
        userQuery.setBirthday2(LocalDate.of(2002,1,1));
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        //condition参数为true才添加条件，否则不添加
        queryWrapper.gt(userQuery.getBirthday()!=null,User::getBirthday,userQuery.getBirthday());
        queryWrapper.lt(userQuery.getBirthday2()!=null,User::getBirthday,userQuery.getBirthday2());
        List<User> users = userDao.selectList(queryWrapper);
        System.out.println(users);
    }
    //查询投影
    @Test
    void testSelectProject() {
        //非lambda格式
//        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        queryWrapper.select("id","name");
//        List<User> users = userDao.selectList(queryWrapper);
        //lambda格式
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        //如下表示生日早于2001-1-1的条件。column参数是实体类中的属性名，第二个参数是数值。lt函数是少于，gt函数是大于
        queryWrapper.lt(User::getBirthday,LocalDate.of(2001,1,1));
        queryWrapper.select(User::getId,User::getName);
        List<User> users = userDao.selectList(queryWrapper);
        System.out.println(users);
    }
    //聚类和分组查询
    @Test
    void testSelectGroup() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        //按名字聚类统计人数
        queryWrapper.select("name, count(*) as count ");
        queryWrapper.groupBy("name");
        List<Map<String, Object>> users = userDao.selectMaps(queryWrapper);
        System.out.println(users);
    }
    //多条件查询
    @Test
    void testMultiConditionSelect() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        //如下表示生日早于2002-1-1的条件。column参数是实体类中的属性名，第二个参数是数值。lt函数是少于，gt函数是大于
        queryWrapper.lt(User::getBirthday,LocalDate.of(2002,1,1));
        queryWrapper.eq(User::getName,"侯彤");
        List<User> users = userDao.selectList(queryWrapper);
        System.out.println(users);
    }
    //
    @Test
    void testService(){
        List<User> users = userService.list();
        System.out.println(users);
    }
}
