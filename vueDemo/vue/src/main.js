const app = createApp(App)
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import zhCn from 'element-plus/es/locale/lang/zh-cn' //国际化 将默认语言改为中文
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './utils/request'
// import 'utils/http'
// 统一导入el-icon图标 解决图标不显示问题
import * as ElIconModules from '@element-plus/icons'
import '@/assets/css/global.css'

// 统一注册el-icon图标
for(let iconName in ElIconModules){
    app.component(iconName,ElIconModules[iconName])
}
app.use(store).use(router).use(ElementPlus,{locale: zhCn, size: 'small'}).mount('#app')
