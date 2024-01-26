<script setup>
import {ref} from "vue";
import api from "../../libs/api/index.js";
import Post from "../../components/post/Post.vue";
import globel from "../../libs/globel/index.js";
import {MdPreview} from "md-editor-v3";


const messages = ref([])
// messageType = 0: 系统， = 1： BBS
const moreText = ref('加载更多')
const listMoreMessages = () => {
  api.get('/api/v1/system-message/list?page=1&pageSize=20').then(res => {
    messages.value.push(...res.list);
    if(res.list.length === 0){
      moreText.value = '没有更多了'
    }
  })
}
listMoreMessages();
const getTimeStr = (time) => {
  // 吧yyyy-MM-dd HH:mm:ss 格式的时间转换为 1分钟前，1小时前，1天前，1月前，1年前
  const now = new Date().getTime();
  const diff = now - new Date(time).getTime();
  if (diff < 60 * 1000){
    return '刚刚'
  } else if (diff < 60 * 60 * 1000){
    return Math.floor(diff / (60 * 1000)) + '分钟前'
  } else if (diff < 24 * 60 * 60 * 1000){
    return Math.floor(diff / (60 * 60 * 1000)) + '小时前'
  } else if (diff < 30 * 24 * 60 * 60 * 1000){
    return Math.floor(diff / (24 * 60 * 60 * 1000)) + '天前'
  } else if (diff < 12 * 30 * 24 * 60 * 60 * 1000){
    return Math.floor(diff / (30 * 24 * 60 * 60 * 1000)) + '月前'
  } else {
    return Math.floor(diff / (12 * 30 * 24 * 60 * 60 * 1000)) + '年前'
  }
}

const scrollRef = ref()
const loading = ref(false)
const isEnd = ref(false)
let page = 1;
let size = 20;
const onScroll = (e) => {
  const lastScroll = scrollRef.value.wrapRef.scrollHeight - scrollRef.value.$el.clientHeight
  if(loading.value){
    return;
  }
  if (e.scrollTop === lastScroll && !isEnd.value){
    if(loading.value){
      return;
    }
    loading.value = true
    console.log('加载之后的消息')
    page ++;
    api.get('/api/v1/system-message/list?page=' + page + '&pageSize=' + size).then(res => {
      messages.value.push(...res.list);
      scrollRef.value.update();
      if(res.list.length < size){
        isEnd.value = true
      }
    }).finally(() => {
      loading.value = false;
    })
  }
}
const selectedMessage = ref(undefined)

const getHtml = (msg) => {

  // 在公式前后加上换行符
  let reg = /\$\$([^\$]+)\$\$/g;
  let result = msg.match(reg);
  if (result){
    result.forEach(item => {
      msg = msg.replace(item, `\n${item}\n`)
    })
  }
  return msg;
}
const thisPost = ref(undefined)
const clickMessage = (msg) => {
  selectedMessage.value = msg;
  api.get('/api/v1/system-message/read/' + selectedMessage.value.id).then(res1 => {
    if (!selectedMessage.value.read){
      selectedMessage.value.read = true;
      if (globel.noReadMessageCount > 0){
        globel.noReadMessageCount --;
      }
    }
  })
  if (selectedMessage.value.messageType === 1){
    thisPost.value = undefined;
    api.get('/api/v1/post/' + selectedMessage.value.businessId).then(res => {
      if (!res.commentsList){
        res.commentsList = []
      }
      res.imagesArray = JSON.parse(res.images)
      thisPost.value = res;
    })
  }
}
</script>

