<template>
  <div class="signup-wrapper">
    <div class="signup-card">
      <!-- 로고 + 브랜드 -->
      <div class="signup-header">
        <img src="@/assets/logo.png" alt="Logo" class="logo-icon" />
        <h2 class="brand">Kikine</h2>
      </div>

      <!-- 전화번호 인증 -->
      <div class="input-group phone-group">
        <label>전화번호</label>
        <input v-model="phone" type="text" placeholder="전화번호" :disabled="verified" />
        <button class="btn-primary" @click="sendVerification" :disabled="verified">
          인증번호 발송
        </button>
      </div>

      <div class="input-group phone-group">
        <label>인증번호</label>
        <input v-model="code" type="text" placeholder="인증번호" :disabled="verified" />
        <button class="btn-secondary" @click="verify" :disabled="verified">
          인증 확인
        </button>
      </div>

      <!-- 회원가입 정보 -->
      <div class="input-group">
        <label>아이디</label>
        <input v-model="userId" type="text" placeholder="아이디" />
      </div>
      <div class="input-group">
        <label>비밀번호</label>
        <input v-model="password" type="password" placeholder="비밀번호" />
      </div>
      <div class="input-group">
        <label>이름</label>
        <input v-model="username" type="text" placeholder="이름" />
      </div>
      <div class="input-group">
        <label>이메일</label>
        <input v-model="email" type="email" placeholder="이메일" />
      </div>

      <!-- 항상 활성화 -->
      <button class="signup-btn" @click="signupUser">
        회원가입
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';  
import { signup, sendCode, verifyCode } from '../../api/signup.js';

const router = useRouter();   //  라우터 객체 생성

const phone = ref('');
const code = ref('');
const username = ref('');
const userId = ref('');
const password = ref('');
const email = ref('');
const verified = ref(false);

const sendVerification = async () => {
  if (!phone.value) return alert('전화번호를 입력하세요');
  try {
    await sendCode(phone.value);
    alert('인증번호가 발송되었습니다.');
  } catch (e) {
    console.error(e);
    alert('인증번호 발송 실패');
  }
};

const verify = async () => {
  if (!code.value) return alert('인증번호를 입력하세요');
  try {
    const res = await verifyCode(phone.value, code.value);
    if (res.data) {
      verified.value = true;
      alert('인증 성공');
    } else {
      verified.value = false;
      alert('인증 실패');
    }
  } catch (e) {
    console.error(e);
    alert('인증 오류');
  }
};

const signupUser = async () => {
  // 필수값 입력 체크
  if (!phone.value) return alert('전화번호를 입력하세요.');
  if (!verified.value) return alert('전화번호 인증을 먼저 완료하세요.');
  if (!userId.value) return alert('아이디를 입력하세요.');
  if (!password.value) return alert('비밀번호를 입력하세요.');
  if (!username.value) return alert('이름을 입력하세요.');
  if (!email.value) return alert('이메일을 입력하세요.');

  try {
    await signup({
      userId: userId.value,
      username: username.value,
      passwordHash: password.value,
      email: email.value,
      status: 'ACTIVE',
      phoneNumber: phone.value
    });
    alert('회원가입 성공');
    router.push('/login');

    // 입력값 초기화
    userId.value = '';
    username.value = '';
    password.value = '';
    email.value = '';
    phone.value = '';
    code.value = '';
    verified.value = false;
  } catch (e) {
    console.error(e);
    alert('회원가입 실패');
  }
};
</script>


<style scoped>
.signup-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #e0f2f1, #a5d6a7);
  font-family: 'Noto Sans KR', sans-serif;
}

.signup-card {
  background: #ffffff;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
  width: 400px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.signup-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.2);
}

.signup-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 25px;
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
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.input-group label {
  margin-bottom: 6px;
  font-weight: 500;
  font-size: 14px;
  color: #555;
}

.input-group input {
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

.phone-group {
  flex-direction: row;
  align-items: center;
  gap: 10px;
}

.phone-group input {
  flex: 1;
}

.phone-group button {
  flex: 0 0 120px;
}

button {
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s ease;
  border: none;
  border-radius: 10px;
}

.btn-primary {
  background-color: #3498db;
  color: #fff;
}

.btn-primary:hover {
  background-color: #2980b9;
}

.btn-secondary {
  background-color: #f39c12;
  color: #fff;
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
}

.signup-btn:hover {
  background-color: #1b5e20;
}
</style>
