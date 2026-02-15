<template>
  <dx-popup :visible="visible" title="컬럼 구성 설정" :width="400" :height="600" @hiding="$emit('update:visible', false)">
    <div class="popup-layout" style="display: flex; flex-direction: column; height: 100%;">
      <div style="flex: 1; overflow-y: auto; padding: 10px;">
        <dx-sortable handle=".drag-handle" @reorder="onReorder">
          <div v-for="group in localGroups" :key="group.parentName" class="group-box">
            <ul class="col-list">
              <li v-for="(col, idx) in group.columns" :key="col.dataField" :class="['col-item', { 'is-hidden': !col.visible }]">
                <div class="drag-zone">
                  <i v-if="idx === 0" class="dx-icon-dragvertical drag-handle"></i>
                </div>
                <span class="caption">{{ col.caption }}</span>
                <dx-button 
                  :icon="col.visible ? 'eyeopen' : 'eyeclose'" 
                  styling-mode="text" 
                  @click="col.visible = !col.visible" 
                />
              </li>
            </ul>
          </div>
        </dx-sortable>
      </div>
      
      <div class="footer">
        <dx-button text="초기화" icon="refresh" type="danger" styling-mode="outlined" @click="$emit('reset')" />
        <div class="btn-group">
          <dx-button text="적용 저장" type="success" @click="$emit('save', localGroups.flatMap(g => g.columns))" />
          <dx-button text="취소" @click="$emit('update:visible', false)" style="margin-left: 8px" />
        </div>
      </div>
    </div>
  </dx-popup>
</template>

<script setup>
import { ref, watch } from 'vue';
import { DxPopup, DxSortable, DxButton } from 'devextreme-vue';

const props = defineProps({ visible: Boolean, columns: Array });
const emit = defineEmits(['update:visible', 'save', 'reset']);
const localGroups = ref([]);

watch(() => props.columns, (val) => {
  const groups = [];
  const map = new Map();
  val.forEach(col => {
    if (!map.has(col.parentName)) {
      const g = { parentName: col.parentName, columns: [] };
      map.set(col.parentName, g);
      groups.push(g);
    }
    map.get(col.parentName).columns.push({ ...col });
  });
  localGroups.value = groups;
}, { immediate: true, deep: true });

const onReorder = (e) => {
  const list = [...localGroups.value];
  const [moved] = list.splice(e.fromIndex, 1);
  list.splice(e.toIndex, 0, moved);
  localGroups.value = list;
};
</script>

<style scoped>
.group-box { margin-bottom: 12px; border: 1px solid #ddd; border-radius: 6px; background: #fff; overflow: hidden; }
.col-list { list-style: none; padding: 0; margin: 0; }
.col-item { display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #f0f0f0; }
.col-item:last-child { border-bottom: none; }
.col-item.is-hidden { opacity: 0.4; background: #f9f9f9; }
.drag-zone { width: 35px; text-align: center; }
.drag-handle { cursor: grab; color: #337ab7; font-size: 18px; }
.caption { flex: 1; font-size: 13px; }
.footer { padding: 15px; border-top: 1px solid #eee; display: flex; justify-content: space-between; }
</style>