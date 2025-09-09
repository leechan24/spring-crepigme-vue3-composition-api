import axios from "@/api/axios";

  // 회원가입
  export const signup = (user) => axios.post('/auth/signup', user);
  
  // 인증번호 발송
  export const sendCode = (phoneNumber) => axios.post('/auth/send-code', { phoneNumber });  // -> 백에서 body로 받는법 
  // axios.post('/auth/send-code', null, { params: { phoneNumber } });  -> 백에서 requestparam으로 받는법 
  
  // 인증번호 검증
  export const verifyCode = (phoneNumber, code) => axios.post('/auth/verify-code',  null, {params: { phoneNumber , code } });


  // 비밀번호 변경  (아직 안되어있음)
  export const resetUserPassword = (contact, newPassword) => axios.post('/auth/reset-password',  { contact, newPassword }); 

