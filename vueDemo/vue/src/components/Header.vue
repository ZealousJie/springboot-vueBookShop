<template>
  <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px;padding-left: 30px;font-weight: bold;color: darkcyan">后台管理</div>
    <div style="flex: 1"></div>
    <div style="width: 100px;padding-top: 18px">
      <el-dropdown>
         <span class="el-dropdown-link">
          {{ userMsg.nickName }}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人中心</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>


<script>
export default {
  name: "Header",
  data(){
    return{
      userMsg:{}
    }
  },
  created() {
   this.userMsg=JSON.parse(window.sessionStorage.getItem('user'))
  },
}


</script>
<style scoped>

</style>





