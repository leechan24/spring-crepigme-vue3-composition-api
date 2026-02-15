<template>
  <div class="grid-container">
    <div v-if="personalView" class="grid-toolbar" style="display: flex; justify-content: flex-end; margin-bottom: 10px;">
      <dx-button icon="columnchooser" text="개인화 설정" @click="openPopup" />
    </div>

    <dx-data-grid 
      ref="gridRef" 
      v-bind="$attrs" 
      :show-borders="true"
      :column-auto-width="true"
      :allow-column-reordering="true"
      :allow-column-resizing="true"
      @content-ready="onGridReady"
    >
      <slot />
    </dx-data-grid>

    <PersonalPopup
      v-if="popupVisible"
      v-model:visible="popupVisible"
      :columns="flatColumns"
      @save="applyAndSave"
      @reset="resetPopupData"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { DxDataGrid, DxButton } from 'devextreme-vue';
import PersonalPopup from './PersonalPopup.vue';

const props = defineProps({ 
  personalView: Boolean, 
  storageKey: { type: String, default: 'GRID_FINAL_SETTING' } 
});

const gridRef = ref(null);
const popupVisible = ref(false);
const flatColumns = ref([]);
const initialFlatList = ref([]);
let isFirstLoad = true;

// 1. 그리드 준비 완료 시 (초기 백업 및 저장된 설정 복구)
const onGridReady = (e) => {
  if (!isFirstLoad) return;
  const instance = e.component;
  
  // 소스코드상의 순수 초기 상태 백업
  const allCols = instance.getColumns().filter(c => c.dataField);
  initialFlatList.value = allCols.map(c => {
    const parent = instance.columnOption(c.ownerBand);
    return {
      dataField: c.dataField,
      caption: c.caption,
      parentName: parent ? (parent.name || parent.caption) : `solo_${c.dataField}`,
      visible: c.visible !== false
    };
  });

  // 저장된 설정 로드 및 복구
  const saved = localStorage.getItem(props.storageKey);
  if (saved) {
    try {
      const { groupOrder, columnVisibility } = JSON.parse(saved);
      instance.beginUpdate();

      // 가시성 먼저 복구
      Object.keys(columnVisibility || {}).forEach(f => {
        instance.columnOption(f, 'visible', columnVisibility[f]);
      });

      // 순서 복구 (일반 컬럼과 밴드 컬럼 구분)
      groupOrder?.forEach((name, idx) => {
        if (name.startsWith('solo_')) {
          // solo_로 시작하면 일반 컬럼이므로 실제 dataField에 index 부여
          const realField = name.replace('solo_', '');
          instance.columnOption(realField, 'visibleIndex', idx);
        } else {
          // 밴드(그룹)인 경우 밴드명에 index 부여
          instance.columnOption(name, 'visibleIndex', idx);
          instance.columnOption(name, 'visible', true); // 복구 시엔 우선 보이게 설정
        }
      });

      instance.endUpdate();
    } catch (err) {
      console.error("저장된 설정 복구 중 오류:", err);
    }
  }
  isFirstLoad = false;
};

// 2. 팝업 열기 (현재 그리드 상태 읽기)
const openPopup = () => {
  const instance = gridRef.value.instance;
  
  // 현재 최상위 레벨(부모 밴드 혹은 독립 컬럼)의 순서에 따라 정렬
  const sortedCols = instance.getColumns().filter(c => c.dataField).sort((a, b) => {
    const pA = instance.columnOption(a.ownerBand) || a;
    const pB = instance.columnOption(b.ownerBand) || b;
    return (pA.visibleIndex || 0) - (pB.visibleIndex || 0);
  });

  flatColumns.value = sortedCols.map(c => {
    const parent = instance.columnOption(c.ownerBand);
    return {
      dataField: c.dataField,
      caption: c.caption,
      parentName: parent ? (parent.name || parent.caption) : `solo_${c.dataField}`,
      visible: c.visible !== false
    };
  });
  popupVisible.value = true;
};

// 3. 팝업 데이터 초기화
const resetPopupData = () => {
  flatColumns.value = JSON.parse(JSON.stringify(initialFlatList.value));
};

// 4. 적용 및 저장 (순서 변경 핵심 로직)
const applyAndSave = (sortedFlatList) => {
  const instance = gridRef.value.instance;
  instance.beginUpdate();

  const groupOrder = [];
  const columnVisibility = {};
  const groupStats = new Map();

  // 1단계: 가시성 적용 및 그룹 정보 수집
  sortedFlatList.forEach(col => {
    instance.columnOption(col.dataField, 'visible', col.visible);
    columnVisibility[col.dataField] = col.visible;

    if (!groupStats.has(col.parentName)) {
      groupStats.set(col.parentName, { 
        hasVisibleChild: false, 
        isBand: !col.parentName.startsWith('solo_') 
      });
      groupOrder.push(col.parentName);
    }
    if (col.visible) groupStats.get(col.parentName).hasVisibleChild = true;
  });

  // 2단계: 최상위 레벨 순서(visibleIndex) 및 밴드 가시성 적용
  groupOrder.forEach((name, idx) => {
    const stats = groupStats.get(name);
    if (stats.isBand) {
      // 멀티헤더 그룹인 경우
      instance.columnOption(name, 'visible', stats.hasVisibleChild);
      instance.columnOption(name, 'visibleIndex', idx);
    } else {
      // 일반 독립 컬럼인 경우 (solo_ 제거 후 실제 필드에 인덱스 부여)
      const realField = name.replace('solo_', '');
      instance.columnOption(realField, 'visibleIndex', idx);
    }
  });

  localStorage.setItem(props.storageKey, JSON.stringify({ groupOrder, columnVisibility }));
  
  instance.endUpdate();
  instance.repaint();
  popupVisible.value = false;
};
</script>