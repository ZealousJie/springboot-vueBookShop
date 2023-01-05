import { createRouter, createWebHistory } from 'vue-router'
import Layout from "@/layout/Layout";
import Cookies from 'js-cookie';


const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/login',
    children: [
      {
        path: 'person',
        name: 'Person',
        component: () => import("../views/Person"),
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("../views/Login")
  },
  {
    path: '/404',
    name: '404',
    component: () => import("../views/404"),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import("../views/Register")
  }
]

const router1 = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

activeRouter()

function activeRouter() {
  let userStr = Cookies.get('user')
  if (userStr){
    let user = JSON.parse(userStr)
    let root = {
      path: '/',
      name: 'Layout',
      component: Layout,
      redirect: "/person",
      children: []
    }
    user.permissionList.forEach(p => {
      let obj = {
        path: p.path,
        name: p.name,
        component: ()=> import("../views/"+p.name)
      };
      root.children.push(obj)
    })
    if (router1){
      router1.addRoute(root)
    }
  }
}

export default router1
