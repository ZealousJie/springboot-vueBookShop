<template>
  <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px;padding-left: 30px;font-weight: bold;color: darkcyan">后台管理</div>
    <div style="flex: 1"></div>
    <div style="width: 100px;padding-top: 18px">
      <el-dropdown :disabled="disabledFlag">
         <span class="el-dropdown-link">
          {{tip}}{{ user.userName }}{{ user.realName }}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人中心</el-dropdown-item>
            <el-dropdown-item @click="logout()">注销登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>


<script>
import Cookies from 'js-cookie';
export default {
  name: "Header",
  data(){
    return{
      user: Cookies.get('user') ? JSON.parse(Cookies.get('user')) : {},
      tip: '您未登录',
      disabledFlag: true
    }
  },
  created() {
    this.load()
  },
  methods: {
    logout(){
      Cookies.remove('user');
      Cookies.remove('userTicket');
      this.$router.push('/login')
    },
    load(){
      if (this.user.userName){
        this.disabledFlag = false;
        this.tip=null;
        this.user.realName = null;
      }
      if (this.user.realName){
        this.disabledFlag = false;
        this.tip=null;
      }
    }


  }

}


</script>
<style scoped>

</style>





