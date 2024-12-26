<script setup>

import {onMounted, reactive, ref} from "vue";
import axiosApp from "@/axiosApp.js";
import {ElMessage} from "element-plus";
import {useRoute, useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();
const emit = defineEmits(["addOk"]);
const showDialog = ref(false);
const formRef = ref(null);
const innerFormRef = ref(null);
const formData = reactive({
  bookListId: 0,
});
const innerFormData = reactive({
  bookListName: '',
});
const innerFormRules = reactive({
  bookListName:{
    required: true,
    message: '名称不能为空',
    trigger: 'blur',
  }
})
//书单列表
const optionList = ref([]);

const submitForm = async (formEl) => {
  if(!formEl)return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      axiosApp().then(app=>{
        app.post('/shelf/add',{
          booklistId: formData.bookListId,
          bookId: parseInt(route.params.bookId),
        }).then((response)=>{
          ElMessage.success('添加成功');
          emit('addOk');
        }).catch(error=>{
          ElMessage.error('操作失败');
          if(error.response.status === 601){
            ElMessage.error('登录信息已过期');
            router.push('/account/login');
          }else if(error.response.status === 602){
            ElMessage.error('已经添加过了！');
          }
        })
      })
    } else {
      throw new Error();
    }
  })
}
const createBookList = async (formEl) => {
  if(!formEl)return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      //submit
      axiosApp().then(app=>{
        app.post('/shelf/create',{
          name: innerFormData.bookListName,
        }).then((response)=>{
          ElMessage.success('创建成功');
          showDialog.value = false;
          getShelfInfo();
        }).catch(error=>{
          console.log(error.message);
          ElMessage.error('创建失败');
          if(error.response.status === 601){
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
const getShelfInfo = ()=>{
  optionList.value.splice(0,optionList.value.length);
  axiosApp().then(app=>{
    app.get('/shelf').then((res) => {
      for(let i = 0; i < res.data.count; i++) {
        optionList.value.push({
          name: res.data.items[i].name,
          id: res.data.items[i].shelfId,
        });
      }
      formData.bookListId = optionList.value[0].id;
    }).catch((err)=>{
      ElMessage.error('获取书架信息失败');
      if(err.response.status === 601){
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
}
onMounted(()=>{
  getShelfInfo();
})
</script>

<template>
 <el-form ref="formRef" :model="formData" @submit.native.prevent>
   <el-form-item label="保存到">
     <el-select v-model="formData.bookListId">
       <el-option v-for="(item,index) in optionList" :label="item.name" :value="item.id"></el-option>
     </el-select>
   </el-form-item>
   <div style="padding: 10px; display: inline-flex; box-sizing: border-box"><el-button color="#3621ef" @click="()=>{
     showDialog = true;
   }">创建书单</el-button></div>

   <el-dialog title="创建书单" v-model="showDialog" style="width: 500px" @close="()=>{
     innerFormData.bookListName = '';
   }">
      <el-form ref="innerFormRef" :model="innerFormData" :rules="innerFormRules" @submit.native.prevent>
        <el-form-item label="名称" prop="bookListName">
          <el-input v-model="innerFormData.bookListName" placeholder="输入新书单的名称" >
          </el-input>
        </el-form-item>
        <el-button color="#3621ef" @click="createBookList(innerFormRef)">创建</el-button>
      </el-form>
   </el-dialog>
   <div style="padding: 10px; display: inline-flex; box-sizing: border-box">
     <el-button @click="submitForm(formRef)" color="#3621ef">确认</el-button>
   </div>
 </el-form>
</template>

<style scoped>

</style>