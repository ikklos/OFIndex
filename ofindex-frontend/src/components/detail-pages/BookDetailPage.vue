<script setup>

import {onMounted, reactive, ref} from 'vue'
import {useRoute} from 'vue-router'
import {Icon} from "view-ui-plus";
import {VideoPlay} from "@element-plus/icons-vue";
const route = useRoute();
const bookData = reactive({
  bookId: route.params.id,
  bookName: '可口可乐和百事可乐哪个更好喝',
  isbn: '114514',
  description: '冰镇可乐，喝的，更痛快，冰镇可乐，喝的，更痛快，冰镇可乐，喝的，更痛快，冰镇可乐，喝的，更痛快，冰镇可乐，喝的，更痛快，冰镇可乐，喝的，更痛快，冰镇可乐，喝的，更痛快，冰镇可乐，喝的，更痛快',
  coverUrl: 'https://s2.loli.net/2024/12/20/3hNwzHfSvQJTbFM.jpg',
  tags:'嘻嘻嘻,不嘻嘻,嘻嘻',
});
const packData = ref([]);
const flagLike = function(index){
  packData.value[index].liked = true;
}
onMounted(() => {
  for(let i = 0; i < 40; i++) {
    packData.value.push({
      packId: 111,
      name: '超炫资源包',
      authorId: 1,
      description:'哇，好炫',
      authorAvatar: 'https://s2.loli.net/2024/12/20/3hNwzHfSvQJTbFM.jpg',
      liked: false,
    });
  }
  if(bookData.bookId !== null){
    //发送详情请求，返回数据
  }
})
</script>

<template>
  <el-container class="book-detail-out">
    <el-main>
      <el-row >
        <el-col :span="8">
          <div class="book-left-area">
            <el-row class="book-title-area">
              <div class="book-title-text">
                {{bookData.bookName}}
              </div>
            </el-row>
            <el-row class="book-cover-area"><el-image :src="bookData.coverUrl" class="book-cover-img" fit="cover"></el-image></el-row>
            <el-row class="book-isbn-area">ISBN:{{bookData.isbn}}</el-row>
            <el-row class="book-tags-area">tags:{{bookData.tags}}</el-row>
          </div>
        </el-col>
        <el-col class="book-right-area" :span="16">
          <el-row class="description-row">
              <div class="description-content">
                <el-scrollbar style="width: 100%;height: 100%;">
                  <div class="text-area">{{bookData.description}}</div>
                </el-scrollbar>
              </div>
          </el-row>
          <el-row class="packs-area">
            <div class="packs-area-inner">
              <el-scrollbar>
                <div class="packs-list">
                  <div v-for="(item,index) in packData" :key="index" class="pack-item">
                    <el-row class="pack-item-inner">
                      <el-col :span="5" class="pack-avatar">
                        <el-avatar :src="item.authorAvatar" fit="cover" style="height: 60px; width: 60px; border: 1px solid #3c5cd7"></el-avatar>
                      </el-col>
                      <el-col :span="19">
                        <el-row>
                          <el-col :span="8" style="background: #fcfce5; border-radius: 5px; font-size:18px; line-height: 20px; padding:5px; box-sizing: border-box" >
                            {{item.name}}
                          </el-col>
                          <el-col :span="16">
                            <Icon type="ios-heart-outline" class="like-icon" @click="flagLike(index)" v-if="!item.liked"/>
                            <Icon type="ios-heart" class="like-icon" v-if="item.liked"/>
                            <el-button color="#4825f6" style="border-radius: 20px">添加此资源包</el-button>
                          </el-col>
                        </el-row>
                        <el-row class="pack-description-row">
                            {{item.description}}
                        </el-row>
                      </el-col>
                    </el-row>
                  </div>
                </div>
              </el-scrollbar>
            </div>
          </el-row>
          <el-row class="buttons-row">
            <el-col :span="8">
              <el-button color="#4825f6" class="bottom-button">开始阅读</el-button>
            </el-col>
            <el-col :span="8" >
              <el-button color="#4825f6" class="bottom-button">选择资源包</el-button>
            </el-col>
            <el-col :span="8" class="bottom-button">
              <el-button color="#4825f6" class="bottom-button">添加到书架</el-button>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<style scoped>
.book-detail-out {
  min-width: 720px;
  min-height: 540px;
}
.book-title-area {
  box-sizing: border-box;
  padding: 0 20px 0 20px;
  justify-content: center;
  align-items: center;
}
.book-title-text {
  box-sizing: border-box;
  padding: 5px;
  color: #1a1a1a;
  height: 100%;
  width: 100%;
  line-height: 30px;
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  justify-content: center;
  align-items: center;
  border-radius: 5px;
}
.book-cover-area{
  padding: 10px;
}
.book-cover-img{
  max-width: 28vw;
  max-height: 68vh;
  min-width: 180px;
  min-height: 210px;
  border: 1px solid #e9e7ef;
  border-radius: 10px;
  box-shadow: 1px 1px 2px rgba(0,0,0,0.2);
}
.book-isbn-area{
  line-height: 16px;
  font-size: 16px;
  padding: 0 20px 0 20px;
}
.book-tags-area{
  line-height: 16px;
  font-size: 16px;
  padding: 10px 20px 0 20px;
}
.description-row{
  box-sizing: border-box;
  height: 30vh;
  width: 100%;
  padding: 10px 20px 10px 20px;
}
.description-content{
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  border-radius: 20px;
  box-shadow: 1px 1px 2px rgba(0,0,0,0.3);
  padding: 10px;
  background: #f5f5f5;
}
.text-area{
  width: 100%;
  display: flex;
  padding:10px;
  overflow-x: hidden;
  align-items: center;
  justify-content: flex-start;
  text-align: left;
  font-size: 20px;
  line-height: 23px;
}
.packs-area{
  width: 100%;
  box-sizing: border-box;
  padding: 10px 20px 10px 20px;
  height: 40vh;
  min-height: 300px;
}
.packs-area-inner{
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  box-shadow: 1px 1px 2px rgba(0,0,0,0.3);
  border-radius: 20px;
  padding: 10px 5px 10px 5px;
  background: #f5f5f5;
}
.packs-list{
  width: 100%;
  height: 100%;
}
.pack-item{
  padding: 5px 10px 5px 10px;
}
.pack-item-inner{
  color: #000000;
  height: 10vh;
  min-height: 80px;
  width: 100%;
  background: #f9fce8;
  border-radius: 10px;
  padding: 5px;
}
.pack-avatar{
  display: grid;
  place-items: flex-start;
  height: 100%;
}
.pack-description-row{
  padding: 5px;
  font-size: 16px;
  line-height: 20px;
  color: #292929;
}
.like-icon{
  font-size:20px;
}
.like-icon:hover{
  cursor: pointer;
}
.buttons-row{
  box-sizing: border-box;
  padding: 10px 20px 10px 20px;
}

.bottom-button{
  width: 10vw;
  height: 6vh;
  min-width: 120px;
  text-align: center;
  line-height: 100%;
  font-size: 18px;
  border-radius: 30px;
}
</style>