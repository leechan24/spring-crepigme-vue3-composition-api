import axios from "axios";

//const API = process.env.VUE_APP_SERVER_URL;
const API = import.meta.env.VITE_SERVER_URL;

// axios 객체 생성
const http = axios.create({
  baseURL: API,
  // "https://cors-anywhere.herokuapp.com/http://i7a801.p.ssafy.io:8080/api": cors 이슈로 proxy서버를 사용할 때 앞에 붙여 사용
  headers: {
    "Content-type": "application/json",
  },
});

export default http;

//http.interceptors.response.use( ~~~ );