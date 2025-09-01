<script setup>
import { ref, reactive, computed } from 'vue';
import { useStore } from 'vuex';
import { useRoute, useRouter } from 'vue-router';
import Card from '@/views/cre/CreCard.vue';

const route = useRoute();
const router = useRouter();
const store = useStore();

const requestBody = ref({});

// 전체 리스트
const fullList = computed(() => store.getters['cre/GET_CRELIST'] || []);

// 페이지 설정
const page = ref(route.query.page ? parseInt(route.query.page) : 1);
const size = 6; // 한 페이지 6개

const totalPages = computed(() => Math.ceil(fullList.value.length / size));

// 현재 페이지 리스트
const list = computed(() => {
  const start = (page.value - 1) * size;
  return fullList.value.slice(start, start + size);
});

// 페이징 버튼 슬라이딩 (한 번에 5페이지)
const pageBlockSize = 5;
const startPage = computed(() => Math.floor((page.value - 1) / pageBlockSize) * pageBlockSize + 1);
const endPage = computed(() => Math.min(startPage.value + pageBlockSize - 1, totalPages.value));

const pageNumbers = computed(() => {
  const pages = [];
  for (let i = startPage.value; i <= endPage.value; i++) pages.push(i);
  return pages;
});

// 페이지 이동
const goPage = (p) => {
  if (p < 1 || p > totalPages.value) return;
  page.value = p;
};

// 이전/다음 블록 이동
const prevBlock = () => goPage(startPage.value - 1);
const nextBlock = () => goPage(endPage.value + 1);

// 카드 클릭
const fnView = (idx) => router.push({ name: 'CreDetail', params: { idx } });
const fnWrite = () => router.push({ path: './write' });

// 리스트 조회
const creList = async () => {
  if (store.getters['cre/GET_CRELIST']?.length) return;
  try {
    await store.dispatch('cre/FETCH_CRELIST', requestBody);
  } catch (err) {
    console.error(err);
    alert('데이터 조회 중 오류가 발생했습니다.');
  }
};

creList();
</script>

<template>
  <div class="home">
    <div class="card-grid">
      <Card v-for="(item, idx) in list" :key="idx" :item="item" @click="fnView(item.idx)" />
    </div>

    <!-- 페이징 버튼 -->
    <div class="pagination">
      <button @click="prevBlock" :disabled="startPage === 1">«</button>
      <button
        v-for="p in pageNumbers"
        :key="p"
        @click="goPage(p)"
        :class="{ active: p === page }"
      >
        {{ p }}
      </button>
      <button @click="nextBlock" :disabled="endPage === totalPages">»</button>
    </div>
  </div>
</template>

<style scoped>
.card-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  justify-items: center;
  padding: 0 1rem;
  max-width: 1200px;
  margin: 0 auto;
}

@media (max-width: 992px) {
  .card-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 576px) {
  .card-grid {
    grid-template-columns: 1fr;
  }
}

.card-grid > * {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.card-grid > * .card {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 페이징 버튼 */
.pagination {
  text-align: center;
  margin: 1rem 0;
}

.pagination button {
  margin: 0 0.25rem;
  padding: 0.5rem 0.75rem;
  border: 1px solid #ccc;
  background: #fff;
  cursor: pointer;
}

.pagination button.active {
  background: #4caf50;
  color: #fff;
  border-color: #4caf50;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>