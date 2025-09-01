<template>
    <div class="cre-detail">
      <div class="common-buttons">
        <button type="button" class="w3-button w3-round w3-blue-gray" @click="fnUpdate">수정</button>&nbsp;
        <button type="button" class="w3-button w3-round w3-red" @click="fnDelete">삭제</button>&nbsp;
        <button type="button" class="w3-button w3-round w3-gray" @click="fnList">목록</button>
      </div>
      <div class="cre-contents">
        <h3>{{ title }}</h3>
        <div>
          <strong class="w3-large">{{ name }}</strong>
          <br>
          <span>{{ sex }}</span>
        </div>
      </div>
      <div class="cre-contents">
        <span>{{ morph }}</span>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue';
  import axios from 'axios'; // axios 패키지를 기본값으로 import
  import { useRouter } from 'vue-router';

  export default {
    setup() {

    const route = useRouter();
    const routeParams = route.currentRoute.value.params;
    console.log(routeParams);
    const idx = routeParams.idx;

    //const  serverUrl = process.env.VUE_APP_SERVER_URL;  // http://localhost:8082 (.env 파일에 VUE_APP_? 로 설정해야한다. ) 
    const  serverUrl = import.meta.env.VITE_SERVER_URL;
    const requestBody = ref({});
    const name = ref('');
    const sex = ref('');
    const morph = ref('');

    //const idx = ref('');

    const fnGetView = () => {
    axios.get(serverUrl + '/api/cre/' + idx, {
        params: requestBody.value
    }).then((res) => {
        console.log(res.data);
        name.value = res.data.name;
        sex.value = res.data.sex;
        morph.value = res.data.morph;
    }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
        alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
        }
    });
    };

    const fnList = () => {
    delete requestBody.value.idx;
    route.push({
        name: 'list',
        query: requestBody.value
    });
    };

    const fnUpdate = () => {
    route.push({
        name: 'write',
        query: requestBody.value
    });
    };

    const fnDelete = () => {
    if (!confirm("삭제하시겠습니까?")) return;

    axios.delete(serverUrl + '/board/' + idx.value, {})
        .then(() => {
        alert('삭제되었습니다.');
        fnList();
        }).catch((err) => {
        console.log(err);
        });
    };

    onMounted(fnGetView);

    return {
    name,
    sex,
    morph,
    fnList,
    fnUpdate,
    fnDelete
      };
    }
  }
  </script>
  
  <style scoped>
  
  </style>