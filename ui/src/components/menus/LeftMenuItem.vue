<script setup>
import {ref, watch} from "vue";
import router from "../../router/index.js";

const props = defineProps({
  conf: {
    type: Object,
    required: true,
    default: {
      id: 1,
      name: "公共聊天室",
      icon: "chat-room.svg",
    }
  },
  active: {
    type: Boolean,
    default: false
  }
})

</script>

<template>
  <div class="item" :class="{active: props.active}">
    <div class="image">
      <img :src="'/api/v1/file/display/' + props.conf.icon">
    </div>
    <div class="title">
      {{props.conf.name}}
    </div>
  </div>
</template>

<style scoped lang="less">
.item {
  height: 72px;
  border-radius: 8px 8px 8px 8px;
  margin: 12px;
  display: flex;
  align-items: center;
  padding: 0 12px;
  cursor: pointer;

  .image {
    display: inline-block;
    vertical-align: middle;
    height: 48px;
    width: 48px;
    overflow: hidden;
    img{
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .title {
    display: inline-block;
    vertical-align: middle;
    margin-left: 12px;
    font-size: 16px;
    color: #58597A;
  }

  &:hover {
    background: rgba(91, 82, 217, 0.08);
  }

  &.active {
    font-weight: bold;
    color: #000235;
    background: rgba(91, 82, 217, 0.08);
  }

}
</style>