package com.example.demo.springbootmybatis.controller;

import com.example.demo.springbootmybatis.dao.UserDAO;
import com.example.demo.springbootmybatis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjingliang
 * @version v1.0
 * @date 2020/8/25
 */
@RestController
@RequestMapping(value = "/index", produces = "application/json;charset=UTF-8")
public class IndexController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @GetMapping("/getcount")
    public String getCount() {
        int count = userDAO.getCount();
        return "数量：" + count;
    }

    @GetMapping("/getuser")
    public List<User> getUser() {
        Map<String, Integer> map = new HashMap<>(4);
        map.put("offset", 0);
        map.put("limit", 100);
        List<User> list = userDAO.queryByOffsetLimit(map);
        return list;
    }

    @PostMapping("/adduser")
    public String addUser(@RequestBody User user) {
        int res = userDAO.insert(user);
        return "添加成功";
    }

    @PutMapping("/updateuser")
    public String updateUser(@RequestBody User user) {
        int res = userDAO.update(user);
        return "修改成功";
    }

    @DeleteMapping("/deleteuser")
    public String deleteUser(@RequestParam("id") Long id) {
        int res = userDAO.deleteById(id);
        return "删除成功";
    }

    /**
     * 事务测试
     *
     * @return
     */
    @Transactional
    @GetMapping("/testsession")
    public String testSession(@RequestParam(value = "lastname", defaultValue = "") String lastname) {
        User user = userDAO.getById(1L);
        user.setLastname(lastname);
        userDAO.update(user);
        // throw new RuntimeException("异常退出");
        // 抛出RuntimeException异常时回滚
        return "事务测试";
    }

    /**
     * 手动提交、回滚事务测试
     *
     * @return
     */
    @GetMapping("/testsession2")
    public String testSession2(@RequestParam(value = "lastname", defaultValue = "") String lastname) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            User user = userDAO.getById(1L);
            user.setLastname(lastname);
            userDAO.update(user);
            transactionManager.commit(status);

            status = transactionManager.getTransaction(def);
            user.setLastname(lastname + "2");
            userDAO.update(user);
            throw new Exception("异常退出");
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
        return "事务测试2";
    }

}
