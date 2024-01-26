<script setup>

import LeftNav from "./components/left-nav/LeftNav.vue";
import globel from "./libs/globel/index.js";
import {ref, watch} from "vue";
import api from "./libs/api/index.js";
import {ElMessage} from "element-plus";
import UserSocket from "./utils/UserSocket.js";
// showLogin.value = !globel.isLogin;

const loginForm = ref({
  email: "",
  username: "",
  password: "",
  checkPassword: "",
  valiCode: "",
  inviteCode: "",
  nickname: "",
  title: '登录'
})
UserSocket.start()
const sendCodeLoading = ref(false)
const sendCodeTime = ref(0)
let timer = null
const sendCode = () => {
  sendCodeLoading.value = true
  let urlPath = '/api/v1/users/register-email-code';
  if (loginForm.value.title === '找回密码'){
    urlPath = '/api/v1/users/reset-password-email-code'
  }
  api.post(urlPath, loginForm.value).then(res => {
    ElMessage.success("验证码已发送，请前往邮箱查看");
    sendCodeTime.value = 60
    timer = setInterval(() => {
      sendCodeTime.value--
      if (sendCodeTime.value === 0) {
        clearInterval(timer)
      }
    }, 1000)
  }).finally(() => {
    sendCodeLoading.value = false
  })
}


globel.token = localStorage.getItem('token')
const handleSubmit = ({values, errors}) => {
  if (!errors || errors.length === 0){
    if (loginForm.value.title === '登录') {
      api.post('/api/v1/users/login', loginForm.value).then(res => {
        globel.token = res.token
        globel.currentUser = res
        globel.isLogin = true
        globel.openLogin = false
        localStorage.setItem('token', res.token)
        ElMessage.success('登陆成功')
        UserSocket.login()
        api.get('/api/v1/system-message/no-read-count').then(r => {
          globel.noReadMessageCount = r.count;
        })
      })
    }else if (loginForm.value.title === '注册'){
      api.post('/api/v1/users/register', loginForm.value).then(res => {
        api.post('/api/v1/users/login', loginForm.value).then(res => {
          globel.token = res.token
          globel.currentUser = res
          globel.isLogin = true
          globel.openLogin = false
          localStorage.setItem('token', res.token)
          ElMessage.success('登陆成功')
          UserSocket.login()
          api.get('/api/v1/system-message/no-read-count').then(r => {
            globel.noReadMessageCount = r.count;
          })
        })
      })
    }else if (loginForm.value.title === '找回密码'){
      api.post('/api/v1/user/reset-password', loginForm.value).then(res => {
        api.post('/api/v1/users/login', loginForm.value).then(res => {
          globel.token = res.token
          globel.currentUser = res
          globel.isLogin = true
          globel.openLogin = false
          localStorage.setItem('token', res.token)
          ElMessage.success('登陆成功')
          UserSocket.login()
          api.get('/api/v1/system-message/no-read-count').then(r => {
            globel.noReadMessageCount = r.count;
          })
        })
      })
    }
  }
}
api.get('/api/v1/users/info').then(res => {
  globel.currentUser = res
  globel.isLogin = true
  UserSocket.login()
  api.get('/api/v1/system-message/no-read-count').then(noredRes => {
    globel.noReadMessageCount = noredRes.count;
  })
}).finally(() => {})
const gotoDownload = () => {
  location.href = 'https://chat.icoding.ink/mobile.html'
}
</script>

