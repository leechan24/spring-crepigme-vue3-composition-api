// 1. DevExtreme 스타일 임포트 (가장 먼저!)
import 'devextreme/dist/css/dx.common.css';
import 'devextreme/dist/css/dx.light.css';

import './assets/common.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import store from './store'
// axios의 초기 interceptor 부분을 설정하는 모듈 
//import interceptor from './api/axios'

// Font Awesome CSS 임포트
import '@fortawesome/fontawesome-free/css/all.css'

const app = createApp(App);
app.config.globalProperties.$axios = axios;  //전역변수로 설정 컴포넌트에서 this.$axios 호출할 수 있음 // vue2 방법
//app.config.globalProperties.$serverUrl = process.env.VUE_APP_SERVER_URL; // vue2 방법이여서 쓰진 않지만 그냥 알아둘려고 적어둠
app.config.globalProperties.$serverUrl = import.meta.env.VITE_SERVER_URL;

app.use(router) // 라우터 플러그인 설치
  .use(store)  // 스토어 플러그인 설치
  .mount('#app'); // 앱 마운트
// 아래와 같이 사전에 조회해서 gloabl로 가질수도 있음 
//await app.config.globalProperties.$store.dispatch('cre/GET_CRELIST');