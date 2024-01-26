<script setup>
import {ref, getCurrentInstance} from "vue";
import {ElMessage} from "element-plus";
import api from "../libs/api/index.js";

const ctx = getCurrentInstance()
const prop = defineProps({
  src: {
    type: Array,
    default: []
  },
  'update:src': {
    type: Function,
    default: () => {}
  },
  size: {
    type: Number,
    default: 100
  }
})

const emits = defineEmits(['update:src'])
const imgInput = ref(null)

const uploadUrl = ref('')
const uploadAvatar = () => {
  const file = imgInput.value.files[0]
  if (!file) return
  let url = ''
  api.get('/api/v1/file/upload').then(res => {
    url = res.url;
    imgInput.value.value = null
    return api.put(url, file, {headers: {'Content-Type': 'application/octet-stream'}})
  }).then(res => {
    const fileKey = url.match(/\.com\/(.*)\?/)[1];
    setTimeout(() => {
      ctx.proxy.$emit('update:src', prop.src.push(fileKey));
    }, 100)
  })
}
const openFileSelect = () => {
  imgInput.value.click()
}
</script>

<template>
  <div>
    <div class="avatar-uploader" :style="'width:' + size + 'px; height: ' + size + 'px'" v-for="src in prop.src" :key="src">
      <img v-if="src" :src="'/api/v1/file/display/' + src" class="avatar" :style="'width:' + size + 'px; height: ' + size + 'px'" />
    </div>
    <div class="avatar-uploader" @click="openFileSelect" :style="'width:' + size + 'px; height: ' + size + 'px'">
      <el-icon class="avatar-uploader-icon" :style="'width:' + size + 'px; height: ' + size + 'px'"><Plus /></el-icon>
      <input ref="imgInput" type="file" accept="image/gif,image/jpeg,image/jpg,image/png,image/svg+xml" style="display: none" @change="uploadAvatar">
    </div>
  </div>
</template>

<style scoped lang="less">
.avatar-uploader{
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  display: inline-block;
  margin-right: 10px;
  margin-bottom: 10px;
  .avatar {
    display: block;
  }

  &:hover {
    border-color: var(--el-color-primary);
  }
}

/deep/.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  text-align: center;
}
</style>