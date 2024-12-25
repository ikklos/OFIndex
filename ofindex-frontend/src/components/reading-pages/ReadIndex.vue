<script setup>

import {computed, onMounted, reactive, ref, watch} from "vue";
import {useRoute,useRouter} from "vue-router";
import axiosApp from "@/main.js";
import VuePdfEmbed from 'vue-pdf-embed'
import {ElMessage, ElCollapseTransition} from "element-plus";
import {CaretLeft, EditPen, Minus, Plus} from "@element-plus/icons-vue";
import axios from "axios";
import NoteSideBar from "@/components/reading-pages/NoteSideBar.vue";
import DiagramSideBar from "@/components/reading-pages/DiagramSideBar.vue";

const route = useRoute();
const router = useRouter();
//数据
const currentPack = ref(-1);
const packData = reactive({
  bookId: parseInt(route.params.bookId),
  packId: (route.params.packId)?parseInt(route.params.packId):null,
  name: null,
  description: null,
  content: null,
  shared: false,
})
const packContent = ref({
  note:[],
  diagram:{
    nodeData: {
      id: 'c9ee977189f3b1f1',
      topic: 'Root',
      root: true,
      children: []
    }
  },
})
const packList = ref([]);
const newPackName = ref('');
const dragging = ref(true);


//控制pdf
const pdfWidth = ref(1241);
const pdfHeight = ref(1754);
const currentPage = ref(1);
const maxPage = ref(1);
const ebookUrl = ref('');

//控制元素是否显示
const showAddPackDialog = ref(false);
const showSideBar = ref(false);
const showMask = ref(false);
const showLightMask = ref(false);
const showYLMask = ref(false);
const loading = ref(false);

const tabPaneName = ref('side-note');

//子组件的Ref
const noteBarRef = ref(null);
const diagramBarRef = ref(null);
//框选的范围
const draggingRect = reactive({
  page: currentPage.value,
  x: 0,
  y: 0,
  width: 0,
  height: 0,
});

const packCount = computed(()=>{
  return packList.value.length;
});


const fillWidth = function () {
  const main = document.getElementById("read-index-main");
  if(main){
    const rect = main.getBoundingClientRect();
    pdfHeight.value = null;
    pdfWidth.value = rect.width-30;
  }
}

const fillHeight = function () {
  const main = document.getElementById("read-index-main");
  if(main){
    const rect = main.getBoundingClientRect();
    pdfHeight.value = rect.height-20;
    pdfWidth.value = null;
  }
}

const loadPack = (id) => {
  if(id !== undefined && id !== null){
    axiosApp.get('/pack/'+id).then(res=>{
        packData.name = res.data.name;
        packData.content = res.data.content;
        packData.description = res.data.description;
        let diagram = JSON.parse(res.data.content).diagram;
        let note = JSON.parse(res.data.content).note;
        packContent.value.diagram = diagram;
        packContent.value.note = note;
    }).catch((err)=>{
      ElMessage.error('加载资源包失败');
    })
  }
}

const loadPackList = ()=>{
  axiosApp.get('/user').then(res=>{
    axiosApp.get('/pack/user/'+res.data.userId).then(response=>{
      packList.value.splice(0,packList.value.length);
      for(let i=0; i < response.data.count; i++){
        packList.value.push(response.data.packs[i]);
      }
      if(response.data.count > 0 && currentPack.value === -1){
        currentPack.value= packList.value[0].packId;
      }
    }).catch((err)=>{
      ElMessage.error('获取资源包列表失败');
    })
  }).catch((err)=>{
    if(err.response.status === 601){
      ElMessage.error('登录已过期');
      router.push('/account/login');
    }
  })
}

const createPack = ()=>{
  if(newPackName.value.length > 0){
    let data = {
      packId: null,
      bookId: packData.bookId,
      name: newPackName.value,
      description: null,
      content: JSON.stringify({
        note:[],
        diagram:{
          nodeData: {
            id: 'c9ee977189f3b1f1',
            topic: 'Root',
            root: true,
            children: []
          }
        },
      }),
      shared: false,
    }
    axiosApp.post('/upload/pack',data).then(response => {
      ElMessage.success('创建成功');
      showAddPackDialog.value = false;
      loadPackList()
    }).catch((err)=>{
      if(err.response.status === 601){
        ElMessage.error('登录已过期');
        router.push('/account/login');
      }
    })
  }else{
    ElMessage.error('资源包名字不能为空')
  }
}

