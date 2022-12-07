<template>
  <div>
<!--    default-active="user"-->
<!--    :default-openeds="[1]" 这两步让侧边栏默认展开 user 是和 要展开的 el-menu-item 的Index对应-->
    <el-menu
        style="width: 250px;min-height: calc(100vh - 50px)"
        :default-active="path"

        router
        class="el-menu-vertical-demo">
      <el-menu-item :index="m.path" v-for="m in user.permissionList" :key="m.perId">
        <i :class="m.icon"></i> {{ m.comment }}
      </el-menu-item>

    </el-menu>
  </div>
</template>

<script>
import Cookies from 'js-cookie';
export default {
  name: "Aside",
  data() {
    return {
      path: this.$route.path,
      user: JSON.parse(Cookies.get("user"))
    }
  }
}
</script>

<style scoped>

</style>