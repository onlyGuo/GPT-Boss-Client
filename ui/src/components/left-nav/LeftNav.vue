<script setup>

import LeftNavItem from "./LeftNavItem.vue";
import globel from "../../libs/globel/index.js";
</script>

<template>
<div>
<!--    <el-tooltip placement="right" effect="light">-->
<!--      <template #content>-->
<!--        <div class="tool-tip">-->
<!--          <div class="title">开放世界</div>-->
<!--          <div class="content">白嫖党和游客的栖息地，白嫖党请在这里繁衍生息~~~</div>-->
<!--        </div>-->
<!--      </template>-->
<!--      <left-nav-item :conf="{icon: 'common'}"></left-nav-item>-->
<!--    </el-tooltip>-->

    <el-tooltip placement="right" effect="light">
      <template #content>
        <div class="tool-tip">
          <div class="title">ChatGPT</div>
          <div class="content">创建独属于你的高速、免排队、的GPT4专属独立频道，高端用户和定制用户专区。</div>
        </div>
      </template>
      <left-nav-item :conf="{icon: 'chat'}"></left-nav-item>
    </el-tooltip>

<!--  <el-tooltip placement="right" effect="light">-->
<!--    <template #content>-->
<!--      <div class="tool-tip">-->
<!--        <div class="title">插件市场</div>-->
<!--        <div class="content">在线购买或开发GPT的插件，只需具备简单的Javascript基础就能完成插件开发。</div>-->
<!--      </div>-->
<!--    </template>-->
<!--    <left-nav-item :conf="{icon: 'appstore'}"></left-nav-item>-->
<!--  </el-tooltip>-->

    <el-tooltip placement="right" effect="light">
      <template #content>
        <div class="tool-tip">
          <div class="title">个人中心</div>
          <div class="content">个人信息、钱包、系统通知等功能都在这儿。</div>
        </div>
      </template>
      <el-badge :value="globel.noReadMessageCount" :max="99" style="width: 100%" class="badge" v-if="globel.noReadMessageCount && globel.noReadMessageCount > 0">
        <left-nav-item :conf="{icon: 'user'}"></left-nav-item>
      </el-badge>
      <left-nav-item :conf="{icon: 'user'}" v-else></left-nav-item>
    </el-tooltip>

<!--    <el-tooltip placement="right" effect="light">-->
<!--      <template #content>-->
<!--        <div class="tool-tip">-->
<!--          <div class="title">GPTs</div>-->
<!--          <div class="content">通过与对话的方式生成个性化的AI，并分享给其他朋友。</div>-->
<!--        </div>-->
<!--      </template>-->
<!--      <left-nav-item :conf="{icon: 'gpts'}"></left-nav-item>-->
<!--    </el-tooltip>-->


<!--    <el-tooltip placement="right" effect="light" v-if=" globel.currentUser &&  globel.currentUser.id === 1">-->
<!--      <template #content>-->
<!--        <div class="tool-tip">-->
<!--          <div class="title">系统管理</div>-->
<!--          <div class="content">管理员和站长办公的专用场所。</div>-->
<!--        </div>-->
<!--      </template>-->
<!--      <left-nav-item :conf="{icon: 'setting'}"></left-nav-item>-->
<!--    </el-tooltip>-->
</div>
</template>

<style scoped lang="less">
/deep/.badge{
  .el-badge__content.is-fixed{
    right: 24px;
    top: 19px;
  }
}
.tool-tip{
  margin: 5px;
  .title{
    font-size: 14px;
    font-family: Source Han Sans CN-Bold, Source Han Sans CN;
    font-weight: bold;
    color: #58597A;
    line-height: 20px;
    margin-bottom: 6px;
  }
  .content{
    font-size: 12px;
    color: #888888;
  }
}
</style>
<style>
.el-popper.is-light{
  box-shadow: 2px 2px 16px 1px rgba(84,90,128,0.24)!important;
  border-radius: 10px;
}
</style>