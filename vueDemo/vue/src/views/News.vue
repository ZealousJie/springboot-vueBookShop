<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-upload
          action="http://localhost:9090/xx/import"
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
    <el-table :data="tableData" style="width: 85%;margin-left: 30px" stripe border>
      <!--      sortable 加了个可以排序的东东 prop name label value-->
      <el-table-column prop="id" label="序号" sortable width="80px"/>
      <el-table-column prop="title" label="标题" width="350px"/>
<!--      <el-table-column prop="content" label="内容" width="100px"/>-->
      <el-table-column prop="author" label="作者" width="100px"/>
      <el-table-column prop="time" label="发布时间" width="250px"/>
      <el-table-column fixed="right" label="操作" >
        <template #default="scope">
          <el-button size="small" @click="details(scope.row)">详情</el-button>
          <el-button size="small" @click="handleClick(scope.row)">编辑</el-button>
          <el-popconfirm title="确定要删除吗" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
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
      <el-dialog v-model="dialogVisible" title="提示" width="50%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="标题">
            <el-input v-model="form.title" style="width: 50%;"></el-input>
          </el-form-item>
          <div id="div1">
          </div>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button type="primary" @click="save">确认</el-button>
            </span>
        </template>
      </el-dialog>
      <el-dialog v-model="vis" title="新闻详情" width="50%">
        <el-card>
          <div v-html="detail.content" style="min-height: 200px"> </div>
        </el-card>
      </el-dialog>
    </div>
  </div>
</template>


<script>

import request from "../utils/request";
import E from "wangeditor";
let editor;
export default {
  name: 'News',
  components: {
  },
  data(){
    return{
      form: {},
      dialogVisible: false,
      vis: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: '',
      detail: {},
      // filesUploadUrls: "http://" + window.server.filesUploadUrl + ":9090/files/upload",
      tableData: [

      ]
    }
  },
  created() { // 页面加载时就执行的方法
    this.load()
  },
  methods: {
    details(row) {
      this.vis=true
      this.detail= row
    },
    handleUploadSuccess(res) {
      if (res.code === "0") {
        this.$message.success("导入成功")
        this.load()
      }
    },
    exportUser() {
      location.href = "http://" + window.server.filesUploadUrl + ":9090/xx/export";
    },
    createEditor(){
      editor = new E("#div1")
      //因为这个富文本编辑器对返回的数据类型有严格要求，重新写一个接口
      editor.config.uploadImgServer= "http://localhost:9090/files/editor/upload"
      editor.config.uploadFileName='file'
      editor.create();

    },
    //查询方法
    load(){
      request.get("/news",{ //get请求不能直接传一个对象当做参数 需要这样写
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
      this.$nextTick(() => {
        if (!editor){
          this.createEditor()
        }else {
          editor.destroy()
          this.createEditor()
        }

      })
    },
    save(){
      this.form.content=editor.txt.html();
      let userStr = sessionStorage.getItem("user") || "{}"
      let user = JSON.parse(userStr)
      this.form.author = user.nickName
      if(this.form.id){//update
        request.put("/news",this.form).then(res =>{
          console.log(res)
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
        request.post("/news", this.form).then(res =>{
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
      this.$nextTick(() => {
        if(!editor){
          this.createEditor()
          editor.txt.html(row.content)
        }
        else {
          editor.destroy();
          this.createEditor()
          editor.txt.html(row.content)
          }
        })

    },
    handleDelete(id){
      console.log(id)
      request.delete("/news/"+id).then(res =>{
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