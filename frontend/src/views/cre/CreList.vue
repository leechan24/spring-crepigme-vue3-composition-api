<template>
  <div class="home">
    <!-- 검색 + 정렬 조건 -->
    <div class="filters">
      <!-- 검색조건 -->
      <select v-model="filters.morph">
        <option value="">모프 전체</option>
        <option v-for="m in morphOptions" :key="m" :value="m">{{ m }}</option>
      </select>

      <select v-model="filters.sex">
        <option value="">성별 전체</option>
        <option value="M">수컷</option>
        <option value="F">암컷</option>
      </select>

      <!--  정렬조건 (오른쪽 끝으로) -->
      <div class="sort-wrapper">
       <!--  <label for="sortKey">정렬:</label>
        <select v-model="sortKey" id="sortKey">
          <option value="">기본</option>
          <option value="morph">모프</option>
          <option value="sex">성별</option>
          <option value="birthday">생일</option>
        </select> -->

        <!-- 추가 버튼 -->
        <button class="add-btn" @click="showModal = true">개체 등록</button>
      </div>
    </div>

    <!-- 카드 리스트 -->
    <div class="card-grid">
      <Card v-for="(item, idx) in list" :key="idx" :item="item" />

      <!--  부족한 칸 placeholder -->
      <div
        v-for="n in (size - list.length)"
        :key="'ph-' + n"
        class="card placeholder"
      ></div>
    </div>
    <!-- CreRegisterModal 호출 -->
    <CreRegisterModal :show="showModal" @close="showModal = false" />

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

<script setup>
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
import { useRoute, useRouter } from 'vue-router';
import Card from '@/views/cre/CreCard.vue';
import CreRegisterModal from '@/views/cre/CreRegisterModal.vue';

const route = useRoute();
const router = useRouter();
const store = useStore();

const showModal = ref(false);

const requestBody = ref({});

// 전체 리스트
const fullList = computed(() => store.getters['cre/GET_CRELIST'] || []);

//  검색 조건
const filters = ref({
  morph: '',
  sex: ''
});

//  정렬 조건
const sortKey = ref('');

//  morph 옵션
const morphOptions = computed(() => {
  const morphs = fullList.value.map((i) => i.morph).filter(Boolean);
  return [...new Set(morphs)];
});

//  검색 + 정렬 적용된 리스트
const filteredList = computed(() => {
  let result = fullList.value.filter((i) => {
    return (
      (!filters.value.morph || i.morph === filters.value.morph) &&
      (!filters.value.sex || i.sex === filters.value.sex)
    );
  });

  // 정렬 적용
  if (sortKey.value) {
    result = [...result].sort((a, b) => {
      if (sortKey.value === 'birthday') {
        // 날짜는 최신순 (null은 뒤로)
        return (b.birthday || '').localeCompare(a.birthday || '');
      }
      return (a[sortKey.value] || '').localeCompare(b[sortKey.value] || '');
    });
  }

  return result;
});

// 페이지 설정
const page = ref(route.query.page ? parseInt(route.query.page) : 1);
const size = 6; // 한 페이지 6개

const totalPages = computed(() => Math.ceil(filteredList.value.length / size));

// 현재 페이지 리스트
const list = computed(() => {
  const start = (page.value - 1) * size;
  return filteredList.value.slice(start, start + size);
});

// 페이징 버튼 슬라이딩 (한 번에 5페이지)
const pageBlockSize = 5;
const startPage = computed(() =>
  Math.floor((page.value - 1) / pageBlockSize) * pageBlockSize + 1
);
const endPage = computed(() =>
  Math.min(startPage.value + pageBlockSize - 1, totalPages.value)
);

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

// 데이터 조회
const creList = async () => {
  try {
    await store.dispatch('cre/FETCH_CRELIST', requestBody);
  } catch (err) {
    console.error(err);
    alert('데이터 조회 중 오류가 발생했습니다.');
  }
};

creList();


</script>

<style scoped>
.filters {
  display: flex;
  gap: 0.5rem;
  margin: 1rem auto;
  max-width: 1200px;
  padding: 0 1rem;
  align-items: center;
  justify-content: space-between; /*  검색조건 왼쪽, 정렬 오른쪽 */
}

.filters select {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 0.25rem;
}

.sort-wrapper {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

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

/* placeholder → 빈칸 자리 유지 */
.placeholder {
  visibility: hidden;
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

/* 추가 버튼 */
.add-btn {
  padding: 0.6rem 1rem;            /* 클릭 영역 넓힘 */
  background: #66bb6a;             /* 밝은 그린 */
  color: #fff;
  border: none;
  border-radius: 0.35rem;          /* 살짝 둥글게 */
  cursor: pointer;
  font-weight: 700;                /* 조금 더 굵게 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
  transition: all 0.2s ease-in-out;
}

.add-btn:hover {
  background: #57a05a;             /* hover 시 색상 변화 */
  transform: scale(1.05);          /* 살짝 커지며 강조 */
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15); /* 그림자 강화 */
}
</style>
