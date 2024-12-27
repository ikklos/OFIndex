<script setup>
import {useRoute, useRouter} from "vue-router";
import {reactive, onMounted, ref} from "vue";
import CommentItem from "@/components/index/CommentItem.vue";
import PostItem from "@/components/index/PostItem.vue";
import axiosApp from "@/axiosApp.js";
import {ElMessage} from "element-plus";

const route = useRoute();
const router = useRouter();
const editingComment = ref('');
const PageData = reactive({
  postId: parseInt(route.params.postId),
  postData: {
    pictures: []
  },
  comment: []
})
const loadData = () => {
  axiosApp().then(app => {
    app.get('/forum/post/' + route.params.postId).then(response => {
      PageData.postId = response.data.postId;
      PageData.postData = JSON.parse(JSON.stringify({
        postId: response.data.postId,
        posterId: response.data.posterId,
        avatar: response.data.posterAvatar,
        tags: response.data.tags,
        title: response.data.title,
        text: response.data.text,
        pictures: response.data.images,
        likes: response.data.likes,
        liked: response.data.liked,
        createTime: response.data.createTime,
      }));
    }).catch(err => {
      ElMessage.error('获取帖子内容失败');
      router.back();
    });
    app.get('/forum/comments/' + route.params.postId).then(response => {
      PageData.comment = JSON.parse(JSON.stringify(response.data.comments));
    }).catch(err => {
      ElMessage.error('获取评论失败');
      if (err.response.status === 601) {
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
}
onMounted(() => {
  //请求帖子详细信息
  loadData();
})
const submitComment = function (data) {
  if (data.text === '') {
    ElMessage.error('不能评论空字符');
    return;
  }
  //发送评论
  axiosApp().then(app => {
    app.post('/forum/comments/add', data).then(response => {
      ElMessage.success('评论成功');
      editingComment.value = '';
      loadData();
    }).catch(err => {
      ElMessage.error('操作失败');
      if (err.response.status === 601) {
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
}
const handleFlagLike = function (id, option) {
  if (option) {
    axiosApp().then(app => {
      app.get('/forum/post/like/' + id).then(response => {
        loadData();
        ElMessage.success('点赞成功，感谢支持');
      }).catch(error => {
        ElMessage.error('操作失败');
        if (error.response.status === 601) {
          ElMessage.error('登录信息已过期');
          router.push('/account/login');
        }
      })
    })
  } else {
    axiosApp().then(app => {
      app.delete('/forum/post/like/' + id).then(response => {
        loadData();
        ElMessage.success('已取消点赞');
      }).catch(error => {
        ElMessage.error('操作失败');
        if (error.response.status === 601) {
          ElMessage.error('登录信息已过期');
          router.push('/account/login');
        }
      })
    })
  }
}
const handleFlagLikeComment = (id, option) => {
  console.log(id, option);
  if (option) {
    axiosApp().then(app => {
      app.get('/forum/comments/like/' + id).then(response => {
        ElMessage.success('已点赞，感谢您的支持');
        loadData();
      }).catch(error => {
        ElMessage.error('操作失败');
        if (error.response.status === 601) {
          ElMessage.error('登录信息已过期');
          router.push('/account/login');
        }
      })
    })
  } else {
    axiosApp().then(app=>{
      app.delete('/forum/comments/like/' + id).then(response => {
        ElMessage.success('已取消点赞');
        loadData();
      }).catch(err => {
        ElMessage.error('操作失败');
        if (err.response.status === 601) {
          ElMessage.error('登录信息已过期');
          router.push('/account/login');
        }
      })
    })
  }
}
</script>

<template>
  <el-container>
    <el-main class="post-detail-main">
      <div class="post-card">
        <el-scrollbar>
          <div class="post-inner">
            <el-row class="post-area">
              <post-item :post-data="PageData.postData" :is-detail="true" @flag-like="handleFlagLike"></post-item>
            </el-row>
            <el-row class="edit-comment-row">
              <el-input v-model="editingComment" type="textarea"
                        placeholder="我要锐评@v@" class="comment-input"
                        :autosize="{minRows:4,maxRows:8}">
              </el-input>
            </el-row>
            <el-row class="button-row">
              <el-button color="#3621ef" @click="submitComment(
                  {
                          postId: parseInt(route.params.postId),
                          text: editingComment,
                          parent: null,
                        }
              )">发布评论
              </el-button>
            </el-row>
            <el-row class="comment-area">
              <div class="comment-area-inner">
                <el-collapse class="comment-collapse">
                  <el-collapse-item title="展开评论" class="comment-collapse-item">
                    <comment-item v-for="(comment,index) in PageData.comment" :key="index" :avatar="comment.userAvatar"
                                  :children_list="comment.comments"
                                  :id="comment.commentId"
                                  :content="comment.text"
                                  :likes="comment.likes"
                                  :liked="comment.liked"
                                  @add-comment="(commentText,id)=>{
                                    console.log(commentText,id);
                                    submitComment({
                                      postId: parseInt(route.params.postId),
                                      text: commentText,
                                      parent: id,
                                    })
                                  }"
                                  @flag-like="handleFlagLikeComment"></comment-item>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </el-row>
          </div>
        </el-scrollbar>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.post-detail-main {
  height: calc(100vh - max(8vh, 60px));
  align-items: center;
  text-align: center;
  justify-content: center;
  justify-items: center;
}

.post-card {
  width: 50%;
  height: 100%;
  min-height: 560px;
  min-width: 680px;
  box-sizing: border-box;
  padding: 20px;
  border-radius: 10px;
  border: 1px solid #c8cef4;
  box-shadow: 1px 1px 4px rgba(0, 0, 0, 0.3);
  background: #f9fce8;
}

.post-inner {
  width: 100%;
}

.post-area {
  width: 100%;
  align-items: center;
  justify-content: center;
  justify-items: center;
}

.edit-comment-row {
  box-sizing: border-box;
  padding: 10px;
}

.comment-input:deep(.el-textarea__inner) {
  background: #f2f5c6;
}

.button-row {
  box-sizing: border-box;
  padding: 10px;
  align-items: center;
  justify-items: flex-end;
  justify-content: flex-end;
}

.comment-area {
  box-sizing: border-box;
  padding: 20px 0 20px 0;
  width: 100%;
}

.comment-area-inner {
  width: 100%;
  background: #f2f5c6;
  border-radius: 10px;
  box-sizing: border-box;
  padding: 10px;
}

.comment-collapse-item {
  width: 100%;
}

.comment-collapse {
  border: none;
}

.comment-collapse-item :deep(.el-collapse-item__header) {
  border-top: none;
  background: #f2f5c6;
  border-bottom-color: #d7b83c;
  color: #3c5cd7;
}

.comment-collapse-item :deep(.el-collapse-item__wrap) {
  border: none;
  background: #f2f5c6;
}
</style>