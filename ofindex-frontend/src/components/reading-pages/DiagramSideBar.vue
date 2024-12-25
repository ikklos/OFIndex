<script setup>

import {TinyMindMap} from "@opentiny/vue";
import {onMounted, ref, watch} from "vue";

const props = defineProps({
  data: {type: Object}
})

const emit = defineEmits(['updateData']);
const render = ref(null);
const mindMap = ref(null);
const innerData = ref({
  nodeData: {
    id: 'c9ee977189f3b1f1',
    topic: 'Root',
    root: true,
    children: []
  }
});
const dialogVisible = ref(false);
const update = () => {
  console.log(render.value.getData());
  emit('updateData',render.value.getData());
}
const onCreate = (instance) => {
  render.value = instance;
}
const refresh = () => {
  if(render.value){
    render.value.init(innerData.value);
  }
}
watch(props.data, (newVal, oldVal) => {
  innerData.value = JSON.parse(JSON.stringify(props.data.diagram));
  refresh();
})
onMounted(()=>{
  if(props.data){
    innerData.value = JSON.parse(JSON.stringify(props.data.diagram));
    refresh();
  }
});
defineExpose({
  refresh
});
</script>

<template>
  <el-button @click="refresh" color="#3621ef">定位到根节点</el-button>
  <el-button color="#3621ef" @click="update">保存更改</el-button>
  <el-button @click="()=>{
    dialogVisible = true;
  }" color="#3621ef">这玩意咋用</el-button>
  <el-dialog v-model="dialogVisible" width="400" title="思维导图快捷键说明">
    <p>
      鼠标左键选中节点
    </p>
    <p>
      Tab键添加子节点
    </p>
    <p>
      Enter键添加同级节点
    </p>
    <p>
      Delete键删除节点
    </p>
    <p>
      Ctrl+C/V复制粘贴
    </p>
    <p>
      Tab键添加子节点
    </p>
    <p>
      鼠标滚轮+shift实现左右滚动
    </p>
    <p>
      通过鼠标滚轮滚动，也可以用鼠标拖动节点
    </p>
    <p style="color:#d22d2d">tips:1.如果画面丢失看不到任何节点，只是渲染位置不太对劲，按“定位到根节点”即可解决</p>
    <p style="color:#d22d2d">2.记得多点击保存按钮，我才不写自动保存o((>ω< ))o</p>
  </el-dialog>
  <tiny-mind-map @create="onCreate" ref="mindMap" class="mind-map-content" v-model="innerData">
  </tiny-mind-map>
</template>

<style scoped>
.mind-map-content{
  box-sizing: border-box;
  width: 100%;
  height: calc(max(560px,75vh));
  padding: 10px 0 10px 0;
}
</style>