<template>
    <div class="common-layout">
        <el-container>
            <el-header class="common-header">
               <div class="left">
                 <img :src="'/api/v1/file/display/' + globel.site.logo" v-if="globel.site && globel.site.logo" />
                 <img src="./assets/logo.svg" v-else />
                 <div class="title-info">
                   <div class="title-text">{{globel.site && globel.site.name ? globel.site.name : ''}}</div>
                   <div class="ipc-num">{{globel.site && globel.site.icp ? globel.site.icp : ''}}</div>
                 </div>
               </div>
              <div class="right">
                <el-tooltip placement="bottom" effect="light">
                  <template #content >
                    <div style="text-align: center">
                      <div style="font-size: 16px; margin-top: 10px">支持IOS/Android/鸿蒙OS</div>
                      <img src="./assets/icons/download-qrcode.png" style="width: 200px; height: 200px" />
                      <div style="margin-bottom: 5px; color: #666666">使用手机扫一扫安装</div>
                    </div>
                  </template>
                  <div class="download" @click="gotoDownload">
                    <img src="./assets/icons/qr-code.svg" />
                    App下载
                  </div>
                </el-tooltip>
              </div>
            </el-header>
            <el-container style="height: calc(100vh - 56px)">
                <el-aside class="common-aside" width="56px">
                    <left-nav></left-nav>
                </el-aside>
                <el-main style="padding: 0; height: calc(100vh - 56px)">
                  <el-dialog v-model="globel.openLogin" :title="loginForm.title" :width="500" center align-center>
                    <el-form :label-width="80">
                      <el-form-item label="用户名" required v-if="loginForm.title === '注册' || (loginForm.title === '登录' && !globel.site.fun.enableEmail)">
                        <el-input v-model="loginForm.username" :placeholder="loginForm.title === '注册' ? '请输入用户名' : '请输入用户名或邮箱'"></el-input>
                      </el-form-item>
                      <el-form-item label="昵称" required v-if="loginForm.title === '注册'">
                        <el-input v-model="loginForm.nickname" placeholder="请输入昵称"></el-input>
                      </el-form-item>
                      <el-form-item label="电子邮箱" required v-if="globel.site.fun.enableEmail">
                        <el-input v-model="loginForm.email" placeholder="请输入电子邮箱"></el-input>
                      </el-form-item>
                      <el-form-item label="验证码" required v-if="loginForm.title !== '登录' && globel.site.fun.enableEmail">
                        <el-input v-model="loginForm.valiCode" placeholder="请输入验证码">
                          <template #append>
                            <el-button @click="sendCode" :loading="sendCodeLoading" :disabled="sendCodeTime > 0">发送验证码<span v-if="sendCodeTime > 0">({{sendCodeTime}})</span></el-button>
                          </template>
                        </el-input>
                      </el-form-item>
                      <el-form-item label="密码" required>
                        <el-input v-model="loginForm.password" placeholder="请输入密码" type="password"></el-input>
                      </el-form-item>
                      <el-form-item label="确认密码" required v-if="loginForm.title !== '登录'">
                        <el-input v-model="loginForm.checkPassword" placeholder="请重新输入密码" type="password"></el-input>
                      </el-form-item>
                      <el-form-item label="邀请码" v-if="loginForm.title === '注册'">
                        <el-input v-model="loginForm.inviteCode" placeholder="请输入邀请码（选填）"></el-input>
                      </el-form-item>
                      <el-button type="primary" style="width: 100%" @click="handleSubmit">{{loginForm.title}}</el-button>
                    </el-form>
                    <div style="width: 100%; height: 1px; background-color: #eeeeee; margin-top: 30px; margin-bottom: 30px"></div>
                    <div class="login-box-footer">
                      <a @click="loginForm.title = '注册'" style="margin: 0 10px;" v-if="(loginForm.title === '登录' || loginForm.title === '找回密码') && globel.site.fun.enableRegister">注册账号</a>
                      <a @click="loginForm.title = '登录'" style="margin: 0 10px;" v-if="loginForm.title === '注册' || loginForm.title === '找回密码'">去登录</a>
                      <a @click="loginForm.title = '找回密码'" v-if="(loginForm.title === '注册' || loginForm.title === '登录') && globel.site.fun.enableEmail">忘记密码</a>
                    </div>
                  </el-dialog>
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style scoped lang="less">
.common-layout{
    background-color: white;
    .common-header{
        height: 56px;
        background: #F1F3FF;
       .left{
         font-size: 18px;
         font-family: Source Han Sans CN-Heavy, Source Han Sans CN;
         font-weight: 800;
         color: #000235;
         line-height: 56px;
         padding: 0 34px;
         float: left;
         img{
           width: 30px;
           height: 30px;
           vertical-align: middle;
         }
         .title-info{
            display: inline-block;
            vertical-align: middle;
            margin-left: 12px;
            height: 56px;
            .title-text{
              font-size: 18px;
              font-family: Source Han Sans CN-Heavy, Source Han Sans CN;
              font-weight: 800;
              color: #000235;
              line-height: 25px;
              margin-top: 8px;
            }
            .ipc-num{
              font-size: 12px;
              font-family: Source Han Sans CN-Regular, Source Han Sans CN;
              font-weight: 400;
              color: #666666;
              line-height: 12px;
            }
         }
       }
      .right{
        float: right;
        height: 56px;
        display: flex;
        align-items: center;
        .download{
          width: 106px;
          height: 32px;
          background: #FFFFFF;
          border-radius: 16px 16px 16px 16px;
          font-size: 14px;
          font-weight: normal;
          color: #000235;
          line-height: 32px;
          display: flex;
          align-items: center;
          cursor: pointer;
          img{
            height: 24px;
            width: 24px;
            background: #FFFFFF;
            box-shadow: 0px 3px 6px 1px rgba(0,0,0,0.16);
            border-radius: 50%;
            margin-right: 8px;
            margin-left: 4px;
          }
        }
      }
    }
    .common-aside{
        background: #F9F9FB;
        text-align: center;
    }
  .login-box-footer{
    text-align: center;
    a{
      color: dodgerblue;
      cursor: pointer;
    }
  }
}
</style>