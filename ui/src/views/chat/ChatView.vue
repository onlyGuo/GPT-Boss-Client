<script setup>

import CommonChatBox from "../../components/chat/CommonChatBox.vue";
import {nextTick, ref, watch} from "vue";
import router from "../../router/index.js";
import ImgUpload from "../../components/ImgUpload.vue";
import api from "../../libs/api/index.js";
import globel from "../../libs/globel/index.js";
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

const moveBar = ref()
const rightAttrWidth = ref(300)
const minWidth = 200
const maxWidth = 500
const moveBarMouseDown = (e) => {
  const startX = e.clientX
  const startWidth = rightAttrWidth.value
  const move = (e) => {
    const moveX = e.clientX
    const moveWidth = startWidth - moveX + startX
    if (moveWidth < minWidth){
      rightAttrWidth.value = minWidth
    }else if (moveWidth > maxWidth){
      rightAttrWidth.value = maxWidth
    }else{
      rightAttrWidth.value = moveWidth
    }
  }
  const up = () => {
    document.removeEventListener('mousemove', move)
    document.removeEventListener('mouseup', up)
  }
  document.addEventListener('mousemove', move)
  document.addEventListener('mouseup', up)
}

const open = ref(false)
const rightAttrArea = ref();
const baseSettingArea = ref();
const sysMsgArea = ref();
const contentLengthArea = ref();
const maxTokenArea = ref();
const temperatureArea = ref();
const frequency_penaltyArea = ref();

const props = defineProps({
  chat: {
    type: Object,
    default: () => {
      return {
        id: null,
        name: '',
        avatar: '',
        contentLength: 5,
        createTime: '',
        dscp: '',
        lastMessage: '',
        lastMessageTimeStr: '',
        sysMsg: '',
        maxToken: 4096,
        temperature: 0.7,
        presence_penalty: 1,
        frequency_penalty: 0.5,
      }
    }
  }
})
nextTick(() => {
  const tour = localStorage.getItem("chat-tour");
  if(!tour){
    open.value = true
    localStorage.setItem("chat-tour", "1")
  }
})
const chatBox = ref()
const loadMessage = () => {
  if(globel.isLogin){
    api.get('/api/v1/chat/message?chatId=' + props.chat.id).then(res => {
      res.forEach(message => {
        chatBox.value.onMessage(message)
      })
    })
  }else{
    let messageStr = localStorage.getItem("chat-message-" + props.chat.id);
    if(!messageStr){
      messageStr = '[]'
    }
    nextTick(() => {
      JSON.parse(messageStr).forEach(message => {
        chatBox.value.onMessage(message)
      })
    })
  }
}
watch(() => globel.isLogin, (val) => {
  if (val){
    loadMessage()
  }
})
loadMessage()

const sendMessage = (a) => {
  // 我的消息
  const message = {
    content: a.message[a.message.length - 1].content,
    role: 'user',
    userId: globel.currentUser ? globel.currentUser.id : undefined
  }
  chatBox.value.onMessage(message)
  localStorage.setItem("chat-message-" + props.chat.id, JSON.stringify(chatBox.value.getHistoryMessage()));

  // AI 回复
  let index;
  let time;
  api.fetch('/api/v1/chat/message', 'POST', {
    chatId: props.chat.id,
    // 根据contentLength获取最近几条消息
    message: a.message.splice(a.message.length - props.chat.contentLength, props.chat.contentLength),
    model: a.module,
    temperature: props.chat.temperature,
    presencePenalty: props.chat.presence_penalty,
    frequencyPenalty: props.chat.frequency_penalty,
    sysMsg: props.chat.sysMsg,
  }, (done, text, headers) => {
    if (headers){
      index = headers.get('message.index')
      time = headers.get('message.time')
    }
    if(text){
      chatBox.value.onMessage({
        content: text,
        role: 'assistant',
        userId: undefined,
        index: index,
        createTime: time,
        nicker: props.chat.name,
        avatar: props.chat.avatar
      })
    }

    if (done){
      localStorage.setItem("chat-message-" + props.chat.id, JSON.stringify(chatBox.value.getHistoryMessage()));
    }
  })
}

</script>

<template>
<div class="room">
  <div class="left" :style="'width: calc(100% - ' + rightAttrWidth + 'px)'">
    <common-chat-box :send="sendMessage" ref="chatBox"></common-chat-box>
  </div>
  <div class="right-attrs" :style="'width: ' + rightAttrWidth + 'px'" ref="rightAttrArea">
    <div class="width-move-bar" ref="moveBar" @mousedown="moveBarMouseDown"></div>
    <div class="content">
      <div class="title">属性编辑区</div>
      <div class="body">
        <el-scrollbar style="height: calc(100vh - 110px)">
          <el-form label-position="top" style="overflow-x: hidden">
            <div ref="baseSettingArea">
              <el-form-item label="会话名称" required>
                <el-input v-model="props.chat.name" placeholder="请输入会话名称"></el-input>
              </el-form-item>
              <el-form-item label="会话头像">
                <img-upload v-model:src="props.chat.avatar"></img-upload>
              </el-form-item>
            </div>
            <div ref="sysMsgArea">
              <el-form-item label="系统提示">
                <el-input v-model="props.chat.sysMsg" type="textarea" :rows="4" placeholder="在这里描述该GPT能做什么？它怎么做？以及应该避免哪些问题？"></el-input>
              </el-form-item>
            </div>
            <div style="width: calc(100% - 10px)">
              <div ref="contentLengthArea">
                <el-form-item label="上下文数">
                  <el-slider v-model="props.chat.contentLength" :min="1" :max="21"></el-slider>
                </el-form-item>
              </div>
