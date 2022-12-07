import { createRouter, createWebHistory } from 'vue-router'
import Layout from "@/layout/Layout";


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

export default router1
