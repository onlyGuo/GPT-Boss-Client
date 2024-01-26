<script setup>

import globel from "../../libs/globel/index.js";
const logout = () => {
  globel.currentUser = undefined;
  globel.openLogin = true;
  globel.token = undefined;
  localStorage.removeItem('token')
}
</script>

<template>
  <div>
    <div class="user-info" v-if="globel.currentUser">
      <div class="item" style="width: 150px">
        <img :src="'/api/v1/file/display/' + globel.currentUser.avatar" v-if="globel.currentUser.avatar">
        <img src="../../assets/icons/default-avatar.svg" v-else>
        <div class="account">
          <div class="nicker">{{globel.currentUser.nickname}}</div>
          <div class="email">{{globel.currentUser.email ? globel.currentUser.email : globel.currentUser.username}}</div>
        </div>
      </div>
      <div class="item" style="margin-right: 10px">
        <el-button @click="logout" circle icon="switch-button"></el-button>
      </div>
      <div class="item">
        <el-button circle icon="setting"></el-button>
      </div>
    </div>
    <div class="user-info" v-else>
      <div class="item" style="text-align: center; width: 100%">您未登录，请<a @click="globel.openLogin = true">登录</a>后继续</div>
    </div>
  </div>
</template>

<style scoped lang="less">
.user-info{
  height: 60px;
  background: #F9F9FB;
  box-shadow: 0 3px 6px 1px rgba(0,0,0,0.16);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  .item{
    display: flex;
    align-items: center;
    a{
      color: #596FFF;
      cursor: pointer;
      margin-left: 5px;
      margin-right: 5px;
    }
    img{
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin-right: 10px;
    }
    .account{
      display: flex;
      flex-direction: column;
      .nicker{
        font-size: 14px;
        font-weight: bold;
        color: #333333;
        width: 90px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 10px;
      }
      .email{
        font-size: 12px;
        color: #999999;
        width: 90px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 10px;
      }
    }
  }
}
</style>