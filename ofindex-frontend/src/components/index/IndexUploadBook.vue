<script setup>
import {reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {Plus} from "@element-plus/icons-vue";
import axios from "axios";

//数据
const formData = ref({
  name: '',
  author: '',
  isbn:'',
  description: '',
  cover: '',
  tags: [],
  bookClass: null,
});
const formRef = ref(null);
//校验规则
const formRules = reactive({
  name:{
    required: true,
    message: '请输入书名',
    trigger: 'blur',
  },
  author:{
    required: true,
    message: '请输入作者',
    trigger: 'blur',
  },
  isbn:{
    required: true,
    message: 'isbn不能为空',
    trigger: 'blur',
  },
  cover:{
    required: true,
    message:'请上传封面',
    trigger:'change',
  },
  bookClass:{
    required: true,
    message: '请选择分类',
    trigger:'change',
  },
});
const classList = ref([
  {
    name: '小说',
    id: 1
  }
])
const imageData = ref([]);
const handleUploadCover = function (option){
  let data = {
    smfile: option,
    format: "json"
  }
  axios.post('/api/upload',data,{
    headers:{"Content-Type":"multipart/form-data", "Authorization": 'WQe6xnSzY6sbQlH0YMmEdFIxTvx7PxzE'}
  }).then(response => {
    if(response.status === 200){
      if(response.data.success === true || response.data.code === 'image_repeated'){
        option.onSuccess(response);
      }else{
        throw new Error('上传失败');
      }
    }else{
      throw new Error('请求失败，可能是网络原因');
    }
  }).catch(error => {
    Elmessge.error(error);
    option.onError(error);
  })
}
const handlePreview = function(uploadFile){

}
const handleExceed = function (response, fileList){
  ElMessage.warning('只能上传一张封面图');
}
const handleRemove = function (file, fileList){
  formData.value.cover = null;
}
const handleSuccess = function (response, file, fileList) {
  if(response.data.success === true){
    formData.value.cover = response.data.data.url;
  }else{
    formData.value.cover = response.data.images;
  }
}
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
const resetForm = (formEl) => {
  if(!formEl)return
  formEl.resetFields()
}
</script>

<template>
  <el-container>
    <el-main class="form-main">
      <el-scrollbar>
        <div class="form-container">
          <el-form ref="formRef"
                   :model="formData"
                   :rules="formRules"
                   label-width="auto"
          >
            <el-form-item label="书名" prop="name">
              <el-input v-model="formData.name"></el-input>
            </el-form-item>
            <el-form-item label="作者" prop="author">
              <el-input v-model="formData.author"></el-input>
            </el-form-item>
            <el-form-item label="ISBN" prop="isbn">
              <el-input v-model="formData.isbn"></el-input>
            </el-form-item>
            <el-form-item label="封面" prop="cover">
              <el-upload :http-request="handleUploadCover"
                         list-type="picture-card"
                         :limit="1"
                         :on-preview="handlePreview"
                         :on-success="handleSuccess"
                         :on-exceed="handleExceed"
                         :on-remove="handleRemove"
                         v-model="imageData">
                <el-icon><Plus/></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="简介" prop="description">
              <el-input type="textarea" v-model="formData.description" :autosize="{minRows: 6, maxRows: 10}"></el-input>
            </el-form-item>
            <el-form-item label="分类" prop="bookClass">
              <el-select v-model="formData.bookClass" placeholder="Select">
                <el-option v-for="(item,index) in classList" :key="index" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="tags" prop="tags">
              <el-input-tag v-model="formData.tags"></el-input-tag>
            </el-form-item>
            <el-button color="#3621ef" @click="submitForm(formRef)">创建书目</el-button>
            <el-button color="#3621ef" @click="resetForm(formRef)">清空表单</el-button>
          </el-form>
        </div>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<style scoped>
.form-main{
  box-sizing: border-box;
  width: 100%;
  align-items: center;
  justify-content: center;
  justify-items: center;
}
.form-container{
  width: 50%;
  min-width: 680px;
  box-sizing: border-box;
  height: 60vh;
  min-height: 500px;
}
</style>