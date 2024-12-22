<script lang="js" setup>

import {reactive, ref} from "vue";
import axios from "axios";
import {Plus} from "@element-plus/icons-vue";

let UploadComponent = ref(null);
const PostEditData = reactive({
  title: '',
  text: '',
  pictures: [],
});
const PicturesRaw = ref([]);
const imageUrl = ref('');
const dialogVisible = ref(false);
const uploadForm = function (){
}
const uploadPicturesHttp = function (options){
  let data = {
    smfile:options,
    format: "json",
  };
  axios.post('/api/upload',data,{
    headers:{"Content-Type":"multipart/form-data", "Authorization": 'WQe6xnSzY6sbQlH0YMmEdFIxTvx7PxzE'}
  }).then(response => {
    if(response.status === 200){
      if(response.data.success === true || response.data.code === 'image_repeated'){
        options.onSuccess(response);
      }else{
        throw new Error('上传失败');
      }
    }else{
      throw new Error('请求失败，可能是网络原因');
    }
  }).catch(error => {
    options.onError(error);
  })
}
const handlePreview = function (uploadFile){
  imageUrl.value = uploadFile.url;
  dialogVisible.value=true;
}
const handleSuccess = function (response, file, fileList) {
  if(response.data.success === true){
    PostEditData.pictures.push(response.data.data.url);
  }else {
    PostEditData.pictures.push(response.data.images);
  }
}
</script>

<template>
  <div class="edit-area">
    <el-form>
      <el-row class="top-title-row">随便写写喽</el-row>
      <el-row class="input-row"><el-input v-model="PostEditData.title"
                                          class="title-input"
                                          placeholder="请输入标题"></el-input></el-row>
      <el-row class="input-row"><el-input v-model="PostEditData.text" type="textarea"
                                          :autosize="{minRows:4,maxRows:8}"
                                          placeholder="请输入内容"></el-input></el-row>
      <el-upload ref="UploadComponent" v-model:file-list="PicturesRaw"
                 :http-request="uploadPicturesHttp"
                 :on-preview="handlePreview"
                 :on-success="handleSuccess"
                 list-type="picture-card">
        <el-icon><Plus /></el-icon>
      </el-upload>
      <el-dialog v-model="dialogVisible">
        <img w-full :src="imageUrl" alt="Preview Image" />
      </el-dialog>
      <el-row class="button-row"><el-button @click="uploadForm" color="#3621ef">发布</el-button></el-row>
    </el-form>
  </div>
</template>

<style scoped>
.edit-area{
  box-sizing: border-box;
  padding: 20px;
  width: 50%;
  min-width: 600px;
  border: 1px solid #3c5cd7;
  border-radius: 24px;
  background: #f9fce8;
  box-shadow: 1px 2px 2px rgba(0, 0, 0, 0.2);
}
.top-title-row{
  box-sizing: border-box;
  padding: 20px;
  width: 100%;
  font-size: 16px;
  color: #292929;
}
.input-row{
  width: 100%;
  box-sizing: border-box;
  padding: 10px;
  align-items: center;
  justify-content: center;
}
.button-row{
  width: 100%;
  box-sizing: border-box;
  padding: 10px;
  align-items: center;
  justify-content: flex-end;
}
.input-row:deep(.el-input__wrapper){
  background: #f7f6bf;
}
.input-row:deep(.el-textarea__inner){
  background: #f7f6bf;
}
</style>