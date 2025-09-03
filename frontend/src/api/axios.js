import axios from "axios";
import store from "@/store";
import router from "@/router";

const api = axios.create({
  baseURL: "/api",
  headers: { "Content-Type": "application/json" },
});

// 요청 인터셉터
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터: 401 발생 시 처리
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const token = localStorage.getItem("accessToken");

    // 1. 로그인 안 된 상태 → 바로 로그인 안내
    if (error.response?.status === 401 && !token) {
      alert("로그인 후 이용 가능합니다.");
      router.push({ name: "Login" });
      return Promise.reject(error);
    }

    // 2. 로그인 되어 있음 & 토큰 만료 → refresh 시도
    if (error.response?.status === 401 && token && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const newToken = await store.dispatch("auth/refresh");
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        store.commit("auth/CLEAR_ACCESS_TOKEN");
        store.commit("auth/CLEAR_USER");
        alert("세션이 만료되었습니다.");
        router.push({ name: "Login" });
        return Promise.reject(refreshError);
      }
    }

    // 그 외 오류
    return Promise.reject(error);
  }
);

export default api;