<!--              <div ref="maxTokenArea">-->
<!--                <el-form-item label="最大Token回复长度">-->
<!--                  <el-slider v-model="props.chat.maxToken" :min="1" :max="4096"></el-slider>-->
<!--                </el-form-item>-->
<!--              </div>-->
              <div ref="temperatureArea">
                <el-form-item label="话题随机性">
                  <el-slider v-model="props.chat.temperature" :min="-2" :max="2" :step="0.01"></el-slider>
                </el-form-item>
                <el-form-item label="话题新鲜度">
                  <el-slider v-model="props.chat.presence_penalty" :min="-2" :max="2" :step="0.01" style="width: calc(100% - 10)"></el-slider>
                </el-form-item>
              </div>
              <div ref="frequency_penaltyArea">
                <el-form-item label="重复率惩罚力度">
                  <el-slider v-model="props.chat.frequency_penalty" :min="-2" :max="2" :step="0.01" style="width: calc(100% - 10)"></el-slider>
                </el-form-item>
              </div>
            </div>
            <el-form-item label="会话描述">
              <el-input v-model="props.chat.dscp" type="textarea" :rows="4" placeholder="简单介绍你的AI"></el-input>
            </el-form-item>
          </el-form>
        </el-scrollbar>
      </div>
      <el-config-provider :locale="zhCn">
        <el-tour v-model="open">
          <el-tour-step
              title="属性面板"
              placement="right"
              description="这是当前会话的属性面板，您可以在这里编辑会话的属性。"
              :target="rightAttrArea"
          />
          <el-tour-step
              title="基本信息"
              placement="top"
              description="您可以在这里修改当前对话的基本信息，比如昵称、头像。"
              :target="baseSettingArea"
          />
          <el-tour-step
              title="系统提示词"
              placement="top"
              description="您可以在这里设置系统提示（System Prompt），比如在这里描述AI该做什么？该怎样做？以及应避免哪些问题。"
              :target="sysMsgArea"
          />
          <el-tour-step
              title="上下文数"
              placement="top"
              :target="contentLengthArea"
          >
            <p>您可以在这里设置上下文数（Context Length），即AI在生成回复时，会考虑前面多少条消息。</p>
            <p>上下文数越高，AI的脑容量就越大，对话越连贯，同时消耗的Token也会越多。</p>
            <p>如果您将上下文数量设置为0，那么AI回答完后会立刻遗忘这条生成的消息。</p>
          </el-tour-step>
          <!--        <el-tour-step-->
          <!--            title="最大Token回复长度"-->
          <!--            :target="maxTokenArea"-->
          <!--            placement="top"-->
          <!--        >-->
          <!--          <p>您可以在这里设置最大Token回复长度（Max Token Length），即AI生成的单次回复最大字数限制。但请注意，Token的数量并不等于汉字的实际数量。</p>-->
          <!--          <p>公式大概如下：<el-tag>1Token</el-tag>≈<el-tag>0.7单词</el-tag>≈<el-tag>0.5汉字</el-tag></p>-->
          <!--        </el-tour-step>-->
          <el-tour-step
              title="话题随机性"
              description="您可以在这里设置话题随机性（Temperature），即AI生成回复时，会考虑多少随机性。数值越大，生成的内容越有创意性"
              :target="temperatureArea"
              placement="top"
          />
          <el-tour-step
              title="话题新鲜度"
              description="您可以在这里设置话题新鲜度（Presence Penalty），数值越大，生成内容时，主题的多样性越高。"
              :target="temperatureArea"
              placement="top"
          />
          <el-tour-step
              title="重复率惩罚力度"
              description="您可以在这里设置重复率惩罚力度（Frequency Penalty），数值越大，生成内容时，重复的字词越少。"
              :target="frequency_penaltyArea"
              placement="top"
          />

        </el-tour>
      </el-config-provider>
    </div>
  </div>

</div>
</template>

<style scoped lang="less">
.room{
  height: 100%;
  display: flex;
  .left{
    flex: 1;
    height: 100%;
    display: flex;
    padding: 0 10px 0 10px;
    flex-direction: column;
    .el-scrollbar{
      flex: 1;
    }
  }
  .right-attrs {
    height: 100%;
    .width-move-bar{
      width: 5px;
      height: 100%;
      position: fixed;
      background-color: transparent;
      cursor: ew-resize;
      border-left: 1px solid #f0f0f0;
      float: left;
      transition: all 0.2s;
      &:hover{
        background-color: dodgerblue;
      }
    }
    .content{
      .title{
        height: 30px;
        line-height: 30px;
        padding-left: 10px;
        background-color: #f0f0f0;
        font-size: 14px;
      }
      .body{
        padding: 10px;
        width: calc(100% - 20px);
      }
    }
  }
}
</style>