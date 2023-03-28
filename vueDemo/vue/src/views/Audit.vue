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
      <el-table-column type="selection"/>
      <el-table-column label="序号" sortable width="75px" type="index" :index="hIndex" align="center"/>
      <el-table-column prop="eventName" label="赛事名称" width="150px" align="center"/>
      <el-table-column prop="eventProject" label="赛事项目" width="150px" align="center"/>
<!--      <el-table-column prop="eventVenue" label="赛事举办地点" width="110px"/>-->
      <el-table-column prop="organizer" label="举办方"  align="center"/>
      <el-table-column label="审核情况" prop="auditState" width="100px" align="center">
        <template #default="scope">
          <span v-if="scope.row.auditState === '0'">待审核</span>
          <span v-if="scope.row.auditState === '1'">通过</span>
          <span v-if="scope.row.auditState === '2'">驳回</span>
        </template>
      </el-table-column>
      <el-table-column prop="auditTime" label="审核时间" width="180px" align="center"/>
      <el-table-column prop="auditPerson" label="审核人"  align="center"/>

      <el-table-column fixed="right" label="操作" align='center'  width="300px">
        <template v-slot="scope">
          <el-button size="small" @click="auditDo(scope.row.id)" :disabled=" scope.row.auditState !== '0'">审核通过</el-button>
          <el-button size="small" @click="auditDont(scope.row)" :disabled=" scope.row.auditState !== '0'" >审核不通过</el-button>
          <el-button size="small" @click="refuseMessage(scope.row)"  v-if="scope.row.auditState === '2'">驳回详情</el-button>
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
          <div v-html="detail.rejectMsg" style="min-height: 200px"> </div>
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
  name: 'Audit',
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
      this.form.rejectMsg = editor.txt.html();
      if (this.form.rejectMsg){
        request.post("/audit/updateAuditDont",this.form).then( res =>{
          if (res.code === "0") {
            this.$message.success("审核成功")
            this.vis=false
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
      request.get("/audit/updateAuditDo/"+id).then(res =>{
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
      request.get("/audit/findAllAudit",{ //get请求不能直接传一个对象当做参数 需要这样写
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        this.tableData= res.data.list
        this.total = res.data.total
      })
    },
    handleSizeChange(ps){
      this.pageSize=ps
      this.load()
    },
    hIndex (index){
      // index索引从零开始，index +1即为当前数据序号
      this.currentPage <= 0 ? this.currentPage = 1 : this.currentPage
      // 如果当前不是第一页数据
      if(this.currentPage != 1) {
        // index + 1 + (( 当前页 - 1) * 每页展示条数)
        // 比如是第二页数据 1 + ((2 - 1)*5) = 6,第二页数据也就是从序号6开始
        return index + 1 + ((this.currentPage - 1) * this.pageSize)
      }
      // 否则直接返回索引+1作为序号
      return index + 1
    },
    handleCurrentChange(pn){
      this.currentPage=pn
      this.load()
    }
  }
}
</script>