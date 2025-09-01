import axios from 'axios';

const state = () => ({
  menus: [], // 1레벨 메뉴
  subMenus: {}, // { parentId: [subMenu,...] }
});

const getters = {
  GET_MENUS: (state) => state.menus,
  GET_SUBMENUS: (state) => (parentId) => state.subMenus[parentId] || [],
};

const mutations = {
  SET_MENUS: (state, payload) => {
    state.menus = payload.filter(m => m.lv === 1);
    state.subMenus = {};
    payload.filter(m => m.lv === 2).forEach(m => {
      if (!state.subMenus[m.parentId]) state.subMenus[m.parentId] = [];
      state.subMenus[m.parentId].push(m);
    });
  },
};

const actions = {
  async fetchMenus({ state, commit }) {
    try {
      const res = await axios.get('/api/menu/list');
      commit('SET_MENUS', res.data);
    } catch (err) {
      console.error('메뉴 조회 오류:', err);
    }
  },
  async reloadMenus({ dispatch }) {
    await dispatch('fetchMenus');
  },
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};
