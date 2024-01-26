<script setup>
import {ref, watch} from "vue";
import router from "../../router/index.js";
import {Delete, Edit} from "@element-plus/icons-vue";

const props = defineProps({
  conf: {
    type: Object,
    required: true,
    default: {
      id: 1,
      name: "公共聊天室",
      avatar: "",
      lastMessage: ''
    }
  },
  active: {
    type: Boolean,
    default: false
  },
  onEdit: {
    type: Function,
    default: () => {}
  },
  onDelete: {
    type: Function,
    default: () => {}
  },
  noEdit: {
    type: Boolean,
    default: false
  },
  noDelete: {
    type: Boolean,
    default: false
  }
})

</script>

<template>
  <div class="item" :class="{active: props.active}">
    <div class="image">
      <img :src="'/api/v1/file/display/' + props.conf.avatar" v-if="props.conf.avatar">
      <img src="../../assets/logo.svg" v-else />
    </div>
    <div class="right">
      <div class="title">
        {{props.conf.name}}
      </div>
      <div class="tool-box">
        <div class="tool-item" v-if="!props.noEdit" @click.stop="props.onEdit(props.conf)"><el-icon><edit></edit></el-icon></div>
        <div class="tool-item" v-if="!props.noDelete" @click.stop="props.onDelete(props.conf.id)"><el-icon><delete></delete></el-icon></div>
      </div>
      <div class="last-message">{{props.conf.lastMessage ? props.conf.lastMessage : '暂无消息'}}</div>
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
  cursor: default;

  .image {
    display: inline-block;
    vertical-align: middle;
    height: 48px;
    width: 48px;
    background: rgba(204, 204, 204, 0.15);
    border-radius: 16px;
    overflow: hidden;
    img{
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  .right{
    display: inline-block;
    vertical-align: middle;
    margin-left: 12px;
    width: calc(100% - 60px);
    .title {
      //display: inline-block;
      vertical-align: middle;
      font-size: 16px;
      color: #58597A;
      // 省略号
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .tool-box{
      float: right;
      margin-top: -10px;
      display: none;
      .tool-item{
        margin-left: 8px;
        font-size: 12px;
        color: #58597A;
        cursor: pointer;
        display: inline-block;
      }
    }
    .last-message{
      margin-left: auto;
      font-size: 12px;
      color: #58597A;
      // 省略号
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  &:hover {
    background: rgba(91, 82, 217, 0.08);
    .tool-box{
      display: inline-block;
    }
  }

  &.active {
    font-weight: bold;
    color: #000235;
    background: rgba(91, 82, 217, 0.08);
  }

}
</style>