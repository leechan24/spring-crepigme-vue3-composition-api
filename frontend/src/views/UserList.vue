
<template>
  <div>
    <h2>User List</h2>
    <table border="1" cellspacing="0" cellpadding="5">
      <thead>
        <tr>
          <th>idx</th>
          <th>Name</th>
          <th>Age</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.idx">
          <td>{{ user.idx }}</td>
          <td>{{ user.name }}</td>
          <td>{{ user.age }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { UserApi } from '@/api/api'; // 기존 api.js 또는 api.ts 그대로 사용

const users = ref([]);

const fetchUsers = async () => {
  try {
    const res = await UserApi.list(); // 프록시 설정을 통해 /user/list 요청
    users.value = res.data;
  } catch (err) {
    console.error('User list fetch error:', err);
  }
};

onMounted(() => {
  fetchUsers();
});
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 0.5rem;
  text-align: left;
}
</style>
