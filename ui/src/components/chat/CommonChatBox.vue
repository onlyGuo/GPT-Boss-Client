<script setup>

import Message from "./Message.vue";
import globel from "../../libs/globel/index.js";
import ImgUpload from "../ImgUpload.vue";
import {nextTick, onMounted, ref} from "vue";
import Globel from "../../libs/globel/index.js";

const props = defineProps({
  send: {
    type: Function,
    default: undefined
  },
  loadMessage: {
    type: Function,
    default: undefined
  },
  noModel: {
    type: Boolean,
    default: false
  }
})


const messages = ref([])
const messageListRef = ref(null)

const selectModel = ref('')

onMounted(() => {
  nextTick().then(() => {
    messageListRef.value.setScrollTop(messageListRef.value.wrapRef.scrollHeight - messageListRef.value.$el.clientHeight);
    if(!selectModel.value && globel.chatModules.length > 0){
      selectModel.value = globel.chatModules[0].name
    }
    if (!selectModel.value){
      setTimeout(() => {
        if(!selectModel.value && globel.chatModules.length > 0){
          selectModel.value = globel.chatModules[0].name
        }
      }, 1000)
    }
  })
})



const isEnd = ref(true);
const onScroll = (e) => {
  const lastScroll = messageListRef.value.wrapRef.scrollHeight - messageListRef.value.$el.clientHeight
  if(loading.value){
    return;
  }
  if (e.scrollTop === lastScroll && !isEnd.value){
    if(loading.value){
      return;
    }
    console.log('加载之后的消息')
    if(props.loadMessage){
      props.loadMessage({
        type: 'after',
        indexId:  messages.value[messages.value.length - 1].id
      }).then(res => {
        if(res.length < 10){
          isEnd.value = true;
        }
        messages.value.push(...res)
        messageListRef.value.update();
      }).finally(() => {
        loading.value = false;
      })
    }
  }else if (e.scrollTop === 0){
    console.log('加载之前的消息')
    if(props.loadMessage){
      props.loadMessage({
        type: 'before',
        indexId:  messages.value[0].id
      }).then(res => {
        messages.value = res.concat(messages.value)
        messageListRef.value.update();
        nextTick().then(() => {
          if(messages.value.length > 50){
            messages.value = messages.value.slice(0, 50)
            isEnd.value = false;
          }
          messageListRef.value.setScrollTop(messageListRef.value.wrapRef.scrollHeight - lastScroll - messageListRef.value.$el.clientHeight);
        })
      }).finally(() => {
        loading.value = false;
      })
    }
  }
}

const inputText = ref('')
const image = ref('')
const sendMessage = () => {
  if(inputText.value.trim() === ''){
    return;
  }
  if(selectModel.value !== 'gpt-4-vision'){
    image.value = '';
  }
  const currentMessage = {
    content: image.value ? inputText.value + "\n\n![img](/api/v1/file/display/" + image.value + ")\n" : inputText.value,
    role: 'user'
  }
  const history = messages.value.slice(messages.value.length - 50).map(item => {
        return {
          content: item.content,
          role: item.role ? item.role : (item.userId === Globel.currentUser?.id ? 'user' : 'assistant')
        }
      });
  history.push(currentMessage)
  // 获得最近6条消息
  const message = {
    type: 'message',
    message: history,
    module: selectModel.value
  }
  props.send(message);
  image.value = '';
  inputText.value = '';
  nextTick().then(() => {
    messageListRef.value.setScrollTop(messageListRef.value.wrapRef.scrollHeight - messageListRef.value.$el.clientHeight);
  })
}

const onKeyDown = (e) => {
  if(e.key === 'Enter'){
    if (e.shiftKey){
      return;
    }
    sendMessage()
  }
}

const onMessage = (message) => {
  if(message.userId && message.userId === Globel.currentUser?.id){
    message.nicker = Globel.currentUser ? Globel.currentUser.nickname : 'Guest';
  }
  if(!message.index){
    message.index = new Date().getTime();
  }
  if(message.isPlus){
    message.plus = true;
  }
  const curMessage = messages.value.find(item => item.index === message.index);
  if(curMessage){
    curMessage.content += message.content
  }else{
    messages.value.push(message)
  }
  if(messages.value.length > 50){
    messages.value = messages.value.slice(messages.value.length - 50)
  }
  nextTick().then(() => {
    if(messageListRef.value){
      messageListRef.value.setScrollTop(messageListRef.value.wrapRef.scrollHeight - messageListRef.value.$el.clientHeight);
    }
  })
}

