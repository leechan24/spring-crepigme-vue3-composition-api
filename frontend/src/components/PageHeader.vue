<template>
  <header>
    <nav class="header-nav">
      <!-- 왼쪽 로고 아이콘 -->
      <div class="logo">
        <router-link to="/">
          <img src="@/assets/logo.png" alt="Logo" class="logo-icon" />
        </router-link>
      </div>

      <!-- 메뉴 (데스크톱) -->
      <ul class="menu" :class="{ open: menuOpen }">
        <li v-for="menu in menus" :key="menu.id" class="menu-item">
          <router-link :to="menu.path" class="menu-link">
            {{ menu.name }}
          </router-link>
          <ul v-if="subMenus(menu.id).length" class="submenu">
            <li v-for="sub in subMenus(menu.id)" :key="sub.id">
              <router-link :to="sub.path" class="submenu-link">
                {{ sub.name }}
              </router-link>
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

      <!-- 오른쪽 로그인 -->
      <div class="login">
        <router-link to="/login" class="login-btn">로그인</router-link>
      </div>
    </nav>

    <!-- 메뉴 밑 구분선 -->
    <div class="menu-underline"></div>
  </header>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useStore } from "vuex";

const store = useStore();
const menuOpen = ref(false);

onMounted(() => {
  store.dispatch("menu/fetchMenus");
});

const menus = computed(() => store.getters["menu/GET_MENUS"]);
const subMenus = (parentId) => store.getters["menu/GET_SUBMENUS"](parentId);
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

/* 로고 아이콘 */
.logo-icon {
  font-size: 1.8rem;
  color: #28a745;
}

/* 메뉴 */
.menu {
  display: flex;
  list-style: none;
  gap: 2rem;
  margin: 0;
  padding: 0;
  align-items: center;
  height: 100%;
}

.menu-item {
  position: relative;
  display: flex;
  align-items: center;
  height: 100%;
}

.menu-link {
  text-decoration: none;
  color: #333;
  font-weight: 500;
  padding: 0 0.5rem;
  line-height: 60px;
  transition: color 0.2s;
}

.menu-link:hover {
  color: #28a745;
}

/* 서브메뉴 */
.submenu {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  background: #fff;
  border-radius: 8px;
  padding: 0.5rem 0;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  z-index: 1000;
}

.submenu li {
  padding: 0;
}

.submenu-link {
  display: block;
  padding: 0.5rem 1rem;
  color: #555;
  text-decoration: none;
  white-space: nowrap;
}

.submenu-link:hover {
  background: #f1f1f1;
}

.menu-item:hover .submenu {
  display: block;
}

/* 로그인 버튼 */
.login {
  display: flex;
  align-items: center;
  height: 100%;
}

.login-btn {
  padding: 0.5rem 1rem;
  background-color: #28a745;
  color: #fff;
  font-weight: 500;
  border-radius: 6px;
  text-decoration: none;
  transition: background-color 0.2s;
}

.login-btn:hover {
  background-color: #218838;
}

/* 햄버거 버튼 (모바일) */
.hamburger {
  display: none;
  flex-direction: column;
  gap: 5px;
  cursor: pointer;
}

.hamburger span {
  display: block;
  width: 25px;
  height: 3px;
  background: #333;
  border-radius: 2px;
}

/* 메뉴 밑 구분선 */
.menu-underline {
  height: 1px;
  background: #ccc;
  width: 100%;
}

/* 반응형 */
@media (max-width: 768px) {
  .menu {
    position: absolute;
    top: 60px;
    left: 0;
    right: 0;
    background: #f8f9fa;
    flex-direction: column;
    gap: 0;
    overflow: hidden;
    max-height: 0;
    transition: max-height 0.3s ease-in-out;
  }

  .menu.open {
    max-height: 500px; /* 충분히 큰 값 */
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
  }

  .submenu-link {
    padding-left: 2rem;
  }

  .hamburger {
    display: flex;
  }

  .login {
    display: none; /* 모바일에서는 로그인 햄버거 메뉴 안으로 넣을 수도 있음 */
  }
}

/* 로고 아이콘 */
.logo-icon {
  width: 50px; /* 원하는 크기로 조절 */
  height: auto; /* 비율 유지 */
}
</style>
