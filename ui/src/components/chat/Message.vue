<script setup>
import globel from "../../libs/globel/index.js";
// import MarkdownIt from 'markdown-it';
// import markdownItKatex from 'markdown-it-katex'
// import hljs from 'highlight.js'

import {nextTick, onMounted, ref, watch} from "vue";

// 引入默认样式
// import 'highlight.js/styles/github-dark-dimmed.min.css'
import {CopyDocument} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {MdPreview} from "md-editor-v3";
import 'md-editor-v3/lib/style.css';
const props = defineProps({
  message: {
    type: Object,
    required: true,
    default: {
      "id": 126925,
      "chatRoomId": 0,
      "userId": 0,
      "message": "@Cheryl \n> [广告] [承接微信机器人/软件/网页/APP/小程序定制开发,QQ1819647938](http://wpa.qq.com/msgrd?v=3&uin=1819647938&site=qq&menu=yes)\n> [广告] [官方渠道OpenAI ApiKey - 人民币直冲自动到账/汇率7.3](https://dev.icoding.ink)\n> [广告] [iCoding论坛，正在建设中，欢迎访问](https://bbs.icoding.ink)\n> [广告位招租(联系邮箱)：guosk373@gmail.com]\n\n建议空间、申诉机制、优化算法、精细化管理、提升效能",
      "createTime": "2023-10-03 13:09:00",
      "nicker": null,
      "avatar": null,
      "messageIndex": "623e9ffded4f4d19b8d5f81e05b0f63d",
      "atUserId": 4593,
      "plus": false
    }
  },
  onHtmlChanged: {
    type: Function,
    default: () => {}
  }
})
// const md = new MarkdownIt({
//   highlight: function (str, lang) {
//     if (lang === 'vue') {
//       lang = 'html'
//     }
//     if (lang === 'vba'){
//       lang = 'vbscript'
//     }
//     // 此处判断是否有添加代码语言
//     if (lang && hljs.getLanguage(lang)) {
//       try {
//         // 得到经过highlight.js之后的html代码
//         const preCode = hljs.highlight(lang, str, true).value
//         // 以换行进行分割
//         const lines = preCode.split(/\n/).slice(0, -1)
//         // 添加自定义行号
//         let html = lines.map((item, index) => {
//           return '<li><span class="line-num" data-line="' + (index + 1) + '"></span>' + item + '</li>'
//         }).join('')
//         html = '<ol>' + html + '</ol>'
//         // 添加代码语言
//         if (lines.length) {
//           html += '<b class="name">' + lang + '</b>'
//         }
//         return '<pre class="hljs"><code>' +
//             html +
//             '</code></pre>'
//       } catch (__) {}
//     }
//     // 未添加代码语言，此处与上面同理
//     const preCode = md.utils.escapeHtml(str)
//     const lines = preCode.split(/\n/).slice(0, -1)
//     let html = lines.map((item, index) => {
//       return '<li><span class="line-num" data-line="' + (index + 1) + '"></span>' + item + '</li>'
//     }).join('')
//     html = '<ol>' + html + '</ol>'
//     return '<pre class="hljs"><code>' +
//         html +
//         '</code></pre>'
//   }
// });
// md.use(markdownItKatex);
const getHtml = () => {
  let cmsg = props.message.content.trim();
  if (!cmsg){
    return '<div class="msg-loading"></div>'
  }

  // 在公式前后加上换行符
  let reg = /\$\$([^\$]+)\$\$/g;
  let result = cmsg.match(reg);
  if (result){
    result.forEach(item => {
      cmsg = cmsg.replace(item, `\n${item}\n`)
    })
  }

  // @xxx 开头 或@xxx结尾
  cmsg = cmsg.replace('@ChatGPT', '[@ChatGPT](#)')
  if (cmsg.startsWith('@')){
    const at = cmsg.substring(0, cmsg.indexOf(' '));
    cmsg = cmsg.replace(at, `[${at}](#)`)
  }
  // return md.render(cmsg)
  return cmsg;
}
// const finalMd = ref('')
// finalMd.value = getHtml()

const messageRef = ref(undefined)
const copy = () => {
  const range = document.createRange();
  range.selectNodeContents(messageRef.value);
  const selection = window.getSelection();
  selection.removeAllRanges();
  selection.addRange(range);
  document.execCommand('copy');
  selection.removeAllRanges();
  ElMessage.success('复制成功')
}

// watch(() => props.message.content, () => {
//   console.log('message change')
//   finalMd.value = getHtml()
// })
const onResize = () => {
  props.onHtmlChanged();
}

