<template>
  <header>
    <nav class="header-nav">
      <!-- 로고 + 브랜드 -->
      <div class="logo">
        <router-link to="/" tabindex="-1">
          <img src="@/assets/logo.png" alt="Logo" class="logo-icon" />
        </router-link>
        <router-link to="/" class="brand-link" tabindex="-1">
          <h2 class="brand">Kikine</h2>
        </router-link>
      </div>

      <!-- 메뉴 (데스크톱) -->
      <ul class="menu" :class="{ open: menuOpen }">
        <li v-for="menu in menus" :key="menu.id" class="menu-item">
          <router-link :to="menu.path" class="menu-link">{{ menu.name }}</router-link>
          <ul v-if="subMenus(menu.id).length" class="submenu">
            <li v-for="sub in subMenus(menu.id)" :key="sub.id">
              <router-link :to="sub.path" class="submenu-link">{{ sub.name }}</router-link>
            </li>
          </ul>
        </li>
      </ul>

      <!-- 햄버거 버튼 (모바일) -->
      <div class="hamburger" @click="menuOpen = !menuOpen">
        <span></span>
        <span></span>
        <span></span>
      </div>

      <!-- 로그인/로그아웃 -->
      <div class="login">
        <template v-if="isLoggedIn">
          <div class="user-dropdown" @click="toggleDropdown">
            <span class="user-name">{{ user?.id || user?.username }}</span>
            <!-- SVG 화살표 아이콘 -->
            <svg class="dropdown-arrow" xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="6 9 12 15 18 9"/>
            </svg>
          </div>
          <ul v-if="dropdownOpen" class="dropdown-menu">
            <li><router-link to="/cre/manage" class="dropdown-link">내 크레 관리</router-link></li>
            <li><router-link to="/profile" class="dropdown-link">내 정보 수정</router-link></li>
            <li><router-link to="/change-password" class="dropdown-link">비밀번호 변경</router-link></li>
            <li><button @click="handleLogout" class="dropdown-link logout-btn">로그아웃</button></li>
          </ul>
        </template>
        <template v-else>
          <router-link to="/login" class="login-btn">로그인</router-link>
        </template>
      </div>
    </nav>

    <!-- 메뉴 밑 구분선 -->
    <div class="menu-underline"></div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

const store = useStore();
const router = useRouter();
const menuOpen = ref(false);
const dropdownOpen = ref(false);

// 메뉴 가져오기
onMounted(() => {
  store.dispatch("menu/fetchMenus");
});

const menus = computed(() => store.getters["menu/GET_MENUS"]);
const subMenus = (parentId) => store.getters["menu/GET_SUBMENUS"](parentId);

// 로그인 상태
const isLoggedIn = computed(() => store.getters["auth/isLoggedIn"]);
const user = computed(() => store.getters["auth/getUser"]);

// 로그아웃 처리
const handleLogout = async () => {
  try {
    await store.dispatch("auth/logout"); // Vuex에서 토큰 초기화
  } catch (err) {
    console.error("로그아웃 실패", err);
  } finally {
    dropdownOpen.value = false;
    router.push("/login");
  }
};

// 드롭다운 토글
const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value;
  const dropdown = document.querySelector(".user-dropdown");
  if (dropdown) dropdown.classList.toggle("open", dropdownOpen.value);
};

// 클릭 외부 영역 감지하여 드롭다운 닫기
const handleClickOutside = (event) => {
  const dropdown = document.querySelector(".user-dropdown");
  const menu = document.querySelector(".dropdown-menu");
  if (dropdown && menu && !dropdown.contains(event.target) && !menu.contains(event.target)) {
    dropdownOpen.value = false;
    dropdown.classList.remove("open");
  }
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style scoped>
.header-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2rem;
  /* background: #f8f9fa; */
  height: 60px;
  position: relative;
}

