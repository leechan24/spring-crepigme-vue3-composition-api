<template>
  <div v-if="show" class="modal-overlay" @click.self="$emit('close')">
    <div class="modal">
      <h3>개체 등록</h3>
      <form @submit.prevent="submitForm" class="form-grid">

        <!-- 왼쪽 컬럼 -->
        <div class="form-group">
          <label>개체명*</label>
          <input v-model="form.name" type="text" required />
        </div>

        <div class="form-group">
          <label>Animal ID</label>
          <input v-model="form.animalId" type="text" placeholder="Unique identifier" />
        </div>

        <div class="form-group">
          <label>개체모프*</label>
          <input v-model="form.morph" type="text" required />
        </div>

        <div class="form-group">
          <label>특징</label>
          <input v-model="form.features" type="text" />
        </div>

        <div class="form-group">
          <label>태어난 날짜*</label>
          <input v-model="form.birthday" type="date" required />
        </div>

        <div class="form-group">
          <label>성별</label>
          <div class="toggle-group">
            <button type="button" @click="form.sex='M'" :class="{selected: form.sex==='M'}"><span class="male">♂ </span> 수컷</button>
            <button type="button" @click="form.sex='F'" :class="{selected: form.sex==='F'}"><span class="female">♀ </span>암컷</button>
            <button type="button" @click="form.sex='U'" :class="{selected: form.sex==='U'}">👶 미상</button>
          </div>
        </div>

        <div class="form-group">
          <label>가격*</label>
          <input v-model="form.price" type="number" required />
        </div>

        <div class="form-group">
          <label>색상</label>
          <input v-model="form.color" type="text" />
        </div>

        <div class="form-group">
          <label>모개체</label>
          <input v-model="form.mother" type="text" />
        </div>

        <div class="form-group">
          <label>부개체</label>
          <input v-model="form.father" type="text" />
        </div>

        <div class="form-group">
          <label>
            <input type="checkbox" v-model="form.petOnly" /> Pet Only
          </label>
        </div>

        <div class="form-group file-group">
          <label>개체 이미지</label>
          <input type="file" @change="onFileChange" accept="image/*" />
          <div v-if="form.imageUrl" class="preview">
            <img :src="form.imageUrl" alt="미리보기" />
          </div>
        </div>

        <div class="form-group file-group">
          <label>설명</label>
          <textarea v-model="form.description" rows="6" placeholder="Max 3000 characters"></textarea>
        </div>

        <div class="modal-actions">
          <button type="submit" class="add-btn">저장</button>
          <button type="button" class="cancel-btn" @click="$emit('close')">취소</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({ show: Boolean });
const emit = defineEmits(['close']);

const form = ref({
  name: '',
  animalId: '',
  morph: '',
  birthday: '',
  price: '',
  mother: '',
  father: '',
  features: '',
  sex: '',
  color: '',
  petOnly: false,
  image: null,
  imageUrl: '',
  description: ''
});

const onFileChange = (e) => {
  const file = e.target.files[0];
  if (!file) return;
  form.value.image = file;
  form.value.imageUrl = URL.createObjectURL(file);
};

const submitForm = () => {
  console.log('폼 데이터', form.value);
  alert('등록 완료!');
  Object.keys(form.value).forEach(k => form.value[k] = k==='petOnly'?false:'');
  emit('close');
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: #fff;
  padding: 1.5rem;
  border-radius: 0.5rem;
  max-width: 700px;
  width: 95%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal h3 {
  margin-bottom: 1rem;
  text-align: center;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 0.5rem;
  border-radius: 0.25rem;
  border: 1px solid #ccc;
}

.toggle-group {
  display: flex;
  gap: 0.5rem;
}

.toggle-group button {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid #aaa;
  background: #f5f5f5;
  cursor: pointer;
  border-radius: 0.25rem;
}

.toggle-group button.selected {
  background: #66bb6a;
  color: #fff;
}

.file-group {
  grid-column: 1 / -1;
}

.preview {
  margin-top: 0.5rem;
}

.preview img {
  max-width: 100%;
  max-height: 150px;
  object-fit: contain;
  border-radius: 0.25rem;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  margin-top: 1rem;
  grid-column: 1 / -1;
}

.add-btn {
  padding: 0.6rem 1rem;
  background: #66bb6a;
  color: #fff;
  border: none;
  border-radius: 0.35rem;
  cursor: pointer;
  font-weight: 700;
}

.add-btn:hover {
  background: #57a05a;
}

.cancel-btn {
  padding: 0.6rem 1rem;
  border: 1px solid #ccc;
  background: #fff;
  border-radius: 0.35rem;
  cursor: pointer;
  font-weight: 600;
}

.cancel-btn:hover {
  background: #f5f5f5;
}

/* 성별 색상 */
.male {
  color: #3498db;
  font-weight: 500;
}
.female {
  color: #e91e63;
  font-weight: 500;
}

@media (max-width: 576px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
