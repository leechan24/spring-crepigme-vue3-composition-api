import { createStore } from 'vuex';
import cre from './cre.js';
import global from './global.js';
import menu from './menu.js';

export default createStore({
  modules: {
    cre,
    global,
    menu
    // 다른 모듈들도 필요한 경우 추가합니다.
  }
});