<template>
  <div class="page-container">
    <DefaultDataGridWrapper 
      :personal-view="true" 
      storage-key="COMPLEX_GRID_SETTING"
      :data-source="employees"
    >
      <dx-column data-field="empNo" caption="사번" :width="80" alignment="center" />

      <dx-column caption="인적 사항" name="band_basic">
        <dx-column data-field="name" caption="성명" />
        <dx-column data-field="age" caption="나이" :width="60" />
      </dx-column>

      <dx-column data-field="deptName" caption="소속 부서" />

      <dx-column caption="성과지표 (2025)" name="band_kpi">
        <dx-column data-field="score" caption="고과점수" alignment="right" />
        <dx-column data-field="grade" caption="등급" alignment="center" />
        <dx-column data-field="bonus" caption="인센티브" 
          :calculate-cell-value="d => d.bonus.toLocaleString() + '원'" 
        />
      </dx-column>

      <dx-column data-field="joinDate" caption="입사일" data-type="date" />
      <dx-column data-field="lastLogin" caption="최종접속" data-type="datetime" />

      <dx-column caption="연락망" name="band_contact">
        <dx-column data-field="phone" caption="휴대전화" />
        <dx-column data-field="email" caption="이메일" />
      </dx-column>
    </DefaultDataGridWrapper>
  </div>
</template>

<script setup>
import { DxColumn } from 'devextreme-vue/data-grid';
import DefaultDataGridWrapper from './DefaultDataGridWrapper.vue';

const employees = [
  { empNo: 'A001', name: '강백호', age: 25, deptName: '영업팀', score: 95, grade: 'A', bonus: 5000000, joinDate: '2023-01-01', lastLogin: '2026-02-06 14:00', phone: '010-1234-5678', email: 'kang@corp.com' },
  { empNo: 'A002', name: '서태웅', age: 24, deptName: '개발팀', score: 88, grade: 'B', bonus: 3500000, joinDate: '2023-05-12', lastLogin: '2026-02-05 09:30', phone: '010-9999-8888', email: 'seo@corp.com' },
  { empNo: 'A003', name: '채치수', age: 29, deptName: '인사팀', score: 99, grade: 'S', bonus: 8000000, joinDate: '2020-11-20', lastLogin: '2026-02-06 18:20', phone: '010-5555-4444', email: 'chae@corp.com' }
];
</script>