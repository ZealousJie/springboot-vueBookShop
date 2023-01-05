<template>
      <div class="wrapper" style="background-color: gainsboro;padding-bottom: 20px" >
        <div style="height: 60px; line-height: 60px; font-size: 20px;padding-left: 50px;color: #cccccc;
        background-color: rgba(0,0,0,2)">
          奇葩赛事平台
        </div>
        <div style="font-size: 30px; text-align: center; padding: 30px 0; color: #333">欢迎登录</div>

        <div style="margin: 50px auto;background-color: white;
        width: 500px;border-radius: 5px;overflow: hidden">

        <el-tabs type="border-card" v-model="activeName" >
          <el-tab-pane label="账号登录" name="first">
            <div style="width: 400px; margin: 50px auto;height: 250px">

              <el-form ref="form" :model="form" size="normal" :rules="rules">
                <el-form-item prop="account">
                  <el-input prefix-icon="user" v-model="form.account" placeholder="请输入账号"></el-input>

                </el-form-item>
                <el-form-item prop="password">
                  <el-input prefix-icon="Lock" v-model="form.password" show-password placeholder="请输入密码"></el-input>
                </el-form-item>
                <el-form-item>
                  <div style="display: flex">
                    <el-input prefix-icon="el-icon-key" v-model="form.validCode" style="width: 50%;margin-right: 10px" placeholder="请输入验证码"></el-input>
                    <ValidCode @input="createValidCode" />
                  </div>
                </el-form-item>
                <el-form-item>
                  <el-button style="width: 100%" type="primary" @click="userLogin">登 录</el-button>
                </el-form-item>
                <el-form-item><el-button type="text" @click="$router.push('/register')">前往注册 >> </el-button></el-form-item>
              </el-form>
            </div>
          </el-tab-pane>
          <el-tab-pane label="邮箱登录" name="second" >
            <div style="width: 400px; margin: 50px auto;height: 250px">
              <el-form ref="form2" :model="form2" size="normal" :rules="rules2">
                <el-form-item prop="email">
                  <el-input prefix-icon="message" v-model="form2.email" placeholder="请输入邮箱号"></el-input>

                </el-form-item>
                <el-form-item prop="code">
                  <el-input prefix-icon="Lock" v-model="form2.code" show-password placeholder="请输入验证码" style="width: 200px"></el-input>
                  <el-button type="success" @click="getEmailCode" size="small" style="margin-left: 100px;width: 90px;height: 30px">获取验证码</el-button>
                </el-form-item>
<!--                <el-form-item>-->
<!--                  <div style="display: flex">-->
<!--                    <el-input prefix-icon="el-icon-key" v-model="form.validCode" style="width: 50%;margin-right: 10px" placeholder="请输入验证码"></el-input>-->
<!--                    <ValidCode @input="createValidCode" />-->
<!--                  </div>-->
<!--                </el-form-item>-->
                <el-form-item>
                  <el-button style="width: 100%" type="primary" @click="emailLogin">登 录</el-button>
                </el-form-item>
                <el-form-item><el-button type="text" @click="$router.push('/register')">前往注册 >> </el-button></el-form-item>
              </el-form>
            </div>
          </el-tab-pane>

        </el-tabs >
        </div>
      </div>
联系电话: 17373437150

</template>

<script>
import request from "../utils/request.js";
import ValidCode from "../components/ValidCode";
import Cookies from 'js-cookie';
import Layout from "@/layout/Layout";
import router1 from "@/router";
import {activeRouter} from "@/router/permission";

export default {
  name: "Login",
  components: {
    ValidCode,
  },
  data() {
    return {
      activeName: 'first',
      validCode: '',
      form: {},
      form2: {},
      rules: {
        account: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min:1, max: 10, message: '账号长度在10以内', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min:1, max: 20, message: '密码长度在20个字符以内', trigger: 'blur'}
        ],

      },
      rules2: {
        email: [
          {required: true, message: '请输入邮箱号', trigger: 'blur'},
          {min:7, max: 30, message: '邮箱长度在7-30以内', trigger: 'blur'}
        ],
        code: [
          {required: true, message: '请输入邮箱发送的验证码', trigger: 'blur'},
          {min:1, max: 6, message: '验证码是六个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    getEmailCode(){
      let reg = /^[a-zA-Z0-9][a-zA-Z0-9_]+\@[a-zA-Z0-9]+\.[a-zA-Z]{2,5}(\.[a-zA-Z]{2,5})*$/i;

      if (!this.form2.email){
        this.$message.error("请输入邮箱号")
        return
      }
      if (!reg.test(this.form2.email)){
        this.$message.error("请输入正确样式的邮箱号")
        return;
      }
      //发送请求 向该邮箱发送验证码
      request.post("/user/email/"+this.form2.email).then(res =>{
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "发送成功"
          })
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    createValidCode(data) {
      this.validCode = data
    },
    userLogin() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.validCode) {
            this.$message.error("请填写验证码")
            return
          }
          if (this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            return
          }
          request.post("/login/accountLogin", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "登录成功"
              })
              Cookies.set('user', JSON.stringify(res.data))
              activeRouter()
              this.$router.push("/")  //登录成功之后进行页面的跳转，跳转到主页
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    },
    emailLogin(){
      request.post("/user/emailLogin", this.form).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "登录成功"
          })
          // sessionStorage.setItem("uuid", JSON.stringify(res.data.uuid))  // 缓存用户信息
          // Cookies.set('user',JSON.stringify(res.data))
          // // 登录成功的时候更新当前路由
          // // activeRouter()
          // this.$router.push("/user")  //登录成功之后进行页面的跳转，跳转到主页

        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
  }
}
</script>

