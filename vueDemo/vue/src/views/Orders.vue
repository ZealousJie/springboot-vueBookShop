<template>
  <div style="padding: 10px">
    <el-table stripe :data="tableData">
      <el-table-column label="id" prop="id" sortable width="80px"/>
      <el-table-column label="订单名称" prop="name"  width="150px"/>
      <el-table-column label="订单编号" prop="orderId" width="120px"/>
      <el-table-column label="支付宝订单号" prop="alipayNo" width="170px"/>
      <el-table-column label="总价格" prop="total" width="80px"/>
      <el-table-column label="单品数量" prop="num" width="80px"/>
      <el-table-column label="创建时间" prop="createTime" width="150px"/>
      <el-table-column label="支付时间" prop="payTime" width="150px"/>
      <el-table-column label="订单状态" prop="state" width="150px"/>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button @click="pay(scope.row)" type="primary" size="small" :disabled="scope.row.state ==='已支付'">支付</el-button>
          <el-button @click="cancel(scope.row.id)" type="danger" size="small" :disabled="scope.row.state ==='已支付'">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>


<script>
import request from "../utils/request";
const baseUrl = "http://localhost:9090"
export default {
  name: 'Orders',
  data() {
    return {
      form: {},
      tableData: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      request.get("/order").then(res =>{
        console.log(res)
        this.tableData=res
      })
    },
    pay(row) {

      window.open("http://localhost:9090/alipay/pay?subject=" + row.name + "&traceNo=" + row.orderId + "&totalAmount=" + row.total)
      this.$message.success("请求支付宝成功")
    },
    cancel(id) {
      console.log(id)
      request.get("/order/deleteOrder/"+id).then(res =>{
        console.log(res)
        this.$message.success("取消订单成功")
        this.load()
      })
    },
  }
}
</script>