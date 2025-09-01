import { createRouter, createWebHistory } from 'vue-router'
import PageHome from '@/views/PageHome.vue'
import CreList from '@/views/cre/CreList.vue'
import SleepMouseList from '@/views/sleepMouse/SleepMouse.vue'
import CreDetail from '@/views/cre/CreDetail.vue'
import Login from '@/views/Login.vue'



const routes = [
  {
    path: '/',
    name: 'PageHome',
    component: PageHome
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/PageAbout.vue')
  },
  {
    path: '/cre/list',
    name: 'CreList',
    component: CreList
  },
  {
    path: '/mouse/list',
    name: 'SleepMouse',
    component: SleepMouseList
  },
  {
    path: '/cre/detail/:idx',
    name: 'CreDetail',
    component: CreDetail
  },
  {
    path: '/cre/write',
    name: 'CreWrite',
    component: () => import('../views/cre/CreWrite.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), // process.env → import.meta.env
  routes
});

// router.beforeEach((to, from, next) => {
//   if (to.meta.requiresAuth && !store.getters['auth/isLoggedIn']) {
//     next('/login');
//   } else {
//     next();
//   }
// });

export default router