/* 로고 + 브랜드 */
/* router-link 내부 이미지 포커스 제거 */
.logo img, .brand-link {
  outline: none;
  user-select: none;
  display: block; /* inline-block 대신 block */
}
.logo { display: flex; align-items: center; gap: 10px; cursor: pointer;}
.logo-icon { width: 50px; height: auto; }
.brand { font-size: 24px; font-weight: bold; color: #2ecc71; margin: 0; }
.brand-link { text-decoration: none; }

/* 메뉴 */
.menu { display: flex; list-style: none; gap: 2rem; margin: 0; padding: 0; align-items: center; height: 100%; }
.menu-item { position: relative; display: flex; align-items: center; height: 100%; }
.menu-link { text-decoration: none; color: #333; font-weight: 500; padding: 0 0.5rem; line-height: 60px; transition: color 0.2s; }
.menu-link:hover { color: #28a745; }

/* 서브메뉴 */
.submenu { display: none; position: absolute; top: 100%; left: 0; background: #fff; border-radius: 8px; padding: 0.5rem 0; box-shadow: 0 4px 12px rgba(0,0,0,0.1); z-index: 1000; }
.submenu li { padding: 0; }
.submenu-link { display: block; padding: 0.5rem 1rem; color: #555; text-decoration: none; white-space: nowrap; }
.submenu-link:hover { background: #f1f1f1; }
.menu-item:hover .submenu { display: block; }

/* 현재 라우트와 일치하는 메뉴만 초록색 */
.menu-link.router-link-exact-active {
  color: #28a745;
  font-weight: 600;
}

/* 서브메뉴도 동일하게 */
.submenu-link.router-link-exact-active {
  color: #28a745;
  font-weight: 600;
}


/* 로그인 버튼 */
.login { display: flex; align-items: center; height: 100%; position: relative; }
.login-btn { padding: 0.5rem 1rem; background-color: #28a745; color: #fff; font-weight: 500; border-radius: 6px; text-decoration: none; transition: background-color 0.2s; }
.login-btn:hover { background-color: #218838; }

/* 사용자 드롭다운 */
.user-dropdown { display: flex; align-items: center; gap: 5px; cursor: pointer; padding: 0 0.5rem; height: 100%; line-height: 60px; font-weight: 500; color: #333; position: relative; }
.user-dropdown:hover { color: #28a745; }
.dropdown-arrow { transition: transform 0.3s ease; }
.user-dropdown.open .dropdown-arrow { transform: rotate(180deg); }

/* 드롭다운 메뉴 */
.dropdown-menu {
  position: absolute;
  top: 100%;  
  right: 0;   
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  list-style: none;
  padding: 0.5rem 0;
  margin: 0;
  z-index: 1000;
  min-width: 150px;
}
.dropdown-menu li { width: 100%; }

/* 공통 스타일 + flex 정렬 */
.dropdown-link, .logout-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  text-decoration: none;
  color: #333;
  background: none;
  border: none;
  cursor: pointer;
  box-sizing: border-box; /* 중요: 패딩 포함해서 전체 width */
}

/* router-link 내부도 블록 처리 */
.dropdown-link {
  display: flex;
  align-items: center;
  width: 100%;
  box-sizing: border-box;
}
.logout-btn { color: #e74c3c; }
.dropdown-link:hover, .logout-btn:hover { background: #f1f1f1; }

/* 아이콘 크기 */
.menu-icon { width: 16px; height: 16px; display: inline-block; }

/* 햄버거 버튼 (모바일) */
.hamburger { display: none; flex-direction: column; gap: 5px; cursor: pointer; }
.hamburger span { display: block; width: 25px; height: 3px; background: #333; border-radius: 2px; }

.menu-underline { height: 1px; background: #ccc; width: 100%; }

/* 모바일 */
@media (max-width: 768px) {
  .menu { position: absolute; top: 60px; left: 0; right: 0; background: #f8f9fa; flex-direction: column; gap: 0; max-height: 0; overflow: hidden; transition: max-height 0.3s ease-in-out; }
  .menu.open { max-height: 1000px; }
  .menu-item { width: 100%; }
  .menu-link { line-height: 50px; padding: 0 1rem; }
  .submenu { position: relative; top: 0; box-shadow: none; display: flex; flex-direction: column; max-height: 0; overflow: hidden; transition: max-height 0.3s ease-in-out; }
  .menu-item.open-submenu .submenu { max-height: 500px; }
  .submenu-link { padding-left: 2rem; line-height: 40px; }
  .hamburger { display: flex; flex-direction: column; gap: 5px; cursor: pointer; }
  .login { display: none; }
}
</style>