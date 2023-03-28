<template>
  <div>
    <!--    头部-->
    <Header/>
    <!--    主体-->
    <div style="display:flex;">
      <!--      侧边栏-->
      <Aside/>
      <div style="flex: 1">
        <!--      内容区域-->
        <router-view />
      </div>

    </div>

  </div>
</template>

<style>
@import url("//unpkg.com/element-ui@2.15.6/lib/theme-chalk/index.css");
/*解决图标不显示问题*/
</style>
<script>
import Header from "@/components/Header";
import Aside from "@/components/Aside";

export default {
  name: "Layout",
  components: {
    Header,
    Aside
  }
}
</script>
