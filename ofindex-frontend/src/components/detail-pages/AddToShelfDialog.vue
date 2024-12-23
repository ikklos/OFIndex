<script setup>

import {reactive, ref} from "vue";

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
const optionList = ref([{
  name: '书架',
  id: 0
}]);

const submitForm = async (formEl) => {
  if(!formEl)return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      //submit
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
    } else {
      throw new Error();
    }
  })
}
</script>

<template>
 <el-form ref="formRef" :model="formData">
   <el-form-item label="保存到">
     <el-select v-model="formData.bookListId">
       <el-option v-for="(item,index) in optionList" :label="item.name" :value="item.id"></el-option>
     </el-select>
   </el-form-item>
   <div style="padding: 10px; display: inline-flex; box-sizing: border-box"><el-button color="#3621ef" @click="()=>{
     showDialog = true;
   }">创建书单</el-button></div>

   <el-dialog title="创建书单" v-model="showDialog" style="width: 500px">
      <el-form ref="innerFormRef" :model="innerFormData" :rules="innerFormRules">
        <el-form-item label="名称" prop="bookListName">
          <el-input v-model="innerFormData.bookListName" placeholder="输入新书单的名称">
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