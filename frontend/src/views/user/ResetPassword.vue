<template>
  <div class="find-password-wrapper">
    <div class="find-password-card">
      <!-- 로고 + 브랜드 -->
      <div class="find-password-header">
        <img src="@/assets/logo.png" alt="Logo" class="logo-icon" />
        <h2 class="brand">Kikine</h2>
      </div>

      <h3 class="title">비밀번호 찾기</h3>

      <!-- 인증 방식 선택 -->
      <div class="auth-methods">
        <button
          class="auth-btn"
          :class="{ active: method === 'phone' }"
          @click="method = 'phone'"
        >
          📱 휴대폰 인증
        </button>
        <button
          class="auth-btn"
          :class="{ active: method === 'email' }"
          @click="method = 'email'"
        >
          📧 이메일 인증
        </button>
      </div>

      <!-- 이메일 인증 -->
      <div v-if="method === 'email'" class="auth-form">
        <label for="email">이메일</label>
        <input
          id="email"
          v-model="contact"
          type="email"
          placeholder="가입 시 등록한 이메일 입력"
        />
        <div class="input-with-btn">
          <input
            v-model="code"
            type="text"
            placeholder="인증번호 입력"
          />
          <button class="btn-secondary" @click="sendVerification">
            인증번호 발송
          </button>
        </div>
        <button class="btn-primary" @click="verifyCodeAction">
          인증 확인
        </button>
      </div>

      <!-- 휴대폰 인증 -->
      <div v-if="method === 'phone'" class="auth-form">
        <label for="phone">휴대폰 번호</label>
        <input
          id="phone"
          v-model="contact"
          type="text"
          placeholder="휴대폰 번호 입력"
        />
        <div class="input-with-btn">
          <input
            v-model="code"
            type="text"
            placeholder="인증번호 입력"
          />
          <button class="btn-secondary" @click="sendVerification">
            인증번호 발송
          </button>
        </div>
        <button class="btn-primary" @click="verifyCodeAction">
          인증 확인
        </button>
      </div>

      <!-- 최종 비밀번호 재설정 -->
      <div v-if="verified" class="reset-form">
        <label for="newPass">새 비밀번호</label>
        <input
          id="newPass"
          v-model="newPassword"
          type="password"
          placeholder="새 비밀번호 입력"
        />
        <button class="signup-btn" @click="resetPassword">
          비밀번호 재설정
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { sendCode, verifyCode, resetUserPassword } from "../../api/signup.js";

const method = ref("phone"); // 기본값: 휴대폰 인증
const contact = ref("");
const code = ref("");
const verified = ref(false);
const newPassword = ref("");

const sendVerification = async () => {
  if (!contact.value) return alert("값을 입력하세요");
  try {
    await sendCode(method.value, contact.value);
    alert("인증번호가 발송되었습니다.");
  } catch (e) {
    console.error(e);
    alert("인증번호 발송 실패");
  }
};

const verifyCodeAction = async () => {
  if (!code.value) return alert("인증번호를 입력하세요");
  try {
    const res = await verifyCode(method.value, contact.value, code.value);
    if (res.data) {
      verified.value = true;
      alert("인증 성공");
    } else {
      verified.value = false;
      alert("인증 실패");
    }
  } catch (e) {
    console.error(e);
    alert("인증 오류");
  }
};

const resetPassword = async () => {
  if (!newPassword.value) return alert("새 비밀번호를 입력하세요");
  try {
    await resetUserPassword(contact.value, newPassword.value);
    alert("비밀번호가 재설정되었습니다. 로그인 페이지로 이동합니다.");
    window.location.href = "/login";
  } catch (e) {
    console.error(e);
    alert("비밀번호 재설정 실패");
  }
};
</script>

<style scoped>
.find-password-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #e0f2f1, #a5d6a7);
  font-family: "Noto Sans KR", sans-serif;
}

.find-password-card {
  background: #ffffff;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
  width: 420px;
  text-align: center;
}

.find-password-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 20px;
}

.logo-icon {
  width: 50px;
  height: auto;
}

.brand {
  font-size: 26px;
  font-weight: bold;
  color: #2e7d32;
  margin: 0;
}

.title {
  font-size: 20px;
  margin-bottom: 20px;
}

.auth-methods {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-bottom: 20px;
}

.auth-btn {
  padding: 10px 16px;
  border-radius: 8px;
  border: 1px solid #ddd;
  cursor: pointer;
  background: #f5f5f5;
  transition: 0.2s;
}

.auth-btn:hover {
  background: #e9e9e9;
}

.auth-btn.active {
  background: #4caf50;
  color: white;
  border-color: #4caf50;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
  text-align: left;
}

.auth-form label {
  font-size: 14px;
  font-weight: bold;
}

.auth-form input {
  padding: 12px 15px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 15px;
  width: 100%;
  box-sizing: border-box;
}

.input-with-btn {
  display: flex;
  gap: 10px;
}

.input-with-btn input {
  flex: 1;
}

.input-with-btn button {
  flex: 0 0 130px;
}

.btn-primary {
  background-color: #3498db;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 10px;
}

.btn-primary:hover {
  background-color: #2980b9;
}

.btn-secondary {
  background-color: #f39c12;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 10px;
}

.btn-secondary:hover {
  background-color: #e67e22;
}

.signup-btn {
  width: 100%;
  padding: 14px;
  background-color: #2e7d32;
  color: #fff;
  font-size: 16px;
  margin-top: 10px;
  border-radius: 10px;
}

.signup-btn:hover {
  background-color: #1b5e20;
}
</style>
