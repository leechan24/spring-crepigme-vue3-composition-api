<template>
  <div class="card shadow-sm">
    <!-- 이미지 -->
    <div class="img-wrapper">
      <span
        v-if="item.imgPath"
        class="img"
        :style="{ backgroundImage: `url(${item.imgPath})` }"
      />
      <span v-else class="img img-placeholder">No Image</span>
    </div>

    <!-- 정보 -->
    <div class="card-body">
      <!-- 이름 + 모프 -->
      <div class="name-morph-row">
        <p class="name">{{ item.name }}</p>
        <p class="morph">{{ item.morph }}</p>
      </div>
      <div class="info-row">
        <div class="left-info">
          <span v-if="item.sex === 'M'" class="sex male">♂ Male</span>
          <span v-else-if="item.sex === 'F'" class="sex female">♀ Female</span>
          <span v-else class="sex baby">👶 Baby</span>

          <span v-if="item.birthday" class="birthday">{{ item.birthday }}</span>
        </div>
        <span class="price">{{ item.price }}만원</span>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  item: { type: Object, required: true }
});
</script>

<style scoped>
.card {
  display: flex;
  flex-direction: column;
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 0.5rem;
  overflow: hidden;
  background: #fff;
}

.img-wrapper {
  width: 100%;
  padding-top: 100%;
  position: relative;
}

.img, .img-placeholder {
  position: absolute;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background-size: cover;
  background-position: center;
  border-bottom: 1px solid #ddd;
  display: flex; align-items: center; justify-content: center;
  font-size: 1rem;
  color: #6c757d;
  background-color: #e9ecef;
}

.card-body {
  padding: 0.75rem;
}

/* 이름 + 모프 한 줄 정렬 */
.name-morph-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* name → 왼쪽 정렬, 더 크게 */
.name {
  font-weight: 700;
  font-size: 1.3rem; /* morph보다 크게 */
  margin-bottom: 0.5rem;
  color: #111;
  text-align: left;
}

/* morph → 오른쪽 정렬, 기존보다 살짝 작게 유지 */
.morph {
  font-weight: 700;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  color: #111;
  text-align: right;
}

/* info-row: 성별 + 생일 + 가격 */
.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.left-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
}

/* 성별 색상 */
.sex.male {
  color: #3498db;
  font-weight: 500;
}
.sex.female {
  color: #e91e63;
  font-weight: 500;
}
.sex.baby {
  color: #f39c12;
  font-weight: 500;
}

/* 생일 */
.birthday {
  font-size: 0.85rem;
  color: #555;
}

/* 가격 → 좀 더 크게 강조 */
.price {
  font-weight: 700;
  font-size: 1.2rem; /* morph, 성별 대비 크기 업 */
  color: #2ecc71;
}
</style>
