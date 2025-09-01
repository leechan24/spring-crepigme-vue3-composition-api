<template>
  <div class="cre-list">
    <div class="common-buttons">
      <button type="button" class="w3-button w3-round w3-blue-gray" v-on:click="fnWrite">등록</button>
    </div>
    <table class="w3-table-all">
      <thead>
      <tr>
        <th>No</th>
        <th>이름</th>
        <th>해칭날짜</th>
        <th>성별</th>
        <th>모프</th>
        <th>가격</th>
        <th>판매가격</th>
        <th>mother</th>
        <th>father</th>
        <th>homeYn</th>
        <th>etc</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(row, idx) in list" :key="idx">
        <td>{{ row.idx }}</td>
        <td><a v-on:click="fnView(row.idx)">{{ row.name }}</a></td>
        <td>{{ row.birthday}}</td>
        <td>{{ row.sex }}</td>
        <td>{{ row.morph}}</td>
        <td>{{ row.price }}</td>
        <td>{{ row.salePrice }}</td>
        <td>{{ row.mother}}</td>
        <td>{{ row.father }}</td>
        <td>{{ row.homeYn}}</td>
        <td>{{ row.etc }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useStore } from 'vuex';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

// 서버 URL
//const serverUrl = process.env.VUE_APP_SERVER_URL;
const  serverUrl = import.meta.env.VITE_SERVER_URL;
// Vue Router & Store
const route = useRoute();
const router = useRouter();
const store = useStore();

// 상태
const requestBody = ref({});
const list = ref([]); // v-for용 배열
const paging = reactive({
  block: 0,
  end_page: 0,
  next_block: 0,
  page: 0,
  page_size: 0,
  prev_block: 0,
  start_index: 0,
  start_page: 0,
  total_block_cnt: 0,
  total_list_cnt: 0,
  total_page_cnt: 0,
});

// 페이징
const page = route.query.page ? parseInt(route.query.page) : 1;
const size = route.query.size ? route.query.size : 10;
const keyword = route.query.keyword;

// 페이징 처리 함수
const paginavigation = () => {
  const pageNumber = [];
  const start_page = paging.start_page;
  const end_page = paging.end_page;
  for (let i = start_page; i <= end_page; i++) pageNumber.push(i);
  return pageNumber;
};

// 잠쥐 리스트 조회
const MouseList = () => {
  requestBody.value = { keyword, page, size };

  axios
    .get(serverUrl + 'api/mouse/list', { params: requestBody.value })
    .then((res) => {
      list.value = res.data;
    })
    .catch((err) => {
      if (err.message.includes('Network Error')) {
        alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
      }
    });
};

// 잠쥐 상세 조회
const fnView = (idx) => {
  router.push({
    name: 'CreDetail',
    params: { idx },
  });
};

// 잠쥐 등록
const fnWrite = () => {
  router.push({ path: './write' });
};

// 초기화
MouseList();
</script>
