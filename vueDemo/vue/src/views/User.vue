<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-upload
          action="http://localhost:9090/user/import"
          :on-success="handleUploadSuccess"
          :show-file-list=false
          :limit="1"
          accept='.xlsx'
          style="display: inline-block; margin: 0 10px"
      >
        <el-button type="primary">导入</el-button>
      </el-upload>
      <el-button type="primary" @click="exportUser">导出</el-button>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%;"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load()">搜索</el-button>
    </div>
    <el-table :data="tableData" style="width: 99%;" stripe border>
      <!--      sortable 加了个可以排序的东东 prop name label value-->
      <el-table-column prop="uid" label="ID" sortable  align="center"/>
      <el-table-column prop="account" label="账号"  align="center"/>
      <el-table-column prop="password" label="密码" align="center"/>
      <el-table-column prop="realName" label="姓名"  align="center"/>
      <el-table-column prop="age" label="年龄" sortable  align="center" width="80px"/>
      <el-table-column prop="sex" label="性别"  align="center" width="50px">
        <template #default="scope">
          <span v-if="scope.row.sex === 0">女</span>
          <span v-if="scope.row.sex === 1">男</span>
        </template>
      </el-table-column>
      <el-table-column prop="roles" label="角色" width="100px" align="center"/>
      <el-table-column prop="phone" label="联系电话" align="center"/>
      <el-table-column prop="state" label="账号状态" align="center" >
        <template #default="scope">
          <span v-if="scope.row.state === 0">封禁</span>
          <span v-if="scope.row.state === 1">正常</span>
          <span v-if="scope.row.state === 2">未激活</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template #default="scope">
          <el-button size="mini" type="danger" plain @click="showReason(scope.row.uid)"
                     v-if="scope.row.state === 0" class="el-icon-search" title="查看封禁原因" circle/>
          <el-button size="mini" type="danger" plain @click="prohibit(scope.row.uid)"
                     v-if="scope.row.state === 1" class="el-icon-warning" title="封禁" circle/>
          <el-button size="mini" type="success"
                     v-if="scope.row.state === 2" class="el-icon-warning" title="未激活需完善个人信息" circle/>
          <el-button size="small" type="success" plain @click="handleClick(scope.row)"
                      class="el-icon-edit" title="修改用户信息" circle/>
          <el-popconfirm title="确定要注销吗" @confirm="handleDelete(scope.row.uid)">
            <template #reference>
              <el-button size="small" type="danger" circle class="el-icon-delete" title="注销用户"/>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--  分页  -->
    <div style="padding: 10px 0">
      <el-pagination
          :currentPage="currentPage"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
      <!--  分页  -->

      <el-dialog v-model="dialogVisible" title="提示" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="账号">
            <el-input v-model="form.account" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.userName" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="form.realName" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="未知">未知</el-radio>
          </el-form-item>

        </el-form>
        <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button type="primary" @click="save">确认</el-button>
            </span>
        </template>
      </el-dialog>

    </div>
  </div>
</template>


<script>

import request from "../utils/request";
import Cookies from 'js-cookie';

export default {
  name: 'User',
  components: {
  },
  data(){
    return{
      form: {},
      dialogVisible: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: '',
      tableData: [

      ]
    }
  },
  created() { // 页面加载时就执行的方法
    this.load()
  },
  methods: {
    /**
     * 封禁表
     * @param id
     */
    showReason(id){
      request.get("/user/books/"+id).then(res =>{
        console.log(res)

      })
    },
    prohibit(id){

    },
    handleUploadSuccess(res) {
      if (res.code === "0") {
        this.$message.success("导入成功")
        this.load()
      }
    },
    exportUser() {
      location.href = "http://localhost:9090/user/export";
    },
    //查询方法
    load(){
      request.get("/user",{ //get请求不能直接传一个对象当做参数 需要这样写
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        console.log(res)
        this.tableData= res.data.records
        this.total = res.data.total
      })
    },
    add(){
      this.dialogVisible=true
      this.form={} //清空表单域
    },
    save(){
      let user = JSON.parse(Cookies.get("user"))
      this.form.roles = user.roleIds
      this.form.operator = user.uid
      if(this.form.uid){//update
        request.post("/user/updateUser",this.form).then(res =>{
          if (res.code === '0'){
            //element ui 提供的提示框 别忘了这个美元符合
            this.$message({
              type: "success",
              message: "更新成功"
            })
          }
          else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load()
          this.dialogVisible=false
        })
      }
      else {//add
        request.post("/user", this.form).then(res =>{
          console.log(res);
          if (res.code === '0'){
            //element ui 提供的提示框
            this.$message({
              type: "success",
              message: "新增成功"
            })
          }
          else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load()
          this.dialogVisible=false
        })

      }
    },
    //编辑
    handleClick(row){
      this.form=JSON.parse(JSON.stringify(row)) //这一步将数据转来转去的操作能将要展示的数据搞成一个独立的对象
      //这里的form有id
      this.dialogVisible=true
    },
    handleDelete(uid){
      request.delete("/user/"+uid).then(res =>{
        console.log(res)
        if (res.code === '0'){
          //element ui 提供的提示框
          this.$message({
            type: "success",
            message: "删除成功"
          })
        }
        else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()
      })
    },
    handleSizeChange(ps){
      this.pageSize=ps
      this.load()
    },
    handleCurrentChange(pn){
      this.currentPage=pn
      this.load()
    }
  }
}
</script>