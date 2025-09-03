<template>
  <div class="login-wrapper">
    <div class="login-card">
      <!-- 로고 + 브랜드 -->
      <div class="login-header">
        <img src="@/assets/logo.png" alt="Logo" class="logo-icon" />
        <h2 class="brand">Kikine</h2>
      </div>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="input-group">
          <label>아이디</label>
          <input v-model="id" type="text" placeholder="Enter your ID" required />
        </div>
        <div class="input-group">
          <label>비밀번호</label>
          <input v-model="password" type="password" placeholder="Enter your password" required />
        </div>
        <button type="submit" class="login-btn">로그인</button>
        <p v-if="error" class="error">{{ error }}</p>
      </form>

      <div class="login-footer">
        <router-link to="/register">회원가입</router-link> | 
        <router-link to="/forgot-password">비밀번호 찾기</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

const id = ref("");
const password = ref("");
const error = ref("");

const store = useStore();
const router = useRouter();

const handleLogin = async () => {
  error.value = "";
  try {
    // 로그인 액션 호출
    await store.dispatch("auth/login", { id: id.value, password: password.value });
    router.push("/"); // 로그인 성공 시 홈으로 이동
  } catch (err) {
    console.error(err);
    error.value = "로그인 실패! 아이디와 비밀번호를 확인해주세요.";
  }
};
</script>

<style scoped>
/* 기존 스타일 그대로 사용 */
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #e0f2f1, #a5d6a7);
  font-family: 'Noto Sans KR', sans-serif;
}

.login-card {
  background: #ffffff;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
  width: 380px;
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.2);
}

.login-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 30px;
}

.logo-icon {
  width: 50px;
  height: auto;
}

.brand {
  font-size: 28px;
  font-weight: bold;
  color: #2e7d32;
  margin: 0;
}

.input-group {
  margin-bottom: 20px;
  text-align: left;
}

.input-group label {
  display: block;
  margin-bottom: 6px;
  color: #555;
  font-weight: 500;
  font-size: 14px;
}

.input-group input {
  width: 90%;
  padding: 12px 15px;
  border-radius: 10px;
  border: 1px solid #ccc;
  font-size: 16px;
  outline: none;
  transition: all 0.2s ease;
}

.input-group input:focus {
  border-color: #2e7d32;
  box-shadow: 0 0 8px rgba(46, 125, 50, 0.3);
}

.login-btn {
  width: 100%;
  padding: 14px;
  border: none;
  border-radius: 10px;
  background-color: #2e7d32;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.3s ease, transform 0.2s ease;
}

.login-btn:hover {
  background-color: #1b5e20;
  transform: translateY(-2px);
}

.error {
  margin-top: 15px;
  color: #d32f2f;
  font-weight: 500;
  font-size: 14px;
  text-align: center;
}

.login-footer {
  margin-top: 20px;
  font-size: 13px;
  color: #555;
}

.login-footer a {
  color: #2e7d32;
  text-decoration: none;
  margin: 0 5px;
  transition: color 0.2s;
}

.login-footer a:hover {
  color: #1b5e20;
}
</style>
