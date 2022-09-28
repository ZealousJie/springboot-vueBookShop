import { createRouter, createWebHistory } from 'vue-router'
import User from '../views/User.vue'
import Layout from "../layout/Layout";
import Book from "../views/Book";
import Person from "../views/Person";
import error from "../views/404";
import News from "../views/News";
import Orders from "../views/Orders";

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/login',
    children: [
      {
        path: 'user',
        name: 'User',
        component: User,
      },
      {
        path: 'person',
        name: 'Person',
        component: Person,
      },
      {
        path: 'book',
        name: 'Book',
        component: Book,
      },
      {
        path: 'news',
        name: 'News',
        component: News,
      },
      {
        path: 'orders',
        name: 'Orders',
        component: Orders,
      },

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
    component: error,
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
