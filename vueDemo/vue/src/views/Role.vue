<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增角色</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%;"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load()">搜索</el-button>
      <el-button type="danger" @click="deleteRole()">
        <i class="el-icon-delete"></i>批量删除
      </el-button>
    </div>
    <el-table :data="tableData" style="width: 99%;margin-left: 0px"
              stripe border ref="singleTable"
              tooltip-effect="dark"
              @selection-change="handleSelectionChange">
      <el-table-column type="index"/>
      <el-table-column type="selection"/>
      <el-table-column prop="rid" label="编号" align='center'/>
      <el-table-column prop="roleName" label="角色名" width="150px"  align='center'/>
      <el-table-column prop="isSystem" label="是否为系统角色" align="center" >
        <template #default="scope">
          <span v-if="scope.row.isSystem === 1">系统角色</span>
          <span v-if="scope.row.isSystem === 0">非系统角色</span>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" align='center'/>
<!--      <el-table-column-->
<!--          label="封面" width="120px" align='center'>-->
<!--        <template #default="scope">-->
<!--&lt;!&ndash;     preview-teleported="true" 解决图片被切割     &ndash;&gt;-->
<!--          <el-image-->
<!--              preview-teleported="true"-->
<!--              style="width: 100px; height: 100px"-->
<!--              :src="scope.row.cover"-->
<!--              :preview-src-list="[scope.row.cover]">-->
<!--          </el-image>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column fixed="right" label="操作" align='center'>
        <template #default="scope">
          <el-button size="small" @click="handleClick(scope.row)" icon="edit">编辑</el-button>
          <el-popconfirm title="确定要删除吗" @confirm="this.delete(scope.row.rid)">
            <template #reference>
              <el-button size="small" type="danger" icon="delete" :disabled="scope.row.isSystem == 1">删除</el-button>
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

<!--编辑修改弹出框-->
      <el-dialog v-model="dialogVisible" title="提示" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="角色名">
            <el-input v-model="form.roleName" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="角色描述">
            <el-input v-model="form.price" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="角色权限">
            <el-input v-model="form.createTime" style="width: 80%;"></el-input>
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

      <!--购买弹出提示框-->
      <el-dialog v-model="dialogOrder" title="提示" width="30%">
        <el-form :model="orderForm" label-width="120px">
          <el-form-item label="商品ID">
            <el-input v-model="orderForm.goodsId" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="购买数量">
            <el-input v-model="orderForm.num" style="width: 80%;"></el-input>
          </el-form-item>

        </el-form>

        <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogOrder = false">取消</el-button>
              <el-button type="primary" @click="saveOrder">确认</el-button>
            </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>


<script>

import request from "../utils/request";


export default {
  name: 'Role',
  components: {
  },
  data(){
    return{
      form: {},
      searchForm: {},
      orderForm: {
        goodsId: 0,
        num: 1
      },
      dialogVisible: false,
      dialogOrder: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      // filesUploadUrls: "http://" + window.server.filesUploadUrl + ":9090/files/upload",
      tableData: [],
      search: '',
      selection: [],
      ids: []
    }
  },
  created() { // 页面加载时就执行的方法
    this.load()
  },
  methods: {
    filesUploadSuccess(res) {
      console.log(res)
      this.form.cover = res.data
    },
    //查询方法
    load(){
      this.searchForm.page=this.currentPage
      this.searchForm.rows=this.pageSize
      this.searchForm.search=this.search
      request.post("/role/findRoles",this.searchForm).then(res =>{
        console.log(res)
        this.tableData= res.data.list
        this.total = res.data.total
      })
    },
    add(){
      this.dialogVisible=true
      this.form={} //清空表单域
      if (this.$refs['upload']) {
        this.$refs['upload'].clearFiles()  // 清除历史文件列表
      }
    },
    save(){
      if(this.form.bid){//update
        request.put("/book",this.form).then(res =>{
          console.log(res)
          if (res.code === '0'){
            //element ui 提供的提示框 别忘了这个美元符合
            this.$message({
              type: "success",
              message: "更新成功"
            })
            this.$refs['upload'].clearFiles()  // 清除历史文件列表
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
        request.post("/role/addRole", this.form).then(res =>{
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
        console.log(res);
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