import { createRouter, createWebHistory } from 'vue-router'
import PageHome from '@/views/PageHome.vue'
import CreList from '@/views/cre/CreList.vue'
import SleepMouseList from '@/views/sleepMouse/SleepMouse.vue'
import CreDetail from '@/views/cre/CreDetail.vue'
import Login from '@/views/Login.vue'
import store from '@/store'   


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
    meta: { requiresAuth: true },
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
  // 개인화 설정 팝업 DxGrid용 
  {
    path: '/cre/test',
    name: 'CreTest',
    component: () => import('../views/cre/CreTest.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/user/SignUp.vue')
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: () => import('../views/user/ResetPassword.vue')
  },

]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), // process.env → import.meta.env
  routes
});

 router.beforeEach((to, from, next) => {
   if (to.meta.requiresAuth && !store.getters['auth/isLoggedIn']) {
     alert("로그인 후 이용 가능합니다.");
     next('/login');
   } else {
     next();
   }
 });

export default router