</script>

<template>
<div class="message-box">
  <div class="left" v-if="(props.message.role == 'assistant' || props.message.userId !== globel.currentUser?.id) && props.message.role !== 'System'">
    <img :class="{plus: props.message.plus}" :src="'/api/v1/file/display/' + props.message.avatar" v-if="props.message.avatar" />
    <img :class="{plus: props.message.plus}" src="../../assets/icons/default-avatar.svg" v-else-if="!props.message.role == 'assistant'" />
    <img :class="{plus: props.message.plus}" src="../../assets/logo.svg" v-else />
    <div class="info">
      <div class="name">
        <img src="../../assets/icons/vip-line.svg" v-if="props.message.plus" class="tag-icon" />
        {{props.message.nicker}}
        <span class="time">{{props.message.createTime}}</span>
      </div>
      <div class="message" ref="messageRef" :class="{plus: props.message.plus}" style="padding: 6px 16px">
        <md-preview :model-value="getHtml()" :on-html-changed="props.onHtmlChanged" :no-img-zoom-in="true" v-resize="onResize"/>
      </div>
      <div class="tools-bar">
        <div class="item" @click="copy">
          <el-icon><CopyDocument></CopyDocument></el-icon>
        </div>
      </div>
    </div>
  </div>
  <div class="right" v-else-if="props.message.userId === globel.currentUser?.id">
    <img :class="{plus: props.message.plus}" :src="'/api/v1/file/display/' + props.message.avatar" v-if="props.message.avatar" />
    <img :class="{plus: props.message.plus}" :src="'/api/v1/file/display/' + globel.currentUser.avatar" v-else-if="globel.currentUser?.avatar" />
    <img :class="{plus: props.message.plus}" src="../../assets/icons/default-avatar.svg" v-else />
    <div class="info">
      <div class="name">
        <span class="time">{{props.message.createTime}}</span>
        {{props.message.nicker ? props.message.nicker : (globel.currentUser ? globel.currentUser.nickname: 'Guest')}}
        <img src="../../assets/icons/vip-line.svg" v-if="props.message.plus" class="tag-icon" />
      </div>
      <div class="message" :class="{plus: props.message.plus}">
        <md-preview :model-value="getHtml()" :on-html-changed="props.onHtmlChanged" :no-img-zoom-in="true" v-resize="onResize"/>
      </div>
    </div>
  </div>
  <div class="system" v-else-if="props.message.nicker === 'System'">
    <div class="message" v-html="getHtml()"></div>
  </div>
</div>
</template>

