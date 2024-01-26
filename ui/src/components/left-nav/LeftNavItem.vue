<script setup>
import {onMounted, ref, watch} from "vue";
import router from "../../router/index.js";

const inMouse = ref(false)
const getImg = (img) => {
    return new URL(`./icons/${img}`, import.meta.url);
}
const props = defineProps({
    conf: {
        type: Object,
        default: {
            title: '开放世界',
            icon: 'common'
        }
    }
})
const imgUrl = ref(getImg(props.conf.icon + '.svg'))
const active = ref(false)
onMounted(() => {
    active.value = router.currentRoute.value.fullPath.startsWith('/' + props.conf.icon)
    imgUrl.value = getImg(props.conf.icon + (inMouse.value || active.value ? '-active.svg' : '.svg'))
})
watch(
    () => router.currentRoute.value,
    (newValue) => {
        active.value = router.currentRoute.value.fullPath.startsWith('/' + props.conf.icon)
        imgUrl.value = getImg(props.conf.icon + (inMouse.value || active.value ? '-active.svg' : '.svg'))
    },
    {immediate: true}
)
watch(() => inMouse.value, (newValue) => {
    active.value = router.currentRoute.value.fullPath.startsWith('/' + props.conf.icon)
    imgUrl.value = getImg(props.conf.icon + (inMouse.value || active.value ? '-active.svg' : '.svg'))
}, {immediate: true})
</script>

<template>
<div class="item" :class="{active: active}" @click="router.replace({path: '/' + props.conf.icon})">
    <div class="left-border"></div>
    <div class="content" @mousemove="inMouse = true" @mouseout="inMouse = false">
      <img :src="imgUrl">
    </div>
</div>
</template>

<style scoped lang="less">
.item{
  width: 100%;
  height: 50px;
  display: inline-block;
  text-align: center;
  cursor: pointer;
  margin-top: 10px;

  &.active{
    .left-border{
      background-color: #000235;
    }
  }

  .left-border{
    height: 30px;
    width: 4px;
    background-color: transparent;
    border-radius: 0 8px 8px 0;
    margin-top: 10px;
    float: left;
    transition: background-color .3s;
  }
  .content{
    width: calc(100% - 4px);
    height: 50px;

    img{
      width: 32px;
      height: 32px;
      vertical-align: top;
      margin-top: 8px;
    }
  }
}
</style>