<script setup>
import api from "./libs/api/index.js";
import router from "./router/index.js";
import Globel from "./libs/globel/index.js";

window.login = () => {
  Globel.openLogin = true
}
window.goto = (path) => {
  router.push({
    path: path
  })
}
api.get('/api/v1/system/web-info').then(info => {
  Globel.site = info
  Globel.chatModules = info.modules;
  if (router.currentRoute.value.path.startsWith('/install') || router.currentRoute.value.path.startsWith('/close')){
    if (Globel.site.fun.enable && Globel.site.status === 1){
      router.replace({
        path: '/chat'
      })
    }
  }else{
    if (Globel.site.status === 4 || !Globel.site.fun.enable){
      router.replace({
        path: '/close'
      })
    }else if (Globel.site.status === 2 || Globel.site.status === 3){
      router.replace({
        path: '/install'
      })
    }
  }
})
</script>

<template>
  <router-view></router-view>
</template>

<style scoped>
</style>
