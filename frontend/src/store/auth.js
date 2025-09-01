import axios from '@/plugins/axios';

const state = () => ({
  accessToken: localStorage.getItem('accessToken') || null,
  refreshToken: localStorage.getItem('refreshToken') || null,
  user: null,
});

const getters = {
  isLoggedIn: (state) => !!state.accessToken,
  getAccessToken: (state) => state.accessToken,
  getUser: (state) => state.user,
};

const mutations = {
  SET_TOKENS(state, { accessToken, refreshToken }) {
    state.accessToken = accessToken;
    state.refreshToken = refreshToken;
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  },
  CLEAR_TOKENS(state) {
    state.accessToken = null;
    state.refreshToken = null;
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  },
  SET_USER(state, user) {
    state.user = user;
  },
};

const actions = {
  async login({ commit }, { id, password }) {
    const res = await axios.post('/auth/login', { id, password });
    const { accessToken, refreshToken, user } = res.data;
    commit('SET_TOKENS', { accessToken, refreshToken });
    commit('SET_USER', user);
  },

  async logout({ commit }) {
    commit('CLEAR_TOKENS');
    commit('SET_USER', null);
  },

  async refreshToken({ state, commit }) {
    try {
      const res = await axios.post('/auth/refresh', { refreshToken: state.refreshToken });
      const { accessToken } = res.data;
      commit('SET_TOKENS', { accessToken, refreshToken: state.refreshToken });
      return accessToken;
    } catch (err) {
      commit('CLEAR_TOKENS');
      commit('SET_USER', null);
      throw err;
    }
  },
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};
