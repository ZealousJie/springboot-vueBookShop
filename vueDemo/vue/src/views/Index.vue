<template>
  <div class="home-container">
    <div class="echart-item" ref="echart"></div>
    <div class="echart-item"></div>
    <div class="echart-item"></div>
    <div class="echart-item"></div>
  </div>
</template>

<script>
// import img_404 from '@/assets/images/gif_404.gif';
import * as echarts from 'echarts';
import request from "@/utils/request";


export default {
  name: 'Index',
  data() {
    return {
      projectOptions: [],
      projectNum: []
    }
  },
  methods: {
    handleGoMain() {
      this.$router.push({path: '/person'})
    },
    async load(){
      let res2 = await request.post("/event/queryProjectNum")
      this.projectNum= res2.data;
      console.log(this.projectNum)
      this.initEcharts();
    },
    initEcharts(){
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(this.$refs.echart)
      let name = []
      this.projectNum.forEach(x => name.push(x.event_project))
      let value = []
      this.projectNum.forEach(x => value.push(x.number))



// 绘制图表
      myChart.setOption({
        title: {
          text: '各项赛事统计'
        },
        tooltip: {},
        xAxis: {
          data: name
        },
        yAxis: {},
        series: [
          {
            name: '数量',
            type: 'bar',
            //this.projectNum
            data: value
          }
        ]
      });
    }
  },
  mounted() {
    this.load()
  }
}
</script>

<style scoped>
.home-container {
  display: flex;
  flex-flow: row wrap;
  width: 100%;
  height: 100%;
}

.echart-item{
  width: 50%;
  height: 50%;

}
</style>