const getHistoryMessage = (message) => {
  return messages.value;
}

defineExpose({
  onMessage, getHistoryMessage
})
const toBottom = () => {
  nextTick().then(() => {
    if(messageListRef.value){
      messageListRef.value.setScrollTop(messageListRef.value.wrapRef.scrollHeight - messageListRef.value.$el.clientHeight);
    }
  })
}

const loading = ref(false);
</script>

<template>
<div class="common-chat-box">
    <div class="messages">
      <el-scrollbar style="height: 100%;" ref="messageListRef" @scroll="onScroll">
        <message :message="item" v-for="item in messages" :key="item.index" :on-html-changed="toBottom"></message>
        <div style="height: 70px"></div>
      </el-scrollbar>
    </div>
  <div class="input-box">
    <el-input
        v-model="inputText"
        :placeholder="'请输入消息内容'"
        class="input-with-select"
        @keydown="onKeyDown"
    >
      <template #prepend v-if="!noModel">
        <el-select v-model="selectModel" placeholder="Select" style="width: 115px;border-radius: 28px;">
          <el-option :label="module.name" :value="module.name" v-for="module in globel.chatModules" class="option">
            <div class="option-info">
              <div class="title" v-text="module.name"></div>
              <div class="desc" v-text="module.description"></div>
            </div>
          </el-option>
        </el-select>
        <div class="selectImg" v-if="selectModel === 'gpt-4-vision'">
          <img-upload :size="40" v-model:src="image"></img-upload>
        </div>
      </template>
    </el-input>
    <button @click="sendMessage">
      <span>发送</span>
    </button>
  </div>
</div>
</template>

<style scoped lang="less">
/deep/.el-input-group--prepend .el-input-group__prepend .el-select .el-select__wrapper{
  box-shadow: none;
}
.option{
  height: 50px;
  .option-info{
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 50px;

    .title{
      font-size: 14px;
      font-family: Source Han Sans CN-Heavy, Source Han Sans CN;
      font-weight: 800;
      line-height: 20px;
    }
    .desc{
      font-size: 12px;
      font-family: Source Han Sans CN-Regular, Source Han Sans CN;
      font-weight: lighter;
      color: gray;
      line-height: 17px;
    }
  }
}
.common-chat-box{
  height: 100%;
  width: 100%;
  .messages{
    height: 100%;
    /deep/.el-table-v2__header-wrapper{
      display: none;
    }
  }
  .input-box{
    display: flex;
    align-items: center;
    justify-content: space-between;
    z-index: 100;
    height: 56px;
    background: #F3F4FA;
    border-radius: 132px 132px 132px 132px;
    //overflow: hidden;
    margin-top: -70px;
    // 最顶层
    position: relative;
    margin-left: 60px;
    margin-right: 60px;
    transition: all 0.3s ease-in-out;
    &:hover{
      margin-top: -75px;
      box-shadow: 0 3px 6px 1px rgba(0,0,0,0.16);
    }

    .selectImg{
      margin-left: 30px;
    }

    /deep/.el-input{
      height: 56px;
      transition: width 0.3s ease-in-out;
      &.inqueue{
        width: 0;
      }
    }
    /deep/.el-input__wrapper{
      //box-shadow: red 0 0 0 1px;
      box-shadow: none!important;
      background-color: transparent;
      border-radius: 28px 0 0 28px;
      transition: all .3s ease-in-out;
      &.is-focus{
        border-radius: 28px 0 0 28px;
      }
    }
    .inqueue{
      /deep/.el-input__wrapper{
        display: none;
      }
    }
    /deep/.el-input-group__prepend{
      box-shadow: none;
      background-color: transparent;
      border-right: 1px solid #eeeeee;
    }
    /deep/.el-select:hover{
      .el-input__wrapper{
        border-radius: 28px 0 0 28px;
      }
    }
    input{
      width: calc(100% - 110px);
      height: 30px;
      border: none;
      padding: 0 10px;
      background-color: transparent;
      outline: none;
      margin-left: 10px;
    }
    button{
      width: 80px;
      height: 48px;
      background: #596FFF;
      box-shadow: inset -2px -3px 6px 1px rgba(0,0,0,0.16);
      border-radius: 132px 132px 132px 132px;
      color: white;
      border: none;
      margin: 4px;
      cursor: pointer;
      transition: all 0.6s ease-in-out;
      overflow: hidden;

      &:hover{
        background: #4A5DEB;
      }
    }
  }
}
</style>