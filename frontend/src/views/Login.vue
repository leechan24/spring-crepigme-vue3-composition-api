<template>
  <div class="login-wrapper">
    <div class="login-card">
     <!--  <h2 class="login-title">Welcome Back</h2> -->
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
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const id = ref('')
    const password = ref('')
    const error = ref('')

    const store = useStore()
    const router = useRouter()

    const handleLogin = async () => {
      error.value = ''
      try {
        await store.dispatch('auth/login', { id: id.value, password: password.value })
        router.push('/')
      } catch (err) {
        error.value = '로그인 실패! 아이디와 비밀번호를 확인해주세요.'
      }
    }

    return { id, password, error, handleLogin }
  }
}
</script>

<style scoped>
/* 배경과 전체 레이아웃 */
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  /* background: linear-gradient(135deg, #6b73ff, #000dff);
 */  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 카드 디자인 */
.login-card {
  background: #ffffff;
  padding: 40px 30px;
  border-radius: 12px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
  width: 360px;
  text-align: center;
}

/* 타이틀 */
.login-title {
  margin-bottom: 25px;
  color: #333;
  font-size: 28px;
  font-weight: 600;
}

/* 입력 그룹 */
.input-group {
  margin-bottom: 20px;
  text-align: left;
}

.input-group label {
  display: block;
  margin-bottom: 5px;
  color: #555;
  font-weight: 500;
}

.input-group input {
  width: 100%;
  padding: 12px 15px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 16px;
  outline: none;
  transition: all 0.2s ease;
}

.input-group input:focus {
  border-color: #6b73ff;
  box-shadow: 0 0 5px rgba(107, 115, 255, 0.5);
}

/* 로그인 버튼 */
.login-btn {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  background: #6b73ff;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.3s ease;
}

.login-btn:hover {
  background: #4a5bff;
}

/* 에러 메시지 */
.error {
  margin-top: 15px;
  color: #ff4d4f;
  font-weight: 500;
  font-size: 14px;
}
</style>
