<script setup>
import{useRoute} from "vue-router";
import {reactive, onMounted} from "vue";

let route = useRoute();
const PostData = reactive({
  postId: route.params.postId,
  title: '脑袋为什么尖尖的',
  text: '那我问你',
  authorId: 1,
  authorAvatar: 'https://s2.loli.net/2024/12/20/3hNwzHfSvQJTbFM.jpg',
  comment: [
    {
      commentId: 1,
      text: '嘻嘻',
      children:[
        {
          commentId: 2,
          text: '哈哈',
        }
      ]
    },{
      commentId: 3,
      text:'hh'
    }
  ]
})
onMounted(() => {
  //请求帖子详细信息

})
</script>

<template>
  <el-container>
    <el-main>
      <div class="post-card">
        <el-scrollbar>
          <div class="post-inner">
            <el-row class="post-area"></el-row>
            <el-row class="comment-area">
              <el-collapse class="comment-collapse">
                <el-collapse-item>
                  <div class="comment-f">
                    <el-collapse class="comment-children">
                      <el-collapse-item></el-collapse-item>
                    </el-collapse>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </el-row>
          </div>
        </el-scrollbar>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>

</style>