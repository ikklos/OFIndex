<script setup>
import {ref,defineProps} from 'vue'
const fold = ref(true);//默认折叠
const hoverTimeout = ref(null); // 用于存储定时器
const props = defineProps({
  menuItems: {
    type: Array,
    required: true,
  },
  disperse:{
    type: Number,
    default: 0.6, //分散程度，根与目录项的圆心距离为(disperse+1)*直径
  },
  direction:{
    type: String,
    default: 'left',
    validator(value) {
      // 只允许 'left' 或 'right'，否则返回 false
      return value === 'left' || value === 'right';
    },
  }
});
const calPosition = function(index){
  if(document.getElementById("balls-root") == null){
    return {};
  }
  let RootPosition = document.getElementById("balls-root").getBoundingClientRect();
  let rotation = 0;
  let rotation_offset = 180.0/(props.menuItems.length+1) * (1+index);
  let dir = (()=>{
    if(props.direction === 'left'){
      return 1;
    }else {
      return -1;
    }
  })();
  let d = RootPosition.right-RootPosition.left //目录项直径，为3vw
  let axisX = (RootPosition.left + RootPosition.right)/2 -
      dir*Math.sin(rotation_offset * (Math.PI / 180))*d*(props.disperse+1); //Math.sin传入弧度值，同时计算圆心距离
  let axisY = (RootPosition.top + RootPosition.bottom)/2 -
      dir*Math.cos(rotation_offset * (Math.PI / 180))*d*(props.disperse+1);
  axisY -= d/2
  axisX -= d/2
  return {
    top: `${axisY}px`,
    left: `${axisX}px`,
    transition: `${(index+1)*0.2}s`
  }
}
const showItems = function(){
  if (hoverTimeout.value) clearTimeout(hoverTimeout.value);
  fold.value = false;
}
const foldItems = function(){
  hoverTimeout.value = setTimeout(() => {
    fold.value = true;
  }, 200);
}
</script>

<template>
  <div class="circle" id="balls-root" @mouseover="showItems" @mouseleave="foldItems">
  </div>
  <transition-group name="el-fade-in-linear">
    <div v-for="(item,index) in menuItems" :key="index" :style="calPosition(index)" class="items" v-if ="!fold" @mouseover="showItems" @mouseleave="foldItems"></div>
  </transition-group>

</template>

<style scoped>
.circle{
  width: 3vw;
  height: 3vw;
  border-radius: 50%;
  border: 1px solid darkgray;
  transition:all 0.3s ease;
  min-width: 40px;
  min-height: 40px;
}
.circle:hover{
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  transform: scale(1.2,1.2);
  cursor: pointer;
  transition: all 0.3s ease;
}
.items{
  position: absolute;
  height: 3vw;
  width: 3vw;
  border-radius: 50%;
  border: 1px solid darkgray;
  min-width: 40px;
  min-height: 40px;
}
.items:hover{
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  transform: scale(1.1,1.1);
  cursor: pointer;
  transition: all 0.3s ease;
}
</style>