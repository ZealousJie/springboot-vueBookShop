package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.SearchForm;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.vo.RegisterVO;
import com.example.demo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserController {

    @Resource
    UserMapper userMapper;
    @Resource
    UserService userService;

    @PostMapping("/emailLogin")//
    public Result<?> emailLogin(@RequestBody User user){

        return Result.success();
    }
    //向登录邮箱发送验证码
    @PostMapping("/email/{email}")//
    public Result<?> sendEmail(@PathVariable("email") String email){
        System.out.println(email);
        return Result.success();
    }

    @PostMapping("/register")//
    public Result<?> register(@RequestBody RegisterVO registerVO){
        boolean flag = checking(registerVO.getAccount(),registerVO.getPassword());
        if (flag){
            return userService.userRegister(registerVO);
        }
        return Result.error("1","账号或密码不规范");
    }


    @PostMapping
    public Result<?> save(@RequestBody UserVO userVO){
        boolean flag = checkingUserVO(userVO);
        if (!flag){
            return Result.error("1","账号信息缺失或不规范 无法新增");
        }

        return userService.insertUser(userVO);
    }



    @GetMapping//查询
    public Result<?> findUserPage(@RequestBody SearchForm searchForm){
        List<UserVO> usersList = userService.findUsers(searchForm);
        return Result.success(usersList);
    }

    @PostMapping (value = "/updateUser")
    public Result<?> update(@RequestBody UserVO userVO){
        boolean flag = checkingUserVO(userVO);
        if (!flag){
            return Result.error("1","更新后的信息不和规范");
        }
        return userService.updateUser(userVO);
    }
    @DeleteMapping("/{uid}")
    public Result<?> delete(@PathVariable String uid){
        userMapper.deleteByUid(uid);
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
            row1.put("用户名", user.getUserName());
            row1.put("密码", user.getPassword());
            row1.put("昵称", user.getRealName());
            row1.put("年龄", user.getAge());
            row1.put("性别", user.getSex());
            row1.put("地址", user.getIdentityNum());
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
            user.setUserName(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setRealName(row.get(2).toString());
            user.setAge(Integer.valueOf(row.get(3).toString()));
            user.setSex("男");
            user.setIdentityNum(row.get(5).toString());
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

    public boolean checking(String account,String pwd){
        if (account.length()<11 && StrUtil.isNotBlank(account)){
            return true;
        }
        if (pwd.length()<33 && StrUtil.isNotBlank(pwd)){
            return true;
        }
        return false;
    }
    public boolean checkingUserVO(UserVO userVO){
        if (userVO.getAccount().length() == 0 || userVO.getAccount().length() > 10){
            return false;
        }
        if (userVO.getPassword().length() == 0 || userVO.getPassword().length() > 32){
            return false;
        }
        return true;
    }

}
