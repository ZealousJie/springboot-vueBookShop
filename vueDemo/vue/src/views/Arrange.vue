<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" size="large" style="font-size: 18px;margin-right: 20px">生成赛事安排</el-button>
      <span style="font-size: larger">赛事名称:</span>
      <el-select v-model="form.eventId" placeholder="赛事名称" filterable clearable style="margin: 10px 10px 10px 0"
                 @change="getEventArrange()" size="large">
        <el-option v-for="(item, i) in events" :key="i" :label="item.eventName" :value="item.id"> </el-option>
      </el-select>
    </div>

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

<!--编辑修改弹出框-->
      <el-dialog v-model="dialogVisible" title="提示" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="角色名">
            <el-input v-model="form.roleName" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="角色描述">
            <el-input v-model="form.description" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="是否为系统角色">
            <el-radio-group v-model="form.isSystem">
              <el-radio :label="1">系统角色</el-radio>
              <el-radio :label="0">非系统角色</el-radio>
            </el-radio-group>
          </el-form-item>
<!--          <el-form-item label="封面">-->
<!--&lt;!&ndash;        :on-success 上传成功后的回调方法    &ndash;&gt;-->
<!--            <el-upload ref="upload" action="http://localhost:9090/files/upload" :on-success="filesUploadSuccess">-->
<!--              <el-button type="primary">点击上传</el-button>-->
<!--            </el-upload>-->
<!--          </el-form-item>-->
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
import Cookies from "js-cookie";


export default {
  name: 'Arrange',
  components: {
  },
  data(){
    return{
      form: {},
      roleForm: {},
      searchForm: {},
      dialogVisible: false,
      dialogOrder: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      search: '',
      selection: [],
      ids: [],
      events: [],
      arrange: []
    }
  },
  created() { // 页面加载时就执行的方法
    this.load()
  },
  methods: {
    //查询方法
    load(){
        request.post('/event/queryAllEvents', this.form).then((res) => {
          this.events = res.data.list;
        });
    },
    getEventArrange(){
      this.form.page = this.currentPage
      this.form.rows = this.pageSize
      request.post('/arrange/queryArrangeByEventId', this.form).then((res) => {
        this.arrange = res.data.list;
      });
    },
    add(){
      this.dialogVisible=true
      this.form={} //清空表单域
      if (this.$refs['upload']) {
        this.$refs['upload'].clearFiles()  // 清除历史文件列表
      }
    },
    save(){
      if(this.form.rid){//update
        request.post("/role/updateRole",this.form).then(res =>{
          console.log(res)
          if (res.code === '0'){
            //element ui 提供的提示框 别忘了这个美元符合
            this.$message({
              type: "success",
              message: "更新成功"
            })
            // this.$refs['upload'].clearFiles()  // 清除历史文件列表
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
        request.post("/role/insertRole", this.form).then(res =>{
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
    //下单1 进入提示框选择购买数量
    handleOrder(bid){
      this.orderForm.goodsId=bid
      this.dialogOrder=true
    },
    saveOrder(){
      request.post("/order", this.orderForm).then(res =>{
        if (res.code === '0'){
          //element ui 提供的提示框
          this.$message({
            type: "success",
            message: "下单成功"
          })
        }
        else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()
        this.dialogOrder=false
      })

    },
    //编辑
    handleClick(row){
      this.form=JSON.parse(JSON.stringify(row))
      //JSON.parse(JSON.stringify(row)) 这一步将数据转来转去的操作能将要展示的数据搞成一个独立的对象
      //这里的form有id
      this.dialogVisible=true
    },
    handlePermission(row){
      let userCurrent = JSON.parse(Cookies.get("user"))
      this.roleForm.rid = row.rid
      this.roleForm.permissionList = row.permissionList
      this.roleForm.currentRoleIds = userCurrent.roleList
      console.log(this.roleForm)
      request.post("/role/changePermission",this.roleForm).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "更新成功"
          })
        }
        else if (res.code === "2"){
          this.$message({
            type: "info",
            message: res.msg
          })
          //10s后强制重新登录
          setTimeout(() => {
            this.$router.push("/login")
          },10000)
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    deleteRole() {
      if (this.selection.length === 0) {//选中0条数据时
        this.$message.info("未选中数据");
        return;
      }
      this.selection.forEach( element => {
        this.ids.push(element.rid)
      })

      let params = {
        ids: this.ids
      }
      this.handleDelete(params)
    },
    handleDelete(params){
      console.log(params)
      request.post("/role/deleteRole",params).then(res =>{
        console.log(res)
        if (res.code === '0'){
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
    delete(id){
      this.ids.push(id)
      let params = {
        ids: this.ids
      }
      request.post("/role/deleteRole",params).then(res =>{
        console.log(res)
        if (res.code === '0'){
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
    },
    handleSelectionChange(val){
      this.selection = val;
    },

  }
}
</script>