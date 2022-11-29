<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
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
      <el-button type="primary" >导出</el-button>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%;"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load()">搜索</el-button>
    </div>
    <el-table :data="tableData" style="width: 99%;margin-left: 0px" stripe border>
      <!--      sortable 加了个可以排序的东东 prop name label value-->
      <el-table-column prop="id" label="序号" sortable width="80px"/>
      <el-table-column prop="bid" label="书编" sortable width="80px"/>
      <el-table-column prop="bookName" label="书名" width="150px"/>
<!--      <el-table-column prop="content" label="内容" width="100px"/>-->
      <el-table-column prop="author" label="作者" width="100px"/>
      <el-table-column prop="pricing" label="定价" width="70px"/>
      <el-table-column prop="publishTime" label="出版时间" width="110px"/>
      <el-table-column prop="auditType" label="审核类型" width="90px"/>
      <el-table-column prop="auditPerson" label="审核人" width="80px"/>
      <el-table-column label="审核情况" prop="state" width="200px"/>
      <el-table-column prop="overruleReason" label="驳回理由" :formatter="stateFormat" v-if="false"/>
      <el-table-column fixed="right" label="操作" align='center'  width="300px">
        <template v-slot="scope">
          <el-button size="small" @click="auditDo(scope.row.id)" :disabled=" scope.row.state !== '待审核'">审核通过</el-button>
          <el-button size="small" @click="auditDont(scope.row)" :disabled=" scope.row.state !== '待审核'" >审核不通过</el-button>
          <el-button size="small" @click="refuseMessage(scope.row)"  :disabled=" scope.row.state === '已审核(上架中)'">驳回详情</el-button>
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

      <el-dialog v-model="vis"  title="驳回理由" width="50%">
        <el-form :model="form">

        </el-form>
        <el-card>
          <div id="div1">
          </div>
        </el-card>
        <template #footer>
            <span class="dialog-footer">
              <el-button @click="vis = false">取消</el-button>
              <el-button type="primary" @click="sure">确认</el-button>
            </span>
        </template>
      </el-dialog>
      <el-dialog v-model="dialogVisible" title="驳回详情" width="50%">
        <el-card>
          <div v-html="detail.overruleReason" style="min-height: 200px"> </div>
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
    sure(){
      let uuid = JSON.parse(window.sessionStorage.getItem('uuid'))
      this.form.overruleReason = editor.txt.html();
      if (this.form.overruleReason){
        request.put("/audit/updateAuditDont/uuid/"+uuid,this.form).then( res =>{
          if (res.code === "0") {
            this.$message.success("审核成功")
            this.load()
          }else {
            this.$message.error("审核失败")
          }
        })
      }else {
        alert("不能为空")
      }
    },
    stateFormat(row,column,cellValue){
      if(!cellValue) return "";
      if (cellValue.length > 2){
        return cellValue.slice(0,2)+ "...";
      }
      return cellValue;
    },
    refuseMessage(row) {
      this.detail=row;
      this.dialogVisible=true
    },
    auditDo(id) {
      let uuid = JSON.parse(window.sessionStorage.getItem('uuid'))
      request.get("/audit/updateAuditDo/id/"+id+"/uuid/"+uuid).then(res =>{
        if (res.code === "0") {
          this.$message.success("审核成功")
          this.load()
        }else {
          this.$message.error("审核失败")
        }
      })
    },
    auditDont(row) {
      this.form=JSON.parse(JSON.stringify(row))
      this.vis=true
      this.$nextTick(() => {
        if(!editor){
          this.createEditor()
          editor.txt.html("请填写合理的驳回理由,为空无法提交")
        }
        else {
          editor.destroy();
          this.createEditor()
          editor.txt.html("请填写合理的驳回理由,为空无法提交")
        }
      })

    },
    handleUploadSuccess(res) {
      if (res.code === "0") {
        this.$message.success("导入成功")
        this.load()
      }
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
      request.get("/audit",{ //get请求不能直接传一个对象当做参数 需要这样写
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