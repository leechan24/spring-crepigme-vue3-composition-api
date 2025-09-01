<template>
  <div>
    <h3>메뉴 관리</h3>
    <ul>
      <li v-for="menu in menus" :key="menu.id">
        <input v-model="menu.name" />
        <input v-model="menu.path" />
        <button @click="updateMenu(menu)">수정</button>
        <button @click="deleteMenu(menu.id)">삭제</button>
      </li>
    </ul>

    <h4>새 메뉴 추가</h4>
    <input v-model="newMenu.name" placeholder="이름" />
    <input v-model="newMenu.path" placeholder="경로" />
    <button @click="addMenu()">추가</button>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue';
import { useStore } from 'vuex';

const store = useStore();
const menus = computed(() => store.getters['menu/GET_MENUS']);

const newMenu = reactive({ name: '', path: '' });

const addMenu = async () => {
  if (!newMenu.name || !newMenu.path) return alert('값을 입력하세요');
  await store.dispatch('menu/addMenu', { ...newMenu });
  newMenu.name = '';
  newMenu.path = '';
};

const updateMenu = async (menu) => {
  await store.dispatch('menu/updateMenu', menu);
};

const deleteMenu = async (id) => {
  if (confirm('삭제하시겠습니까?')) {
    await store.dispatch('menu/deleteMenu', id);
  }
};
</script>
