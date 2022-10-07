<template>
  <div>
<!--    default-active="user"-->
<!--    :default-openeds="[1]" 这两步让侧边栏默认展开 user 是和 要展开的 el-menu-item 的Index对应-->
    <el-menu
        style="width: 250px;min-height: calc(100vh - 50px)"
        :default-active="path"

        router
        class="el-menu-vertical-demo">
      <el-sub-menu index="1">
        <template #title>系统管理</template>
        <el-menu-item index="/user">用户管理</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/book">上架书籍管理</el-menu-item>
      <el-menu-item index="/news">公告管理</el-menu-item>
      <el-menu-item index="/orders">订单管理</el-menu-item>
      <el-menu-item index="/audit">书籍审核管理</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data() {
    return {
      path: this.$route.path
    }
  }
}
</script>

<style scoped>

</style>