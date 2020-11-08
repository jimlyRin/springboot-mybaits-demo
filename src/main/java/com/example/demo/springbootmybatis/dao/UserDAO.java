package com.example.demo.springbootmybatis.dao;

import com.example.demo.springbootmybatis.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author linjingliang
 * @version v1.0
 * @date 2020/8/24
 */
@Repository
public interface UserDAO {

    int getCount();

    User getById(Long id);

    List<User> queryByFirstName(String firstName);

    List<User> queryByOffsetLimit(Map map);

    int insert(User user);

    int update(User user);

    int deleteById(Long id);

    int deleteBatch(List<Long> list);
}
