package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    UserMapper userMapper;

    @PostMapping("/login")//
    public Result<?> login(@RequestBody User user){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword());
        User selectedUser = userMapper.selectOne(wrapper);
        if(selectedUser == null){
            return Result.error("1","用户名或密码错误");
        }
        return Result.success(selectedUser);
    }
    @PostMapping("/register")//
    public Result<?> register(@RequestBody User user){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, user.getUsername());
        User selectedUser = userMapper.selectOne(wrapper);
        if(selectedUser != null){
            return Result.error("1","用户名重复");
        }
        userMapper.insert(user);
        return Result.success();
    }

    //@RequestBody 将json 数据转换为Java对象
    @PostMapping //新增
    public Result<?> save(@RequestBody User user){
        if(user.getPassword() == null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }



    @GetMapping//查询
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        //new一个mp提供的进行分页的Page对象
        Page<User> page = new Page<>(pageNum, pageSize);
        //用Wrappers 生成一个用来完成模糊查询的 wrapper
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if(StrUtil.isNotBlank(search)){//当search 不为空就进行模糊查询 hutool提供的方法 帮助判断非空？
            wrapper.like(User::getNickName, search);
        }
        //用mp的selectPage方法完成分页
        Page<User> userPage = userMapper.selectPage(page, wrapper);
        return Result.success(userPage);
    }
    @PutMapping //更新
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id){
        userMapper.deleteById(id);
        return Result.success();
    }
    //hutool 导入导出excel文件
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        //1 .将数据读取到一个List<Map<String, Object>>里
        List<Map<String, Object>> list = CollUtil.newArrayList();
        List<User> all = userMapper.selectList(null);//查到所有的用户
        for (User user : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("用户名", user.getUsername());
            row1.put("密码", user.getPassword());
            row1.put("昵称", user.getNickName());
            row1.put("年龄", user.getAge());
            row1.put("性别", user.getSex());
            row1.put("地址", user.getAddress());
            list.add(row1);
        }
        // 2. 将list写入到excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
    @PostMapping("/import")
    public Result<?> upload(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);//第二行开始读
        List<User> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickName(row.get(2).toString());
            user.setAge(Integer.valueOf(row.get(3).toString()));
            user.setSex(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            saveList.add(user);
        }
        for (User user : saveList) {
            userMapper.insert(user);
        }
        return Result.success();
    }
    @GetMapping("/books")
    public Result<?> getUserBooks(){



        return Result.success();
    }

}