<style scoped lang="less">
.message-box{
  //display: flex;
  margin-bottom: 10px;
  margin-top: 10px;
  margin-left: 10px;
  max-width: 100%;

  .left{
    display: flex;
    align-items: start;
    max-width: 100%;
    img{
      width: 48px;
      height: 48px;
      margin-right: 10px;
      background: rgba(204,204,204,0.15);
      border-radius: 16px;
      &.plus{
        border: 1px solid #ffeb13;
      }
    }
    .info{
      max-width: 75%;
      &:hover{
        .tools-bar{
          .item{
            // 放大
            transform: scale(1);
          }
        }
      }
      .name{
        font-size: 14px;
        font-weight: bold;
        color: #333;
        .time{
          font-size: 12px;
          font-weight: normal;
          color: #999;
          margin-left: 10px;
        }
        img.tag-icon{
          background: none;
          margin-bottom: -14px;
          width: 40px;
          height: 40px;
        }
      }
      .message{
        font-size: 14px;
        font-weight: normal;
        color: #000235;
        line-height: 24px;
        background: #F5F5F5;
        border-radius: 16px 16px 16px 16px;
        padding: 6px 16px;
        display: inline-block;
        max-width: 100%;
        overflow: auto;
        transition: all .3s;
        img{
          max-width: 100%;
        }
        &.plus{
          background: #414141;
          border: 1px solid #ffeb13;
          color: #ffeb13;
          /deep/a{
            color: #f594ff;
            text-decoration: none;
          }
        }
        &:hover{
          box-shadow: inset 0px 3px 6px 1px rgba(137,137,162,0.24);
          transform: translateY(3px);
        }
      }
      .tools-bar{
        display: flex;
        //align-items: center;
        justify-content: flex-end;
        //float: right;
        height: 30px;
        .item{
          margin-left: 10px;
          cursor: pointer;
          transition: all .3s;
          height: 30px;
          width: 30px;
          border-radius: 15px;
          background: #F5F5F5;
          line-height: 30px;
          text-align: center;
          // 缩小
          transform: scale(0);

          &:hover{
            color: #409EFF;
            background: #e3e3e3;
            box-shadow: 0 3px 6px 1px rgba(0,0,0,0.16);
          }
        }
      }
    }
  }
  .right{
    display: flex;
    align-items: start;
    flex-direction: row-reverse;
    max-width: 100%;
    img{
      width: 48px;
      height: 48px;
      margin-left: 10px;
      background: rgba(204,204,204,0.15);
      border-radius: 16px;
      &.plus{
        border: 1px solid #ffeb13;
      }
    }
    .info{
      max-width: 75%;
      text-align: right;
      .name{
        font-size: 14px;
        font-weight: bold;
        color: #333;
        text-align: right;
        .time{
          font-size: 12px;
          font-weight: normal;
          color: #999;
          margin-left: 10px;
        }
        img.tag-icon{
          background: none;
          margin-bottom: -14px;
          width: 40px;
          height: 40px;
        }
      }
      .message{
        font-size: 14px;
        font-weight: normal;
        color: #000235;
        line-height: 24px;
        background: #E4E8FF;
        border-radius: 16px 16px 16px 16px;
        padding: 6px 16px;
        display: inline-block;
        text-align: left;
        max-width: 100%;
        overflow: auto;
        img{
          max-width: 100%;
        }
        &.plus{
          background: #414141;
          border: 1px solid #ffeb13;
          color: #ffeb13!important;
          /deep/a{
            color: #f594ff;
            text-decoration: none;
          }
          /deep/.md-editor-preview{
            color: #ffeb13!important;
          }
        }
      }
    }
  }
  .system{
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    color: #666;
  }
}
///deep/blockquote{
//  margin: 0;
//  background: rgba(0, 0, 0, .1);
//  padding: 10px;
//  border-radius: 5px;
//}
///deep/p{
//  margin: 0;
//  white-space: pre-wrap;
//  overflow: auto;
//}
///deep/a{
//  color: #2600E6;
//  text-decoration: none;
//}
///deep/code{
//  background: rgba(0, 0, 0, .1);
//  padding: 2px 5px;
//  border-radius: 5px;
//}
///deep/.code-toolbar .toolbar .toolbar-item {
//  span, button{
//    padding: 3px 5px;
//    margin: 3px;
//    cursor: pointer;
//  }
//}
///deep/pre.hljs {
//  border-radius: 5px;
//  position: relative;
//  overflow-y: auto;
//  font-family: "Source Code Pro", "SF Mono", Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
//  font-size: 14px;
//}
//
///deep/pre.hljs ol {
//  list-style: decimal;
//  margin: 0;
//  margin-left: 40px;
//  padding: 0;
//}
//
///deep/pre.hljs ol li {
//  list-style: decimal-leading-zero;
//  position: relative;
//  padding-left: 10px;
//}
//
///deep/pre.hljs ol li .line-num {
//  position: absolute;
//  left: -40px;
//  top: 0;
//  width: 40px;
//  height: 100%;
//  border-right: 1px solid rgba(0, 0, 0, .66);
//}
//
///deep/pre.hljs b.name {
//  position: absolute;
//  top: 2px;
//  right: 12px;
//  z-index: 10;
//  color: #999;
//  pointer-events: none;
//}
/deep/.md-editor-preview-wrapper{
  padding: 0;
}
/deep/.md-editor{
  background: transparent;
}
/deep/.default-theme blockquote{
  margin: 0;
}
///deep/.default-theme p{
//  line-height: 20px;
//}
</style>
<style>
.msg-loading {
  margin-bottom: 27px;
  margin-top: 14px;
  margin-left: 20px;
  margin-right: 20px;
  position: relative;
  width: 1px;
  height: 1px;
}

.msg-loading:before,
.msg-loading:after {
  position: absolute;
  display: inline-block;
  width: 15px;
  height: 15px;
  content: "";
  border-radius: 100%;
  background-color: #000;
}

.msg-loading:before {
  left: -15px;
  animation: ball-pulse infinite 0.75s -0.4s cubic-bezier(0.2, 0.68, 0.18, 1.08);
}

.msg-loading:after {
  right: -15px;
  animation: ball-pulse infinite 0.75s cubic-bezier(0.2, 0.68, 0.18, 1.08);
}

@keyframes ball-pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }

  50% {
    transform: scale(0.1);
    opacity: 0.6;
  }

  100% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>