<template>
  <div>
    <hr/>
    <footer class="footer">
      <!-- <div class="footer-top">
        <h2 class="brand">Kikine</h2>
        <p class="tagline">Your style, your way.</p>
      </div> -->

      <div class="footer-links">
        <div class="link-section">
          <h4>고객센터</h4>
          <ul>
            <li>이메일: lc961109@gmail.com</li>
            <li>전화: 010-4503-1808</li>
            <li>운영시간: 월~금 09:00~18:00</li>
          </ul>
        </div>
        <div class="link-section">
          <h4>회사 정보</h4>
          <ul>
            <li>주소: 서울특별시 강남구 테헤란로 123</li>
            <li>사업자등록번호: 123-45-67890</li>
          </ul>
        </div>
        <!-- <div class="link-section">
          <h4>Follow Us</h4>
          <ul class="social">
            <li><a href="#">Instagram</a></li>
            <li><a href="#">Facebook</a></li>
            <li><a href="#">Twitter</a></li>
          </ul>
        </div> -->
      </div>

      <div class="footer-bottom">
        &copy; 2025 Kikine. All rights reserved.
      </div>
    </footer>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.footer {
  background-color: #f8f8f8;
  padding: 2px 2px; /* 위아래 2px로 줄임 */
  color: #333;
  font-family: "Noto Sans KR", sans-serif;
}
.footer-top {
  text-align: center;
  margin-bottom: 1px;
}
.brand {
  font-size: 24px;
  font-weight: bold;
  color: #2ecc71; /* 녹색 브랜드 색상 */
}
.tagline {
  font-size: 14px;
  margin-top: 5px;
  color: #555;
}
.footer-links {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin-bottom: 30px;
}
.link-section h4 {
  font-size: 16px;
  margin-bottom: 10px;
  font-weight: 600;
}
.link-section ul {
  list-style: none;
  padding: 0;
}
.link-section ul li {
  font-size: 14px;
  margin-bottom: 3px; /* 기존 5px → 3px */
}
.social li {
  display: inline;
  margin-right: 10px;
}
.footer-bottom {
  text-align: center;
  font-size: 12px;
  color: #999;
}
</style>
