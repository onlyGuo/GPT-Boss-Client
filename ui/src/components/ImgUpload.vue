<script setup>
import {ref, getCurrentInstance} from "vue";
import {ElMessage} from "element-plus";
import api from "../libs/api/index.js";
import {Delete} from "@element-plus/icons-vue";

const ctx = getCurrentInstance()
const prop = defineProps({
  src: {
    type: String,
    default: ''
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
  api.post('/api/v1/file/upload', {
    file: file
  }, {headers: {'Content-Type': 'multipart/form-data'}}).then(res => {
    setTimeout(() => {
      ctx.proxy.$emit('update:src', res.fileKey)
    }, 100)
  }).finally(() => {
    imgInput.value.value = null
  })
  // api.get('/api/v1/file/upload').then(res => {
  //   url = res.url;
  //   imgInput.value.value = null
  //   return api.put(url, file, {headers: {'Content-Type': 'application/octet-stream'}})
  // }).then(res => {
  //   const fileKey = url.match(/\.com\/(.*)\?/)[1];
  //   setTimeout(() => {
  //     ctx.proxy.$emit('update:src', fileKey)
  //   }, 100)
  // })
}
const openFileSelect = () => {
  imgInput.value.click()
}
</script>

<template>
  <div class="avatar-uploader" @click.stop="openFileSelect" :style="'width:' + size + 'px; height: ' + size + 'px'">
    <img v-if="src" :src="'/api/v1/file/display/' + src" class="avatar" :style="'width:100%; height: 100%;object-fit: cover;'" />
    <el-icon v-else class="avatar-uploader-icon" :style="'width: 100%; height: 100%'"><Plus /></el-icon>
    <el-icon v-if="src"
             style="position: absolute; z-index: 1; top: 0; right: 0; background-color: red; padding: 1px; color: white; border-radius: 5px"
              @click.stop="$emit('update:src', '')"
    >
      <delete></delete>
    </el-icon>
    <input ref="imgInput" type="file" accept="image/gif,image/jpeg,image/jpg,image/png,image/svg+xml" style="display: none" @change="uploadAvatar">
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