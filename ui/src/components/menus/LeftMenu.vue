<script setup>

import LeftMenuItem from "./LeftMenuItem.vue";
import api from "../../libs/api/index.js";
import {ref} from "vue";
import router from "../../router/index.js";

const menus = ref([])
api.get('/api/v1/common-channel').then(res => {
  menus.value = res;
})
</script>

<template>
<div class="left-menu">
  <left-menu-item v-for="item in menus" :key="item.id" :conf="item" @click="router.replace({
    path: '/common/' + item.id
  })"></left-menu-item>
</div>
</template>

<style scoped lang="less">
.left-menu{
}
</style>