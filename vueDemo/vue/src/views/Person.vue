<template>
  <div style="display:flex">
    <el-card style="width: 600px; margin: 10px 50px 10px 10px;height: 675px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
        <el-form-item style="text-align: center" label-width="0">
          <el-upload
              class="avatar-uploader"
              action="http://127.0.0.1:9876/api/files/ossUpload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
          <el-form-item>
            <div style="font-size: 50px">
              个人信息
            </div>
          </el-form-item>
        </el-row>
        <el-form-item label="姓名">
          <el-input v-model="form.realName" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.userName"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="form.sex"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center;margin-top: 60px">
        <el-button size="medium" type="primary" @click="update">保存</el-button>
        <el-button size="medium" type="danger" @click="updatePwd()">修改密码</el-button>
      </div>
    </el-card>
    <el-card style="width: 600px; margin: 10px;height: 675px">
      <div style="font-size: 50px;margin: 60px" align="center">
        关注列表
      </div>
      <el-descriptions  v-for="item in eventForm" title="赛事信息">
        <el-descriptions-item label="赛事名">{{item.eventName}}</el-descriptions-item>
        <el-descriptions-item label="赛事阶段">{{ item.eventStage }}</el-descriptions-item>
        <el-descriptions-item label="备注">
          <el-tag size="small" type="success" onclick="alert('发Q')">双击详情</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作"><el-button size="small" @click="delAttention(item.id)" type="danger">取消关注</el-button></el-descriptions-item>
      </el-descriptions>
      <div v-if="eventForm.length === 0" style="font-size: 40px;text-align: center;color: lightsalmon;margin-top: 170px">
        暂无数据
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "../utils/request";
import Cookies from 'js-cookie'
export default {
  name: "Person",
  data() {
    return {
      form: {},
      eventForm: {}
    }
  },
  created() {
    let str = Cookies.get('user') ? JSON.parse(Cookies.get('user')) : {}
    this.form = str
    this.load()
  },
  methods: {
    load(){
      request.post("/user/queryAttentionByUid").then(res =>{
        this.eventForm = res.data
      })
    },
    updatePwd(){

    },
    delAttention(id){
      let attentionForm = {
        typeId: 0,
        eventId: id
      }
      request.post("/event/attentionOrNot",attentionForm).then(res =>{
        if (res.code === '0'){
          //element ui 提供的提示框 别忘了这个美元符合
          this.$message({
            type: "success",
            message: "取关成功"
          })
          this.load()
        }
        else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    handleAvatarSuccess(res) {
      if (res.code === '0') {
        this.$message({
          type: "success",
          message: "上传成功"
        })
        this.form.avatar = res.data
      }else {
        this.$message({
          type: "error",
          message: res.msg
        })
      }

    },
    update() {
      request.put("/user/updateUserPerson", this.form).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
          Cookies.set('user',JSON.stringify(this.form))
          // sessionStorage.setItem("user", JSON.stringify(this.form))
          // 触发Layout更新用户信息
          this.$emit("userInfo")
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>