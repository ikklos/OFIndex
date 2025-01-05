<script setup>

import LittleBalls from "@/components/index/LittleBalls.vue";
import {ref, onMounted, onBeforeUnmount, reactive} from 'vue';
import {useRouter} from 'vue-router'
import {DeleteFilled, Picture} from "@element-plus/icons-vue";
import axiosApp from "@/axiosApp.js";
import {ElMessage, ElMessageBox} from "element-plus";

let router = useRouter();
const menuItems = ref([
  {name: "历史"}, {name: "书架"}, {name: "回退"},{name: '创建书单'}
]);
const DataList = ref([{
  books:[]
}]);
const nowBookList = ref(0);
const formRef = ref(null);
const formRules = reactive({
  name:{
    required: true,
    message: '名称不能为空',
    trigger: 'blur'
  }
});
const formData = reactive({
  name: ''
});
const dialogVisible = ref(false);
const jumpToDetail = function(bookId) {
  router.push('/index/detail/book-detail/'+bookId);
}
const jumpToBookList = function(index) {
  nowBookList.value = index;
}
const handleLittleBallsClicked = function (index){
  if(index === 0){
    loadHistory();
  }
  if(index === 1){
    reloadData();
  }
  if(index === 2){
    nowBookList.value = 0;
  }
  if(index === 3){
    dialogVisible.value = true;
  }
}
const submitForm = async (formEl) => {
  if(!formEl)return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      axiosApp().then(app=>{
        app.post('/shelf/create',{
          name: formData.name,
        }).then(response=>{
          ElMessage.success('添加成功');
          reloadData();
          dialogVisible.value = false;
        }).catch(error=>{
          ElMessage.error('添加失败');
          if (error.response.status === 601){
            ElMessage.error('登录信息已过期');
            router.push('/account/login');
          }
        })
      })
    } else {
      throw new Error();
    }
  })
}
const reloadData = ()=>{
  DataList.value.splice(1,DataList.value.length-1);
  axiosApp().then(app=>{
    app.get('/shelf').then(res => {
      nowBookList.value = 0;
      DataList.value = JSON.parse(JSON.stringify(res.data.items));
    }).catch(err => {
      ElMessage.error('获取书架信息失败');
      if(err.response.status === 601){
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
}
const loadHistory = ()=>{
  DataList.value.splice(0,DataList.value.length);
  axiosApp().then(app=>{
    app.get('/shelf/history').then(res=>{
      nowBookList.value = 0;
      DataList.value.push({
        books: JSON.parse(JSON.stringify(res.data.items)),
      });
    }).catch(err => {
      ElMessage.error('获取浏览历史失败');
      if(err.response.status === 601){
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
}
const deleteBookList = (id)=>{
  ElMessageBox.confirm('确认删除吗，这会删除掉此书单下所有的书').then(()=>{
    axiosApp().then(app=>{
      app.delete('/shelf/remove',{
        params:{
          booklistId: id,
          bookId:null,
        }
      }).then(response=>{
        ElMessage.success('删除成功');
        reloadData();
      }).catch(error=>{
        ElMessage.error('删除失败');
        if(error.response.status === 601){
          ElMessage.error('登录信息已过期');
          router.push('/account/login');
        }
      })
    }).catch(error=>{
      console.log(error.message);
      ElMessage.error('出了小问题');
    });
  }).catch(err=>{
    console.log(err.message);
    ElMessage.error('出了小问题');
  })
}
const deleteBook = (id)=>{
  ElMessageBox.confirm('确认删除吗').then(()=>{
    axiosApp().then(app=>{
      app.delete('/shelf/remove',{
        params:{
          booklistId: DataList.value[nowBookList.value].shelfId,
          bookId: id,
        }
      }).then(response=>{
        ElMessage.success('删除成功');
        reloadData();
      }).catch(error=>{
        ElMessage.error('删除失败');
        if(error.response.status === 601){
          ElMessage.error('登录信息已过期');
          router.push('/account/login');
        }
      })
    }).catch(error=>{
      console.log(error.message);
      ElMessage.error('出了小问题');
    });
  }).catch((err)=>{
    console.log(err.message);
    ElMessage.error('出了小问题');
  })
}
onMounted(() => {
  reloadData();
})
</script>

<template>
  <el-container class="shelf-page-container">
    <el-main style="z-index: 9" id="shelf-main-field">
      <el-scrollbar style="height: 80vh; min-height: 520px;">
        <div class="items-container">
          <div v-for="(item,index) in DataList.slice(1)" :key="index" v-if="nowBookList === 0" class="data-item" >
            <el-row style="box-sizing: border-box; padding: 5px 0 5px 0; height: 8%; justify-content: flex-end">
              <el-button link style="color:#4825f6" @click="deleteBookList(item.shelfId)">
                <el-icon>
                  <DeleteFilled></DeleteFilled>
                </el-icon>
              </el-button>
            </el-row>
            <el-row style="width: 100%;height: 92%">
              <div class="item-inner" @click="jumpToBookList(index+1)" style="background: #06041c;
              box-sizing: border-box; border: 1px #0a062a solid;color: #ffffff; font-size: 18px">
                {{item.name}}
              </div>
            </el-row>
          </div>
          <div v-for="(item,index) in DataList[nowBookList].books" :key="index" class="data-item">
            <el-row style="box-sizing: border-box; padding: 5px 0 5px 0; height: 8%; justify-content: flex-end">
              <el-button link style="color:#4825f6" @click="deleteBook(item.bookId)">
                <el-icon>
                  <DeleteFilled></DeleteFilled>
                </el-icon>
              </el-button>
            </el-row>
            <el-row style="width: 100%;height: 92%">
              <div class="item-inner" @click="jumpToDetail(item.bookId)">
                <el-image :src="item.cover" fit="contain">
                  <template #error>
                    <div class="error-image">
                      <el-icon size="45px" color="#758a99">
                        <Picture/>
                      </el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
            </el-row>
          </div>
        </div>
      </el-scrollbar>
    </el-main>
    <el-aside style="z-index: 10">
      <div class="full-fix center-layout-row">
        <LittleBalls :menu-items="menuItems" :disperse="1" @item-clicked="handleLittleBallsClicked"/>
      </div>
    </el-aside>
    <el-dialog v-model="dialogVisible" title="创建书单" width="500">
      <el-form ref="formRef" :rules="formRules" :model="formData" @submit.native.prevent>
        <el-form-item label="名称" prop="name">
          <el-input placeholder="请输入书单名称" v-model="formData.name"></el-input>
        </el-form-item>
        <el-button color="#3621ef" @click="submitForm(formRef)">创建</el-button>
      </el-form>
    </el-dialog>
  </el-container>
</template>

<style scoped>
.shelf-page-container {
  background: #f3f2fe;
  min-width: 720px;
}
.el-aside {
  width: 5vw;
  min-width: 50px;
}
.items-container{
  display: flex;
  flex-wrap: wrap;
  width: calc(6*max(12vw,90px) + 7*20px);
}
.data-item {
  display: inline-block;
  width: 12vw;
  height: 37vh;
  min-height: 150px;
  min-width: 120px;
  padding: 15px 10px 15px 10px;
}
.item-inner{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.2s ease-in-out;
  border: 1px solid #edd1d8;
  box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}
.item-inner:hover {
  transform: scale(1.1, 1.1);
  transition: all 0.2s ease-in-out;
  cursor: pointer;
}
.el-image{
  max-width: 100%;
  max-height: 100%;
}
</style>