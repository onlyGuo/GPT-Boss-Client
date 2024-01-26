<script setup>

import UserInfo from "../../components/left-nav/UserInfo.vue";
import {nextTick, ref, watch} from "vue";
import api from "../../libs/api/index.js";
import {Plus} from "@element-plus/icons-vue";
import ChatListItem from "../../components/chat/ChatListItem.vue";
import DefaultPage from "./DefaultPage.vue";
import router from "../../router/index.js";
import {ElMessage} from "element-plus";
import globel from "../../libs/globel/index.js";
import ChatView from "./ChatView.vue";
const items = ref([
  {
    id: '1',
    name: "未命名会话",
    avatar: "",
    autoSong: false,
    contentLength: 5,
    dscp: '',
    ifPublic: '',
    lastMessage: '',
    lastMessageTimeStr: '',
    maxToken: 4096,
    temperature: 0.7,
    presence_penalty: 1,
    frequency_penalty: 0.5,
  }
])
const selectChat = ref(undefined)
const selectChatSnapshot = ref(undefined);

const listChats = () => {
  nextTick(() => {
    if (globel.isLogin){
      api.get('/api/v1/chat').then(res => {
        items.value = res;
        if (router.currentRoute.value.params.id){
          selectChat.value = items.value.find(item => item.id == router.currentRoute.value.params.id)
          if (selectChat.value){
            selectChatSnapshot.value = JSON.stringify(selectChat.value)
          }
        }
      })
    }else{
      let listStr = localStorage.getItem('chatList')
      if(!listStr){
        listStr = JSON.stringify(items.value)
        localStorage.setItem('chatList', listStr)
      }
      items.value = JSON.parse(listStr)
      if (router.currentRoute.value.params.id){
        selectChat.value = items.value.find(item => item.id == router.currentRoute.value.params.id)
        if (selectChat.value){
          selectChatSnapshot.value = JSON.stringify(selectChat.value)
        }
      }
    }
    watch(() => selectChat.value, (val) => {
      if (val) {
        // 本地缓冲，1秒内有连续改动时，不保存，当改动超过1秒且没有新的变化时，保存(调用saveSelectChat)
        const now = new Date().getTime()
        if (now - lastSelectChatUpdateTime < 1000){
          clearTimeout(updateTimer)
        }
        updateTimer = setTimeout(() => {
          saveSelectChat()
        }, 1000)
        lastSelectChatUpdateTime = now
      }
    }, {deep: true})
  })
}
watch(() => globel.isLogin, (val) => {
  if (val){
    listChats()
  }
})
listChats()

watch(() => selectChat.value, (val) => {
  if (val){
    router.push({
      path: '/chat/' + val.id
    })
  }
})
// 深度监听selectChat 中的属性变动
let lastSelectChatUpdateTime = 0
let updateTimer;



const saveSelectChat = () => {
  if (JSON.stringify(selectChat.value) === selectChatSnapshot.value){
    return;
  }
  if (globel.isLogin){
    api.put('/api/v1/chat', selectChat.value)
  }else{
    localStorage.setItem('chatList', JSON.stringify(items.value))
  }
  selectChatSnapshot.value = JSON.stringify(selectChat.value)
}

const deleteChat = (id) => {
  if (globel.isLogin){
    api.delete('/api/v1/chat/' + id).then(res => {
      ElMessage.success('删除成功')
      items.value = items.value.filter(item => item.id !== id);
    })
  }else{
    items.value = items.value.filter(item => item.id !== id);
    localStorage.setItem('chatList', JSON.stringify(items.value))
  }
}

const select = (item) => {
  selectChat.value = item;
  selectChatSnapshot.value = JSON.stringify(selectChat.value)
}

const newChat = () => {
  if (globel.isLogin){
    api.post('/api/v1/chat/new').then(res => {
      items.value.push(res)
      selectChat.value = res
      if (selectChat.value){
        selectChatSnapshot.value = JSON.stringify(selectChat.value)
      }
    })
  }else{
    let title = '未命名会话'
    const defaultList = items.value.filter(item => item.name.startsWith(title))
    if (defaultList.length > 0){
      let max = 0
      defaultList.forEach(item => {
        const num = parseInt(item.name.replace(title, '').replace('(', '').replace(')', '').trim())
        if (num > max){
          max = num
        }
      })
      title = title + '(' + (max + 1) + ')'
    }
    selectChat.value = {
      id: title,
      name: title,
      avatar: "",
      autoSong: false,
      contentLength: 5,
      dscp: '',
      ifPublic: '',
      lastMessage: '',
      lastMessageTimeStr: '',
      maxToken: 4096,
      temperature: 0.7,
      presence_penalty: 1,
      frequency_penalty: 0.5,
    }
    items.value.push(selectChat.value)
    localStorage.setItem('chatList', JSON.stringify(items.value))
    if (selectChat.value){
      selectChatSnapshot.value = JSON.stringify(selectChat.value)
    }
    return;
  }
}
</script>

<template>
  <div class="common-layout">
    <el-container style="height: 100%">
      <el-aside width="256px" class="aside">
        <el-button style="width: 100%; height: 40px" @click="newChat"><el-icon><plus></plus></el-icon>新建会话</el-button>
        <el-scrollbar style="height: calc(100% - 100px)">
          <chat-list-item :conf="item" v-for="item in items" :key="item.id" @click.stop="select(item)"
                          :active="item === selectChat" no-edit :on-delete="deleteChat"></chat-list-item>
        </el-scrollbar>
        <user-info></user-info>
      </el-aside>
      <el-main style="padding: 0">
        <div v-if="!selectChat">
          <default-page style="margin-top: 20px"></default-page>
        </div>
        <ChatView v-else :chat="selectChat" :key="selectChat.id"></ChatView>
<!--        <router-view v-else :key="selectChat.id"></router-view>-->
      </el-main>
    </el-container>
  </div>
</template>

<style scoped lang="less">
.common-layout{
  height: 100%;
  .aside{
    background: rgba(249,249,251,0.5);
  }
}
</style>