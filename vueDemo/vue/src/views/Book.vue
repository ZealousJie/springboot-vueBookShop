<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">上架</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%;"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load()">搜索</el-button>
    </div>
    <el-table :data="tableData" style="width: 99%;margin-left: 0px" stripe border>
      <!--      sortable 加了个可以排序的东东 prop name label value-->
      <el-table-column prop="bid" label="序号" sortable width="80px" align='center'/>
      <el-table-column prop="bookName" label="书名" width="150px" align='center'/>
      <el-table-column prop="price" label="价格" width="100px" align='center'/>
      <el-table-column prop="createTime" label="上架时间" width="150px" align='center'/>
      <el-table-column prop="author" label="作者" width="100px" align='center'/>
      <el-table-column prop="stock" label="库存" width="100px" align='center'/>
      <el-table-column prop="state" label="上架情况" width="100px" align='center'/>
      <el-table-column
          label="封面" width="120px" align='center'>
        <template #default="scope">
<!--     preview-teleported="true" 解决图片被切割     -->
          <el-image
              preview-teleported="true"
              style="width: 100px; height: 100px"
              :src="scope.row.cover"
              :preview-src-list="[scope.row.cover]">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align='center'>
        <template #default="scope">
          <el-button size="small" @click="handleClick(scope.row)" icon="edit" :disabled="scope.row.state !=='已上架'">编辑</el-button>
          <el-button size="small" @click="handleOrder(scope.row.bid)" type="success" icon="sell" :disabled="scope.row.state !=='已上架'">购买测试</el-button>
          <el-popconfirm title="确定要删除吗" @confirm="handleDelete(scope.row.bid)">
            <template #reference>
              <el-button size="small" type="danger" icon="delete" :disabled="scope.row.state !=='已上架'">下架</el-button>
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
          <el-form-item label="书名">
            <el-input v-model="form.bookName" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="form.price" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="出版时间">
            <el-input v-model="form.createTime" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="库存">
            <el-input v-model="form.stock" style="width: 80%;"></el-input>
          </el-form-item>
          <el-form-item label="封面">
<!--        :on-success 上传成功后的回调方法    -->
            <el-upload ref="upload" action="http://localhost:9090/files/upload" :on-success="filesUploadSuccess">
              <el-button type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
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
  name: 'Book',
  components: {
  },
  data(){
    return{
      form: {},
      orderForm: {
        goodsId: 0,
        num: 1
      },
      dialogVisible: false,
      dialogOrder: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: '',
      // filesUploadUrls: "http://" + window.server.filesUploadUrl + ":9090/files/upload",
      tableData: [

      ]
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
      request.get("/book",{ //get请求不能直接传一个对象当做参数 需要这样写
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
        request.post("/book", this.form).then(res =>{
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
    handleDelete(id){
      console.log(id)
      request.delete("/book/"+id).then(res =>{
        console.log(res)
        if (res.code === '0'){
          //element ui 提供的提示框
          this.$message({
            type: "success",
            message: "下架成功"
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