<template>
  <header>
    <nav class="header-nav">
      <!-- 로고 + 브랜드 -->
      <div class="logo">
        <router-link to="/">
          <img src="@/assets/logo.png" alt="Logo" class="logo-icon" />
        </router-link>
        <router-link to="/" class="brand-link">
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
          <span class="user-name">{{ user?.id || user?.username }}</span>
          <button @click="handleLogout" class="login-btn">로그아웃</button>
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
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

const store = useStore();
const router = useRouter();
const menuOpen = ref(false);

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
    console.log("로그아웃 시작");
    await store.dispatch("auth/logout"); // Vuex에서 토큰 초기화
  } catch (err) {
    console.error("로그아웃 실패", err);
  } finally {
    router.push("/login");
  }
};
</script>

<style scoped>
.header-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2rem;
  background: #f8f9fa;
  height: 60px;
  position: relative;
}

/* 로고 + 브랜드 */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}
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

/* 로그인 버튼 */
.login { display: flex; align-items: center; height: 100%; }
.login-btn { padding: 0.5rem 1rem; background-color: #28a745; color: #fff; font-weight: 500; border-radius: 6px; text-decoration: none; transition: background-color 0.2s; }
.login-btn:hover { background-color: #218838; }

/* 햄버거 버튼 (모바일) */
.hamburger { display: none; flex-direction: column; gap: 5px; cursor: pointer; }
.hamburger span { display: block; width: 25px; height: 3px; background: #333; border-radius: 2px; }

.menu-underline { height: 1px; background: #ccc; width: 100%; }

/* 모바일 */
@media (max-width: 768px) {
  .menu {
    position: absolute;
    top: 60px;
    left: 0;
    right: 0;
    background: #f8f9fa;
    flex-direction: column;
    gap: 0;
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.3s ease-in-out;
  }

  .menu.open {
    max-height: 1000px; /* 충분히 크게 잡아서 서브 메뉴도 보이도록 */
  }

  .menu-item {
    width: 100%;
  }

  .menu-link {
    line-height: 50px;
    padding: 0 1rem;
  }

  .submenu {
    position: relative;
    top: 0;
    box-shadow: none;
    display: flex; /* flex로 변경 */
    flex-direction: column;
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.3s ease-in-out;
  }

  .menu-item.open-submenu .submenu {
    max-height: 500px; /* 서브 메뉴 펼치기 */
  }

  .submenu-link {
    padding-left: 2rem;
    line-height: 40px;
  }

  .hamburger {
    display: flex;
    flex-direction: column;
    gap: 5px;
    cursor: pointer;
  }

  .login {
    display: none;
  }
}
</style>
