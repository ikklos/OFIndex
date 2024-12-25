<script setup>
import {reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {Plus} from "@element-plus/icons-vue";
import axios from "axios";
import axiosApp from "@/main.js";

//数据
const formData = reactive({
  name: '',
  author: '',
  isbn: '',
  description: '',
  cover: '',
  tags: [],
  bookClass: 0,
  pdfData: []
});
const formRef = ref(null);
//校验规则
const formRules = reactive({
  name: {
    required: true,
    message: '请输入书名',
    trigger: 'blur',
  },
  author: {
    required: true,
    message: '请输入作者',
    trigger: 'blur',
  },
  isbn: {
    required: true,
    message: 'isbn不能为空',
    trigger: 'blur',
  },
  cover: {
    required: true,
    message: '请上传封面',
    trigger: 'change',
  },
  bookClass: {
    required: true,
    message: '请选择分类',
    trigger: 'change',
  },
  pdfData: {
    required: true,
    message: '请上传文件',
    trigger: 'change',
  }
});
const classList = ref([
  {
    name: '小说',
    id: 1
  }
])
const imageData = ref([]);
const uploadPdfRef = ref(null);
const handleUploadCover = function (option) {
  console.log(JSON.stringify(option));
  let data = {
    smfile: option,
    format: "json"
  }
  axios.post('/api/upload', data, {
    headers: {"Content-Type": "multipart/form-data", "Authorization": 'WQe6xnSzY6sbQlH0YMmEdFIxTvx7PxzE'}
  }).then(response => {
    if (response.status === 200) {
      if (response.data.success === true || response.data.code === 'image_repeated') {
        option.onSuccess(response);
      } else {
        throw new Error('上传失败');
      }
    } else {
      throw new Error('请求失败，可能是网络原因');
    }
  }).catch(error => {
    ElMessage.error(error);
    option.onError(error);
  })
}
const handlePreview = function (uploadFile) {

}
const handleExceed = function (response, fileList) {
  ElMessage.warning('只能上传一张封面图');
}
const handleRemove = function (file, fileList) {
  formData.cover = null;
}
const handleSuccess = function (response, file, fileList) {
  console.log('success');
  if (response.data.success === true) {
    formData.cover = response.data.data.url;
  } else {
    formData.cover = response.data.images;
  }
}
const beforeUpload = function (file) {
  if (file.type !== 'application/pdf') {
    ElMessage.error('只能上传pdf!');
    return false;
  }
  return true;
}
const submitForm = (formEl, fileData) => {

  const form = {
    name: formData.name,
    author: formData.author,
    description: formData.description,
    cover: formData.cover,
    tags: formData.tags,
    isbn: formData.isbn,
    bookClass: formData.bookClass,
  }
  console.log(form);
  let data = new FormData();
  console.log(JSON.stringify(form));
  data.append('formData',new Blob([JSON.stringify(form)], {type: 'application/json'}),{contentType: 'application/json'});
  data.append('file', fileData.file, fileData.filename);
  axiosApp.post('/create/book', data).then(response => {
    if (response.data.message === 'Book created') {
      ElMessage.success('创建成功');
      fileData.onSuccess(response);
      formEl.resetFields();
    }
  }).catch(error => {
    ElMessage.error(error);
    fileData.onError(error);
  });
}

const createBook = async (formEl) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
        if (valid) {
          //submit
          uploadPdfRef.value.submit();
        }
      }
  )
}
const resetForm = (formEl) => {
  if (!formEl) return
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
                <el-icon>
                  <Plus/>
                </el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="简介" prop="description">
              <el-input type="textarea" v-model="formData.description" :autosize="{minRows: 6, maxRows: 10}"></el-input>
            </el-form-item>
            <el-form-item label="分类" prop="bookClass">
              <el-select v-model="formData.bookClass" placeholder="Select">
                <el-option v-for="(item,index) in classList" :key="index" :label="item.name"
                           :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="tags" prop="tags">
              <el-input-tag v-model="formData.tags"></el-input-tag>
            </el-form-item>
            <el-form-item label="上传pdf文件" prop="pdfData">
              <el-upload ref="uploadPdfRef"
                         v-model:file-list="formData.pdfData"
                         :limit="1"
                         :auto-upload="false"
                         :before-upload="beforeUpload"
                         :http-request="(option) => submitForm(formRef,option)">
                <el-button color="#3621ef">点击上传文件</el-button>
              </el-upload>
            </el-form-item>
            <el-button color="#3621ef" @click="createBook(formRef)">创建书目</el-button>
            <el-button color="#3621ef" @click="resetForm(formRef)">清空表单</el-button>
          </el-form>
        </div>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<style scoped>
.form-main {
  box-sizing: border-box;
  width: 100%;
  align-items: center;
  justify-content: center;
  justify-items: center;
}

.form-container {
  width: 50%;
  min-width: 680px;
  box-sizing: border-box;
  height: 60vh;
  min-height: 500px;
}
</style>