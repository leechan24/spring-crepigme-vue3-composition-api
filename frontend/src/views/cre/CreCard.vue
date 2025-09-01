<template>
  <div class="card shadow-sm">
    <div class="img-wrapper">
      <span
        v-if="item.imgPath"
        class="img"
        :style="{ backgroundImage: `url(${item.imgPath})` }"
      />
      <span v-else class="img img-placeholder">No Image</span>
    </div>
    <div class="card-body">
      <p class="card-text">{{ item.name }}</p>
      <div class="d-flex justify-content-between align-items-center">
        <div class="btn-group">
          <button type="button" class="btn btn-sm btn-primary">구입하기</button>
        </div>
        <small class="price text-muted">{{ item.morph }}</small>
        <small class="discount badge bg-danger">{{ item.sex }}</small>
        <small class="real text-danger">{{ item.price }}원</small>
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
}

.img-wrapper {
  width: 100%;
  padding-top: 100%; /* 1:1 비율 */
  position: relative;
  margin-bottom: 10px;
}

.img, .img-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 0.25rem;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  color: #6c757d;
  background-color: #e9ecef;
}

.card-body .price {
  text-decoration: line-through;
}
</style>