//完成content和packData的装载，上传
const updatePack = ()=>{
  console.log('packContent:',packContent.value);
  packData.content = JSON.stringify(packContent.value);
  packData.shareId = false;
  axiosApp.post('/upload/pack',packData).then(response => {
    if(response.status === 200){
      ElMessage.success('保存成功');
    }
  }).catch((err)=>{
    if(err.response.status === 601){
      ElMessage.error('登陆已过期');
      router.push('/account/login');
    }
  })
}
const clearMap = (obj)=>{
  if(!obj)return;
  if(obj.parent !== undefined){
    obj.parent = undefined;
  }
  if(obj.children !== undefined && obj.children !== null){
    for(let i = 0; i < obj.children.length; i++){
      clearMap(obj.children[i]);
    }
  }
}
watch(currentPack, (newVal, oldVal) => {
  if(newVal !== -1){
    loadPack(newVal);
    packData.packId = newVal;
  }else{
    packData.packId = null;
  }
})
onMounted(()=>{
  axiosApp.get('/load/ebook/'+packData.bookId,{
    responseType: 'blob',
  }).then((response)=>{
    let blobData = response.data;
    let localUrl = URL.createObjectURL(blobData);
    ebookUrl.value = localUrl;
  }).catch((err)=>{
    ElMessage.error('加载电子书失败');
  });
  loadPackList();
  if(currentPack.value !== -1){
    loadPack(currentPack.value);
  }
  addEventListener('keydown', (e)=>{
      if(e.key === 'ArrowRight'){
        if(currentPage.value < maxPage.value){
          currentPage.value += 1;
        }
      }else if(e.key === 'ArrowLeft'){
        if(currentPage.value > 1){
          currentPage.value -= 1;
        }
      }
  });
});

//处理鼠标动作
const onResizingMouseDown = function (event) {
  dragging.value = true;
  addEventListener('mousemove',onResizingMouseMove);
  addEventListener('mouseup',onResizingMouseUp);
}

const onResizingMouseUp = function (event) {
  dragging.value = false;
  removeEventListener('mousemove',onResizingMouseMove);
  removeEventListener('mouseup',onResizingMouseUp);
}

const onResizingMouseMove = function (event) {
  if (!dragging.value) return;
  let axisX = event.clientX;
  let element = document.getElementById('reading-page-side-bar-area');
  if(element){
    let rect = element.getBoundingClientRect();
    let width = axisX + 3 - rect.left;
    if(width >= 400){
      element.style.width = width + 'px';
    }
  }
}

const handleMouseDown = (event) => {
  let elementPdfArea = document.getElementById('pdf-area');
  let rect = null;
  if(elementPdfArea){
    rect = elementPdfArea.getBoundingClientRect();
    let x = event.clientX;
    let y = event.clientY;
    if(x < rect.left || x > rect.right || y < rect.top || y > rect.bottom){
      return;
    }
  }else{
    return;
  }
  showLightMask.value = true;
  let elementLightMask = document.getElementById('light-mask');
  if(elementLightMask){
    elementLightMask.style.left = event.clientX + 'px';
    elementLightMask.style.top = event.clientY + 'px';
    //计算相对位置，除以它的页面大小
    draggingRect.x = (event.clientX - rect.left)/rect.width;
    draggingRect.y = (event.clientY - rect.top)/rect.height;
  }else{
    showLightMask.value = false;
    return;
  }

  addEventListener('mousemove',handleMouseMove);
  addEventListener('mouseup',handleMouseUp);
}

const handleMouseUp = (event) => {
  noteBarRef.value.handleCreateLinkedNote(true);
  let elementLightMask = document.getElementById('light-mask');
  if(elementLightMask){
    elementLightMask.style.left = '0';
    elementLightMask.style.top = '0';
    elementLightMask.style.width = '0';
    elementLightMask.style.height = '0';
  }
  showLightMask.value = false;
  showMask.value = false;
  removeEventListener('mousedown',handleMouseDown);
  removeEventListener('mousemove',handleMouseMove);
  removeEventListener('mouseup',handleMouseUp);
}

const handleMouseMove = (event) => {
  let elementPdfArea = document.getElementById('pdf-area');
  let rect = null;
  if(elementPdfArea){
    rect = elementPdfArea.getBoundingClientRect();
    let x = event.clientX;
    let y = event.clientY;
    if(x < rect.left || x > rect.right || y < rect.top || y > rect.bottom){
      return;
    }
  }else{
    return;
  }
  let elementLightMask = document.getElementById('light-mask');
  if(elementLightMask){
    let maskRect = elementLightMask.getBoundingClientRect();
    if(event.clientX >= maskRect.left && event.clientY >= maskRect.top){
      elementLightMask.style.width = (event.clientX-maskRect.left)+'px';
      elementLightMask.style.height = (event.clientY-maskRect.top)+'px';
      draggingRect.width = (event.clientX-maskRect.left)/rect.width;
      draggingRect.height = (event.clientY-maskRect.top)/rect.height
    }
  }
}

const handleKeyPressP = (event) => {
  if(event.key === 'p' || event.key === 'P'){
    handleCreateLinkNote();
  }
}

