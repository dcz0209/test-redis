package com.dongchuanzhe.redis.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dcz.util.DateUtil;
import com.dcz.util.RandomUtil;
import com.dcz.util.StringUtil;
import com.dongchuanzhe.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class RedisTest {

    @Resource
    RedisTemplate<String, User> redisTemplate;
    
    
    
    /**
     * 
     * @Title: randomUser 
     * @Description:  模拟生成5万条User对象（22分）
     * @return: void
     */
    public List<User> randomUser() {
        User user;
        List<User> list = new ArrayList<User>();
        String[] gender = {"男","女"};
        String[] email = {"@qq.com","@163.com","@sian.com","@gmail.com","@sohu.com","@hotmail.com","@foxmail.com"};
        for (int i = 1; i <= 50000; i++) {
            user = new User();
            //(1)   ID使用1-5万的顺序号。（2分）
            user.setId(i);
            //(2)   姓名使用3个随机汉字模拟，可以使用以前自己编写的工具方法。（4分）
            user.setName(StringUtil.generateChineseName());
            //(3) 性别在女和男两个值中随机。（4分）
            user.setGender(gender[RandomUtil.random(0, 1)]);
            //(4) 手机以13开头+9位随机数模拟。（4分）
            user.setPhone("13"+RandomUtil.randomNumberString(9));       
            //(5)   邮箱以3-20个随机字母 + @qq.com  | @163.com | @sian.com | @gmail.com | @sohu.com | @hotmail.com | @foxmail.com模拟
            user.setEmail(RandomUtil.randomString(RandomUtil.random(3, 20))+email[RandomUtil.random(0, email.length-1)]);
            //(6)   生日要模拟18-70岁之间，即日期从1949年到2001年之间
            Calendar calendar = Calendar.getInstance();
            calendar.set(1949, 0, 1);
            Date d1 = calendar.getTime();
            calendar.set(2000, 11, 31);
            Date d2 = calendar.getTime();
            user.setBirthday(DateUtil.randomDate(d1, d2));
            list.add(user);
        }
        
        return list;
    }
    public Map<String,User> randomUsermap() {
        User user;
        Map<String,User> map = new HashMap<String, User>();
        String[] gender = {"男","女"};
        String[] email = {"@qq.com","@163.com","@sian.com","@gmail.com","@sohu.com","@hotmail.com","@foxmail.com"};
        for (int i = 1; i <= 50000; i++) {
            user = new User();
            //(1)   ID使用1-5万的顺序号。（2分）
            user.setId(i);
            //(2)   姓名使用3个随机汉字模拟，可以使用以前自己编写的工具方法。（4分）
            user.setName(StringUtil.generateChineseName());
            //(3) 性别在女和男两个值中随机。（4分）
            user.setGender(gender[RandomUtil.random(0, 1)]);
            //(4) 手机以13开头+9位随机数模拟。（4分）
            user.setPhone("13"+RandomUtil.randomNumberString(9));       
            //(5)   邮箱以3-20个随机字母 + @qq.com  | @163.com | @sian.com | @gmail.com | @sohu.com | @hotmail.com | @foxmail.com模拟
            user.setEmail(RandomUtil.randomString(RandomUtil.random(3, 20))+email[RandomUtil.random(0, email.length-1)]);
            //(6)   生日要模拟18-70岁之间，即日期从1949年到2001年之间
            Calendar calendar = Calendar.getInstance();
            calendar.set(1949, 0, 1);
            Date d1 = calendar.getTime();
            calendar.set(2000, 11, 31);
            Date d2 = calendar.getTime();
            user.setBirthday(DateUtil.randomDate(d1, d2));
            map.put(user.getId()+"", user);
        }
        
        return map;
    }
    
    
    @Test
    public void addByJDK() {
        List<User> list = randomUser();
        long t1 = System.currentTimeMillis();
        ListOperations<String, User> opsForList = redisTemplate.opsForList();
        opsForList.leftPushAll("jdk", list);
        
        long t2 = System.currentTimeMillis();
        System.out.println("ByJdk:50000条:"+(t2-t1)+"毫秒");
    }
    @Test
    public void addByJSON() {
        List<User> list = randomUser();
        long t1 = System.currentTimeMillis();
        ListOperations<String, User> opsForList = redisTemplate.opsForList();
        opsForList.leftPushAll("json", list);
        
        long t2 = System.currentTimeMillis();
        System.out.println("ByJson:50000条:"+(t2-t1)+"毫秒");
    }
    @Test
    public void addHash() {
        Map<String, User> map = randomUsermap();
        long t1 = System.currentTimeMillis();
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.putAll("hash", map);
        
        long t2 = System.currentTimeMillis();
        System.out.println("hash:50000条:"+(t2-t1)+"毫秒");
    }
    
}
