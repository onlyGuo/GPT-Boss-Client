<script setup>

import {MdPreview} from "md-editor-v3";
import 'md-editor-v3/lib/style.css';
import {nextTick, ref} from "vue";
import api from "../libs/api/index.js";
import router from "../router/index.js";
import globel from "../libs/globel/index.js";

const content = ref('');
const webInfo = ref({});
api.get('../install.md').then(res => {
  content.value = res;
  api.get('/api/v1/system/manager-key').then(key => {
    content.value = content.value.replace('{{managerKey}}', key);
  })
  api.get('/api/v1/system/web-info').then(info => {
    webInfo.value = info;
    if (webInfo.value.status === 1){
      finish();
    }
    // scrollToBottom();
  })
})

const log = ref('使用须知：\n' +
    '1. 请确认你已经在GPT-BOSS中配置了正确的数据库。\n' +
    '2. 点击初始化按钮完成数据库初始化.\n' +
    '3. 初始化完成后，点击开始使用按钮进入聊天界面。\n\n\n' +
    '欢迎使用AI对话程序！我们致力于提供一个安全、友好、有益的对话环境。为了保证每位用户都能享受到最佳的体验，请遵循以下守则：\n' +
    '\n' +
    '请以礼貌和尊重的态度与AI进行交流。\n' +
    '避免使用侮辱性、歧视性或攻击性的语言。\n' +
    '\n' +
    '不要共享您或他人的个人信息，包括但不限于姓名、地址、电话号码或电子邮件地址。\n' +
    '不要要求AI透露其它用户的个人信息。\n' +
    '\n' +
    '不要利用AI进行任何违法行为，包括传播非法内容或参与非法活动。\n' +
    '不要尝试破坏或干扰AI程序的正常运行。\n' +
    '\n' +
    '避免发布不适当的内容，如色情、暴力或其他可能令人不适的材料。\n' +
    '请保持对话的文明和专业，即使在不同意见的情况下也应如此。\n' +
    '\n' +
    '尽可能清晰地表达您的问题或评论，这有助于AI更准确地理解和响应。\n' +
    '如果AI的回答不准确，请耐心指正或重新表述您的问题。')
const scrollBar = ref(null);
const scrollToBottom = () => {
  nextTick().then(() => {
    scrollBar.value.setScrollTop(scrollBar.value.wrapRef.scrollHeight - scrollBar.value.$el.clientHeight);
  })
}
const toNext = () => {
  api.get('/api/v1/system/web-info').then(info => {
    webInfo.value = info;
  })
}

const inited = ref(false)
const initDb = () => {
  log.value = '准备安装数据库...\n';
  scrollToBottom();
  api.fetch('/api/v1/system/init-db', "POST", {}, (done, value) => {
    if (done){
      if (log.value.includes('点击”完成“按钮')){
        inited.value = true;
      }
      return;
    }
    log.value += value;
    scrollToBottom();
  })
}
const finish = () => {
  router.replace({path: '/'})
}
</script>

<template>
  <div class="install">
    <div class="install-content" v-if="webInfo.status === 2">
      <div class="install-title">[{{webInfo.name}}] 数据库初始化</div>
      <div class="install-desc">[{{webInfo.name}}] 是一款基于 ChatGPT 的聊天机器人，可以用于聊天、问答、翻译、绘图、制表、情感分析等多种场景。</div>
      <div class="install-body">
        <div style="padding: 10px">
          <el-scrollbar height="400px" ref="scrollBar">
            <pre class="install-log">{{log}}</pre>
          </el-scrollbar>
        </div>
        <div style="height: 50px; text-align: center">
          <el-button type="primary" @click="initDb" v-if="!inited">初始化数据库</el-button>
          <el-button type="primary" @click="finish" v-else>完成</el-button>
        </div>
      </div>
    </div>
    <div class="install-content" v-else>
      <div class="install-title">ChatGPT CN Client 安装程序</div>
      <div class="install-desc">ChatGPT CN Client 是一款基于 ChatGPT 的聊天机器人，可以用于聊天、问答、翻译、绘图、制表、情感分析等多种场景。</div>
      <div class="install-body">
        <md-preview :model-value="content" />
        <div style="height: 50px; text-align: center">
          <el-button type="primary" @click="toNext">下一步</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
.install{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  .install-content{
    width: 100%;
    max-width: 800px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    box-shadow: 0 3px 6px 1px rgba(0,0,0,0.16);
    border-radius: 10px;
    overflow-y: hidden;

    .install-log{
      width: calc(100% - 20px);
      padding: 10px;
      height: 100%;
      overflow: auto;
      color: #666666;
      white-space: pre-wrap;
    }

    .install-title{
      font-size: 24px;
      font-weight: bold;
      padding: 10px;
      background: #F3F4FA;
      width: calc(100% - 20px);
      text-align: center;
    }
    .install-desc{
      font-size: 12px;
      margin-bottom: 20px;
      background: #F3F4FA;
      padding: 10px;
      width: calc(100% - 20px);
      text-align: center;
      color: #666666;
    }
    .install-body{
      width: 100%;
      height: 100%;
      overflow: auto;
      .md-editor{
        width: 100%;
        height: 100%;
        .md-editor-preview{
          padding: 20px;
        }
      }
    }
  }
}
</style>