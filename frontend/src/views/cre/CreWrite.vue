<template>
  <div class="board-detail">
    <div class="common-buttons">
      <button type="button" class="w3-button w3-round w3-blue-gray" @click="fnSave">저장</button>&nbsp;
      <button type="button" class="w3-button w3-round w3-gray" @click="fnList">목록</button>
    </div>
    <div class="board-sex">
      <input type="text" v-model="name" class="w3-input w3-border" placeholder="이름을 입력해주세요.">
      <input type="text" v-model="morph" class="w3-input w3-border" placeholder="모프를 입력해주세요." v-if="idx === undefined">
    </div>
    <div class="board-sex">
      <textarea cols="30" rows="10" v-model="sex" class="w3-input w3-border" style="resize: none;"></textarea>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  setup() {
    //const  serverUrl = process.env.VUE_APP_SERVER_URL;  // http://localhost:8082 (.env 파일에 VUE_APP_? 로 설정해야한다. ) 
    const  serverUrl = import.meta.env.VITE_SERVER_URL;
    const name = ref('');
    const birthday = ref('');
    const sex = ref('');
    const morph = ref('');
    const mother = ref('');
    const father = ref('');
    const homeYn = ref('');
    const etc = ref('');
    const idx = ref(0);
    const price = ref('');
    const salePrice = ref('');

    const requestBody = ref({});

    const fnGetView = () => {
      if (idx.value !== undefined) {
        axios.get(serverUrl + '/cre/' + idx.value, {
          params: requestBody.value
        }).then((res) => {
          name.value = res.data.name;
          morph.value = res.data.morph;
          sex.value = res.data.sex;
        }).catch((err) => {
          console.log(err);
        });
      }
    };

    onMounted(fnGetView);

    const fnList = () => {
      delete requestBody.value.idx;
      this.$router.push({
        path: './list',
        query: requestBody.value
      });
    };

    const fnView = (idx) => {
      requestBody.value.idx = idx;
      this.$router.push({
        path: './detail',
        query: requestBody.value
      });
    };

    const fnSave = () => {
      let apiUrl = serverUrl + '/board';
      const form = {
        "idx": idx.value,
        "name": name.value,
        "sex": sex.value,
        "morph": morph.value
      };

      if (idx.value === undefined) {
        //INSERT
        this.$axios.post(apiUrl, form)
          .then((res) => {
            alert('글이 저장되었습니다.');
            fnView(res.data.idx);
          }).catch((err) => {
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
            }
          });
      } else {
        //UPDATE
        this.$axios.patch(apiUrl, form)
          .then((res) => {
            alert('글이 저장되었습니다.');
            fnView(res.data.idx);
          }).catch((err) => {
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
            }
          });
      }
    };

    return {
      serverUrl,
      name,
      birthday,
      sex,
      morph,
      price,
      mother,
      father,
      homeYn,
      etc,
      salePrice,
      idx,
      requestBody,
      fnGetView,
      fnList,
      fnView,
      fnSave
    };
  }
};
</script>

<style scoped>

</style>