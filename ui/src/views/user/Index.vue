<script setup>

import StaticMenuItem from "../../components/menus/StaticMenuItem.vue";
import router from "../../router/index.js";
import UserInfo from "../../components/left-nav/UserInfo.vue";
import globel from "../../libs/globel/index.js";
if (router.currentRoute.value.fullPath.endsWith('/user')){
  router.replace('/user/user-center')
}
</script>

<template>
  <div class="common-layout">
    <el-container style="height: 100%">
      <el-aside width="256px" class="aside">
        <el-scrollbar style="height: calc(100% - 60px)">
          <static-menu-item :conf="{name: '个人信息', key: 'user-center'}"></static-menu-item>
          <static-menu-item :conf="{name: '我的钱包', key: 'purse'}"></static-menu-item>
<!--          <static-menu-item :conf="{name: '邀请返利', key: 'invite'}"></static-menu-item>-->
          <el-badge :value="globel.noReadMessageCount" :max="99" style="width: 100%" class="badge" v-if="globel.noReadMessageCount && globel.noReadMessageCount > 0">
            <static-menu-item :conf="{name: '系统消息', key: 'notice'}" style="margin: 0 12px"></static-menu-item>
          </el-badge>
          <static-menu-item :conf="{name: '系统消息', key: 'notice'}" v-else></static-menu-item>
        </el-scrollbar>
        <user-info></user-info>
      </el-aside>
      <el-main style="padding: 0">
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped lang="less">
.common-layout{
  height: 100%;
  .aside{
    background: rgba(249,249,251,0.5);
    /deep/.badge{
      .el-badge__content.is-fixed{
        right: 24px;
        top: 5px;
      }
    }
  }
}
</style>