<script setup>

import {onMounted, reactive, ref, watch} from "vue";
import {CaretLeft, Plus} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {v4 as uuidV4} from 'uuid'
const props = defineProps({
  data: { type: Object },
  rect: {type: Object},
});
const innerData = ref([]);
const reactiveData = ref([]);
reactiveData.value = innerData.value;
let lastData = {
  id:null,
  arr:[],
  f:null,
};
const namePath = ref([]);
const showDialog = ref(false);
const newName = ref('');
const needRect = ref(false);
let emit = defineEmits([
    'updateData',
    'jumpToPage',
]);
const goDeep = function (id) {
  for(let i = 0; i < reactiveData.value.length; i++) {
    if(reactiveData.value[i].id === id) {
      namePath.value.push(reactiveData.value[i].name);
      lastData = {
        id: id,
        arr:reactiveData.value,
        f: lastData,
      }
      reactiveData.value = reactiveData.value[i].children;
      return;
    }
  }
  ElMessage.error('未找到该笔记项');
}
const goBack = function () {
  if(lastData.f === null)ElMessage.error('回退失败');
  else{
    namePath.value.splice(namePath.value.length-1, 1);
    reactiveData.value = lastData.arr;
    lastData = lastData.f;
  }
}
const createNote = (obj) => {
  reactiveData.value.push(obj);
  showDialog.value = false;
  ElMessage.success('添加笔记成功');
}
const handleClickAdd = (link) => {
  if(newName.value.length > 0){
    let obj = {
      id: uuidV4(),
      name: newName.value,
      rect: null,
      children: [],
    };
    if(link){
      obj.rect = props.rect;
      needRect.value = false;
    }
    createNote(obj);
    newName.value = "";
  }else{
    ElMessage.error('笔记内容不能为空');
  }
}
const handleCreateLinkedNote = ()=>{
  needRect.value = true;
  showDialog.value = true;
}
const handleUpdate = ()=>{
  emit('updateData',innerData.value);
}
const jumpPage = (option)=>{
  emit('jumpToPage',option)
}
watch(props.data, (value) => {
  if(value){
    innerData.value = JSON.parse(JSON.stringify(value.note));
    reactiveData.value = innerData.value;
    namePath.value.splice(0, namePath.value.length);
    lastData = {
      id:null,
      arr:[],
      f:null,
    };
  }else{
    innerData.value = [];
    reactiveData.value = innerData.value;
    namePath.value.splice(0, namePath.value.length);
    lastData = {
      id:null,
      arr:[],
      f:null,
    };
  }
});

defineExpose({
  handleCreateLinkedNote
})
onMounted(()=>{
  if(props.data.note !== null){
    innerData.value = JSON.parse(JSON.stringify(props.data.note));
    reactiveData.value = innerData.value;
  }else{
    innerData.value = [];
    reactiveData.value = innerData.value;
    namePath.value.splice(0, namePath.value.length);
    lastData = {
      id:null,
      arr:[],
      f:null,
    };
  }
})
</script>

<template>
  <div class="note-container">
    <el-container>
      <el-header style="height: 60px">
        <el-button color="#3621ef" @click="handleUpdate">保存更改</el-button>
      </el-header>
      <el-main>
        <el-scrollbar>
          <div v-if="namePath.length > 0" class="item-div" >
            <div style="display: flex; align-items: center; justify-content: flex-end;" class="item-inner">
              回到上一级<el-icon @click="goBack" class="clickable back-icon"><CaretLeft/></el-icon>
            </div>
          </div>
          <div v-for="(item,index) in reactiveData" class="item-div">
            <el-row>
              <el-col :span="20">
                <div class="item-inner clickable" @click="goDeep(item.id)" style="font-size: large; color: #1a1a1a">
                  {{item.name}}
                </div>
              </el-col>
              <el-col :span="4">
                <el-button icon="Search" color="#3621ef" v-if="item.rect!==null" @click="jumpPage(item.rect)" size="small"></el-button>
              </el-col>
            </el-row>
          </div>
          <div class="item-div">
            <el-row>
              <el-col :span="20">
                <div class="item-inner">
                  按P键可框选区域创建关联式笔记
                </div>
              </el-col>
              <el-col :span="4">
                <el-button color="#3621ef" icon="Plus" @click="()=>{showDialog=true}" size="small"></el-button>
              </el-col>
            </el-row>
          </div>
        </el-scrollbar>
        <el-dialog v-model="showDialog" @closed="()=>{newName='';}" title="输入笔记内容" width="500">
          <el-input v-model="newName"></el-input>
          <div style="display: flex; box-sizing: border-box; padding:10px; width: 100%; align-items: center; justify-content: flex-end;">
            <el-button color="#3621ef" @click="handleClickAdd(needRect)">添加笔记</el-button>
          </div>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
.note-container {
  width: 100%;
  height: calc(max(560px,75vh));
}
.item-div{
  width: 100%;
  box-sizing: border-box;
  padding: 5px;
}
.item-inner{
  box-sizing: border-box;
  height: 100%;
  background: #f7f6bf;
  border-radius: 5px;
  padding: 0 5px 0 5px;
}
.clickable{
  cursor: pointer;
}
.back-icon{
  font-size: 20px;
  color: #3621ef
}
.back-icon:hover{
  color: #3621ef
}

</style>