<template>
  <div class="notice">
    <el-container style="height: 100%; padding: 0">
      <el-aside width="300px" class="aside">
        <div class="message-list" style="height: calc(100% - 32px)">
          <div class="main-title">消息列表</div>
          <div class="messages" style="height: calc(100% - 22px)">
            <el-scrollbar style="height: 100%" ref="scrollRef" @scroll="onScroll">
              <div v-for="message in messages" :key="message.id" @click="clickMessage(message)">
                <el-badge is-dot style="width: 100%" v-if="!message.read">
                  <div class="message">
                    <div class="icon">
                      <img src="../../assets/icons/notice.svg" v-if="message.messageType === 0" />
                      <img src="../../assets/icons/default-avatar.svg" v-if="message.messageType === 1 && !message.avatar" />
                      <img :src="'/api/v1/file/display/' + message.avatar" v-if="message.messageType === 1 && message.avatar" />
                    </div>
                    <div class="content">
                      <div class="title">
                        <div class="text" :class="{read: message.read}">{{message.messageTitle}}</div>
                        <div class="time">{{getTimeStr(message.createdTime)}}</div>
                      </div>
                      <div class="desc">{{message.messageText}}</div>
                    </div>
                  </div>
                </el-badge>
                <div class="message" v-else>
                  <div class="icon">
                    <img src="../../assets/icons/notice.svg" v-if="message.messageType === 0" />
                    <img src="../../assets/icons/default-avatar.svg" v-if="message.messageType === 1 && !message.avatar" />
                    <img :src="'/api/v1/file/display/' + message.avatar" v-if="message.messageType === 1 && message.avatar" />
                  </div>
                  <div class="content">
                    <div class="title">
                      <div class="text" :class="{read: message.read}">{{message.messageTitle}}</div>
                      <div class="time">{{getTimeStr(message.createdTime)}}</div>
                    </div>
                    <div class="desc">{{message.messageText}}</div>
                  </div>
                </div>
              </div>
              <div style="height: 30px; line-height: 30px; text-align: center; color: gray;font-size: 14px">{{moreText}}</div>
            </el-scrollbar>
          </div>
        </div>
      </el-aside>
      <el-main>
        <div class="empty" v-if="!selectedMessage">
          <el-empty description="点击左侧列表消息查看详情" />
        </div>
        <div v-else-if="selectedMessage.messageType === 0" class="sys-message">
          <!-- 系统消息 -->
          <div class="title">{{selectedMessage.messageTitle}}</div>
          <div class="time">{{selectedMessage.createdTime}}</div>
<!--          <div class="content" v-html="getHtml(selectedMessage.messageText)"></div>-->
          <md-preview :model-value="getHtml(selectedMessage.messageText)" :no-img-zoom-in="true"/>
        </div>
        <div v-else-if="selectedMessage.messageType === 1" class="post-message">
          <post :post="thisPost" v-if="thisPost" list-comment />
          <div v-loading="true" v-else style="height: 100px"></div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped lang="less">
.notice{
  height: 100%;
  .aside{
    background: rgba(249,249,251,0.5);
    border-left: 1px solid #ebeef5;
    .message-list{
      .main-title{
        font-size: 14px;
        font-weight: bold;
        color: #333;
        margin-bottom: 15px;
        text-align: center;
        margin-top: 10px;
      }
      .messages{
        border-top: 1px solid #ebeef5;
        /deep/.el-badge__content.is-fixed.is-dot{
          right: 15px;
          top: 15px;
        }
        .message{
          display: flex;
          align-items: center;
          cursor: pointer;
          padding: 8px 16px;
          &:hover{
            background: rgba(204,204,204,0.15);
          }
          .icon{
            width: 48px;
            height: 48px;
            margin-right: 10px;
            background: rgba(204,204,204,0.15);
            border-radius: 16px;
            overflow: hidden;
            img{
              width: 48px;
              height: 48px;
            }
          }
          .content{
            flex: 1;
            width: calc(100% - 58px);
            .title{
              display: flex;
              justify-content: space-between;
              margin-bottom: 5px;
              margin-top: 0;
              .text{
                font-size: 14px;
                font-weight: bold;
                color: #333;
                &.read{
                  color: #999;
                  font-weight: lighter;
                }
              }
              .time{
                font-size: 12px;
                font-weight: normal;
                color: #999;
              }
            }
            .desc{
              font-size: 12px;
              font-weight: normal;
              color: #999;
              margin-top: 5px;
              // 超出显示省略号
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }
        }
      }
    }
  }
  .sys-message{
    padding: 16px;
    .title{
      font-size: 16px;
      font-weight: bold;
      color: #333;
      margin-bottom: 10px;
    }
    .time{
      font-size: 12px;
      font-weight: normal;
      color: #999;
      margin-bottom: 10px;
    }
    .content{
      font-size: 14px;
      font-weight: normal;
      color: #333;
      line-height: 24px;
    }
  }
}
</style>