const handleCreateLinkNote = () => {
  showMask.value = true;
  addEventListener('mousedown',handleMouseDown);
}

const handleLoaded = function ({numPages}){
  maxPage.value = numPages;
}

const handleTabChange = (name)=>{
  if(name === 'side-note'){
    addEventListener('keypress',handleKeyPressP);
  }else{
    removeEventListener('keypress',handleKeyPressP);
    const fn = async () => {
      return new Promise((resolve) => {
        setTimeout(() => {
          if(diagramBarRef.value){
            diagramBarRef.value.refresh();
          }
          resolve(null)
        }, 300);
      })
    }
    loading.value = true
    fn().finally(() => (loading.value = false))
  }
}

const handleSwitchShowSideBar = ()=>{
  showSideBar.value = !showSideBar.value
  if(showSideBar.value){
    if(tabPaneName.value === 'side-note'){
      addEventListener('keypress',handleKeyPressP);
    }
  }else{
    removeEventListener('keypress',handleKeyPressP);
  }
}

const handleJumpToPage = (option)=>{
  try{
    const pdf = document.getElementById("pdf-area");
    if(!pdf){
      throw new Error('获取pdf区域失败');
    }
    else {
      let rect = pdf.getBoundingClientRect();
      if(rect.width > rect.height){
        fillWidth();
      }else{
        fillHeight();
      }
      if(option.page <= maxPage.value && option.page >= 1){
        currentPage.value = option.page;
        const fn = async () => {
          return new Promise((resolve) => {
            setTimeout(() => {
              showYellowLightMask(option);
            },500);
          })
        }
        fn();
      }else {
        throw new Error('跳转失败，找不到页码');
      }
    }
  }catch(e){
    ElMessage.error('跳转失败'+ e );
    console.log(e);
  }
}

const showYellowLightMask = (option)=>{
  let mask = document.getElementById('yellow-light-mask');
  let pdfArea = document.getElementById('pdf-area');
  if(mask && pdfArea){
    let pdfRect = pdfArea.getBoundingClientRect();
    let x = pdfRect.left + pdfRect.width * option.x;
    let y = pdfRect.top + pdfRect.height * option.y;
    let width = pdfRect.width * option.width;
    let height = pdfRect.height * option.height;
    mask.style.left = x+'px';
    mask.style.top = y+'px';
    mask.style.width = width+'px';
    mask.style.height = height+'px';
    const func = async () => {
      return new Promise((resolve) => {
        setTimeout(() => {
          showYLMask.value = false;
        },800)
      })
    }
    showYLMask.value = true;
    func().finally(() => {
      mask.style.left = '0';
      mask.style.top = '0';
      mask.style.width = '0';
      mask.style.height = '0';
    });
  }
}

const jumpBack = () => {
  router.back();
}

watch(currentPage,(newVal, oldVal)=>{
  draggingRect.page = newVal;
});
</script>

