import api from "@/api/axios";
import router from "@/router";

const state = () => ({
  accessToken: localStorage.getItem("accessToken") || null,
  user: JSON.parse(localStorage.getItem('user')) || null,
});

const getters = {
  isLoggedIn: (state) => !!state.accessToken,
  getAccessToken: (state) => state.accessToken,
  getUser: (state) => state.user,
};

const mutations = {
  SET_ACCESS_TOKEN(state, token) {
    state.accessToken = token;
    localStorage.setItem("accessToken", token);
  },
  CLEAR_ACCESS_TOKEN(state) {
    state.accessToken = null;
    localStorage.removeItem("accessToken");
  },
  SET_USER(state, user) {
    state.user = user;
  },
  CLEAR_USER(state) {
    state.user = null;
  },
};

const actions = {
  async login({ commit }, { id, password }) {
    try {
      const res = await api.post("/auth/login", { id, password });
      const { accessToken, user } = res.data;
      commit("SET_ACCESS_TOKEN", accessToken);
      commit("SET_USER", user);
      
      // 브라우저 스토리지 저장
      localStorage.setItem('user', JSON.stringify(user));
      localStorage.setItem('token', accessToken);
    
    } catch (err) {
      commit("CLEAR_ACCESS_TOKEN");
      commit("CLEAR_USER");
      commit("cre/CLEAR_CRE_LIST", null, { root: true }); // cre 모듈의 리스트 초기화 이건 임시 조치 다른 모듈도 필요
      throw err;
    }
  },

  async logout({ commit, getters }) {
    try {
      const user = getters.getUser;
      const userId = user?.userId;

      if (userId) {
        await api.post("/auth/logout", null, {
          params: { userId },
        });
      }
    } catch (err) {
      console.error("로그아웃 실패", err);
    } finally {
      commit("CLEAR_ACCESS_TOKEN");
      commit("CLEAR_USER");

      localStorage.removeItem('user');
      localStorage.removeItem('accessToken');
      router.push({ name: "Login" });
    }
  },

  // refresh 액션도 그대로 두지만, axios 인터셉터에서 호출되므로 직접 호출은 거의 없음
  async refresh({ commit }) {
    try {
      const res = await api.post("/auth/refresh");
      const { accessToken } = res.data;
      commit("SET_ACCESS_TOKEN", accessToken);
      return accessToken;
    } catch (err) {
      commit("CLEAR_ACCESS_TOKEN");
      commit("CLEAR_USER");
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
