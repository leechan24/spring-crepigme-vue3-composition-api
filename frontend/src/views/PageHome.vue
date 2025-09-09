<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/daram.jpg" />
  </div>
</template>

<script>
export default {
  name: 'PageHome'
}
</script>

<style scoped>
.home {
  user-select: none;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  /* width: 100vw; */
  background-color: #ffffff; /* 공백 색상을 흰색으로 변경 */
  overflow: hidden;
}

.home img {
  display: block;          
  max-width: 100%;         /* 부모 너비에 맞추되 */
  max-height: 100%;        /* 부모 높이에도 맞춤 */
  height: auto;
  width: auto;
  object-fit: contain;     /* 비율 유지하면서 영역 안에 맞춤 */
  user-select: none;       /* 이미지 선택 방지 */
}
</style>