<template>
  <el-container class="reading-page" style="z-index: 10">
    <el-header class="reading-page-header">
      <el-row style="height: 100%">
        <el-col :span="8" style="height: 100%">
          <div class="center-div">
            <el-input-number v-model="currentPage" :min="1" :max="maxPage"/>
          </div>
          <div class="center-div" style="font-size: 18px;">共{{maxPage}}页</div>
        </el-col>
        <el-col :span="8" style="height: 100%">
          <div class="center-div">
            <el-button color="#3621ef" @click="fillWidth">横向填充</el-button>
          </div>
          <div class="center-div">
            <el-button color="#3621ef" @click="fillHeight">纵向填充</el-button>
          </div>
        </el-col>
        <el-col :span="8" style="height: 100%">
          <div style="height: 100%; display: flex; align-items: center; justify-content: flex-end;">
            <el-button color="#3621ef"
                       style="border-radius: 50%; height: 40px; width: 40px; font-size: 20px"
                        @click="handleSwitchShowSideBar">
              <el-icon>
                <EditPen/>
              </el-icon>
            </el-button>
            <el-button style="height: 40px; width: 40px; font-size: 20px;" @click="jumpBack" color="#3621ef" circle>
              <el-icon>
                <CaretLeft></CaretLeft>
              </el-icon>
            </el-button>
          </div>
        </el-col>
      </el-row>
    </el-header>
    <el-container style="height: calc(100vh - max(8vh,60px));">
      <!--侧边展开栏-->
      <el-aside v-loading="loading" v-show="showSideBar" style="background-color:#fcfce5; min-width: 400px;" id="reading-page-side-bar-area">
        <el-container style="height: 100%; min-width: 400px;">
          <el-container >
            <!-- 如果还没有资源包则显示遮罩-->
            <div v-if="packCount === 0" class="side-bar-mask">
              <el-button icon="Plus" style=" height: 50px; width: 50px;"
                         color="#3621ef" circle plain @click="()=>{
                           showAddPackDialog = true;
                         }">
              </el-button>
            </div>
            <el-header v-if="packCount > 0">
              <el-row>
                <el-col :span="16">
                  <el-select v-model="currentPack">
                    <el-option v-for="(item,index) in packList" :label="item.packName" :value="item.packId">
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="8" style="display: flex; align-items: center; justify-content: flex-end;">
                  <el-button color="#3621ef" icon="Plus" @click="()=>{showAddPackDialog=true;}"></el-button>
                </el-col>
              </el-row>
            </el-header>
            <el-main v-if="packCount > 0">
              <el-tabs v-model="tabPaneName" tab-position="bottom" style="height: 100%; width: 100%;" @tab-change="handleTabChange">
                <el-tab-pane label="笔记" name="side-note">
                  <note-side-bar ref="noteBarRef" :data="packContent" :rect="draggingRect"
                                 @update-data="(newData)=>{
                                    packContent.note = JSON.parse(JSON.stringify(newData));
                                    updatePack();
                                 }"
                                 @jump-to-page="handleJumpToPage">
                  </note-side-bar>
                </el-tab-pane>
                <el-tab-pane label="思维导图" name="side-mind-map">
                  <diagram-side-bar ref="diagramBarRef" @update-data="(newData)=>{
                    packContent.diagram.nodeData = JSON.parse(JSON.stringify(newData.nodeData));
                    updatePack();
                  }" :data="packContent"></diagram-side-bar>
                </el-tab-pane>
              </el-tabs>
            </el-main>
            <el-dialog v-model="showAddPackDialog" title="创建资源包" width="500" @closed="()=>{
              newPackName = '';
            }">
              <el-input v-model="newPackName" placeholder="请输入资源包名称"></el-input>
              <div style="box-sizing: border-box; display: flex;
              align-items: center;
              justify-content: flex-end;
              width: 100%; padding: 10px">
                <el-button color="#3621ef" @click="createPack">创建</el-button>
              </div>
            </el-dialog>
          </el-container>
          <el-aside style="width:5px" class="drag-bar"
                    @mousedown="onResizingMouseDown"></el-aside>
        </el-container>
      </el-aside>
      <!--pdf显示区域-->
      <el-main class="reading-page-main" id="read-index-main">
        <vue-pdf-embed id='pdf-area' :source="ebookUrl" :height="pdfHeight" :width="pdfWidth" :page="currentPage" @loaded="handleLoaded"/>
      </el-main>
    </el-container>
  </el-container>
  <div class="dark-mask" v-show="showMask"></div>
  <div class="light-mask" id="light-mask" v-show="showLightMask"></div>
  <transition name="el-fade-in">
    <div class="yellow-light-mask" id="yellow-light-mask" v-show="showYLMask"></div>
  </transition>
</template>

<style scoped>
.reading-page{
  height: 100vh;
  width: 100vw;
  min-height: 500px;
  min-width: 800px;
}
.reading-page-header{
  background: #edeb67;
  color: #1a1a1a;
  height: calc(max(8vh,60px))
}
.center-div{
  box-sizing: border-box;
  padding: 10px;
  height: 100%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  justify-items: center;
}
.drag-bar{
  background: #d1c2fb;
}
.drag-bar:hover{
  cursor: ew-resize;
  background: #8e6df9;
}
.side-bar-mask{
  display: flex;
  height: 100%;
  width: 100%;
  background: #1b1800;
  align-items: center;
  justify-items: center;
  justify-content: center;
}
.reading-page-main{
  box-sizing: border-box;
  padding: 10px;
  align-items: center;
  justify-content: center;
  justify-items: center;
}
.reading-page-main::-webkit-scrollbar{
  width:10px;
  height:10px;
  /**/
}
.reading-page-main::-webkit-scrollbar-track{
  background: #FFFFFF;
  border-radius:2px;
}
.reading-page-main::-webkit-scrollbar-thumb{
  background: #3621ef;
  border-radius:10px;
}
.reading-page-main::-webkit-scrollbar-thumb:hover{
  background: #0012e2;
  cursor: pointer;
}
.reading-page-main::-webkit-scrollbar-corner{
  background: #0000dd;
}
.dark-mask{
  z-index: 100;
  position: absolute;
  top:0;
  left:0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.5);
}
.light-mask{
  z-index: 200;
  position: absolute;
  top:0;
  left:0;
  width: 0;
  height: 0;
  background: rgba(256,256,256,0.2);
}
.yellow-light-mask{
  z-index: 200;
  position: absolute;
  top:0;
  left:0;
  width: 0;
  height: 0;
  border-radius: 20px;
  background: rgba(231,226,15,0.5);
  transition: all 0.2s ease-in-out;
}
</style>