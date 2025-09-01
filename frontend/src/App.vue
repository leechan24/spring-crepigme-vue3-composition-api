<template>
  <div id="app" class="layout">
    <PageHeader /> <!-- 헤더 컴포넌트 -->
    <main class="content">
      <router-view /> <!-- 페이지 이동이 표시될 곳 -->
    </main>
    <PageFooter /> <!-- 푸터 컴포넌트 -->

    <!-- 여러 개 공지사항 팝업 -->
    <!-- 첫 페이지(/)에서만 표시 -->
    <div class="notice-container" v-if="route.path === '/'">
      <div
        v-for="(item, idx) in visibleNotices"
        :key="item.idx"
        class="notice-popup"
      >
        <h3>📢 공지사항</h3>
        <div v-html="item.html"></div>
        <div class="popup-buttons">
          <button @click="closeNotice(idx)">닫기</button>
          <button @click="closeToday(item.idx, idx)">오늘 하루 보지 않기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import Cookies from "js-cookie";
import PageHeader from '@/components/PageHeader.vue'
import PageFooter from '@/components/PageFooter.vue'
import { computed, ref, onMounted, watch } from 'vue';
import { useStore } from 'vuex';
import { useRoute } from 'vue-router';

// Composition API +  를 사용하면 컴포넌트 등록이 자동으로 처리되어서 안써도 된다.  
// 그러므로 import에서도 빼기 가능
/* export default  defineComponent {
  name: 'App',
  components: {
    PageFooter,
    PageHeader
  }
} */

const store = useStore();
const route = useRoute();

// Vuex에서 CRE dialogHTML 가져오기
const dialogHTML = computed(() => store.getters['cre/GET_DIALOG_HTML']);

// 실제 화면에 보여줄 공지사항 (쿠키 체크 후)
const visibleNotices = ref([]);

// 닫기 버튼 동작 (해당 인덱스 제거)
const closeNotice = (idx) => {
  visibleNotices.value.splice(idx, 1)
}

// 오늘 하루 보지않기 버튼 
const closeToday = (itemIdx, idx) => {
  // 쿠키에 하루 동안 저장 (1일 만료)
  Cookies.set(`cre-${itemIdx}`, "hidden", { expires: 1 });
  // 화면에서도 제거
  closeNotice(idx);
};

// 공지사항 로드 함수
const loadNotices = () => {
  dialogHTML.value.forEach(item => {
    // 쿠키 체크 + 이미 visibleNotices에 없으면 추가
    const exists = visibleNotices.value.some(v => v.idx === item.idx);
    if (!Cookies.get(`cre-${item.idx}`) && !exists) {
      visibleNotices.value.push({ ...item }); // 얕은 복사로 참조 끊기
    }
  });
};

onMounted(async () => {
  // CRE 리스트가 없으면 조회
  if (!store.getters['cre/GET_CRELIST']?.length) {
    await store.dispatch('cre/FETCH_CRELIST')  // 현재 여기가 공지사항이 되어야하는데 크레로 임시로해서 2번 호출함 list.vue에서도 불러서 
  }

  // 첫 페이지(/)이면 공지사항 로드
  if (route.path === '/') {
    loadNotices();
  }
});

// route 변경 감지
watch(
  () => route.path,
  (newPath) => {
    if (newPath === '/') {
      // 첫 페이지로 돌아오면 공지사항 다시 로드
      loadNotices();
    } else {
      // 다른 페이지로 이동하면 공지사항 숨김
      visibleNotices.value = [];
    }
  }
);
</script>

<style>
/* 전체 레이아웃 */
.layout {
  display: flex;
  flex-direction: column;
  min-height: 98vh; /* 브라우저 전체 높이 채움 */
}

/* main(=router-view)이 남는 공간 차지 */
.content {
  flex: 1;
  padding-top: 20px; /* 헤더 높이 + 여유 */
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}

/* 공지사항 팝업 컨테이너 */
.notice-container {
  position: fixed;   /* 화면 고정 */
  top: 1rem;
  left: 1rem;
  display: flex;
  flex-direction: row;   /* 가로 방향으로 쌓기 */
  flex-wrap: wrap;       /* 화면 끝나면 다음 줄로 */
  gap: 1rem;             /* 팝업 간 간격 */
  z-index: 3000;
}

/* 개별 팝업 카드 */
.notice-popup {
  display: flex;
  flex-direction: column;  /* 내용 + 버튼을 세로로 배치 */
  min-width: 300px;
  max-width: 300px;
  min-height: 350px;      /* 내용이 없어도 일정 높이 */
  background: #fefefe;    /* 약간 밝은 배경 */
  border: 1px solid #ccc;
  border-radius: 8px;      /* 모서리 둥글게 */
  padding: 1rem 1.5rem;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
  z-index: 1001;
  text-align: left;
  overflow: hidden;
}

/* 제목 스타일 */
.notice-popup h3 {
  font-size: 1.2rem;
  font-weight: bold;
  margin: 0 0 0.75rem 0;
  border-bottom: 1px solid #ddd; /* 제목과 내용 구분 */
  padding-bottom: 0.5rem;
  color: #333;
}

/* 내용 스타일 */
.notice-popup div[v-html] {
  flex: 1; /* 내용이 길면 늘어나고 버튼은 아래 고정 */
  font-size: 0.95rem;
  color: #555;
  margin-bottom: 0.75rem;
  overflow-y: auto;  /* 내용 많으면 스크롤 */
}

/* 버튼 영역 */
.popup-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  margin-top: auto;  /* 버튼을 카드 하단으로 밀어내기 */
}

.popup-buttons button {
  background-color: #007bff;
  border: none;
  color: white;
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: background-color 0.2s;
}

.popup-buttons button:hover {
  background-color: #0056b3;
}

/* 오늘 하루 보지않기 버튼 색 다르게 */
.popup-buttons button:last-child {
  background-color: #6c757d;
}

.popup-buttons button:last-child:hover {
  background-color: #5a6268;
}
</style>
