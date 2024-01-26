<script setup>
import api from "../../libs/api/index.js";
import {ref} from "vue";
import ImgUpload from "../../components/ImgUpload.vue";
import {ElMessage} from "element-plus";

const user = ref({})
api.get('/api/v1/users/info').then(res => {
  user.value = res;
})

const resetAccessKey = () => {
  api.put('/api/v1/users/accessKey').then(res => {
    user.value.accessKey = res.accessKey
    ElMessage.success('AccessKey已刷新');
  })
}

const submit = () => {
  if (!user.value.nickname){
    ElMessage.error('用户昵称不能为空');
  }
  api.put('/api/v1/users/info', user.value).then(res => {
    ElMessage.success('保存成功');
  })
}

</script>

<template>
<div class="uc">
  <el-form label-width="90">
    <el-form-item label="用户昵称">
      <el-input placeholder="请输入用户昵称" v-model="user.nickname"></el-input>
    </el-form-item>
    <el-form-item label="用户头像">
      <img-upload v-model:src="user.avatar"></img-upload>
    </el-form-item>
    <el-form-item label="用户名">
      <el-input placeholder="请输入用户名" disabled v-model="user.username"></el-input>
    </el-form-item>
    <el-form-item label="用户邮箱">
      <el-input placeholder="请输入用户邮箱" disabled v-model="user.email"></el-input>
    </el-form-item>
    <el-form-item label="AccessKey">
      <el-input placeholder="点击生成AccessKey" disabled v-model="user.accessKey">
        <template #append>
          <el-button @click="resetAccessKey">生成/重建 AccessKey</el-button>
        </template>
      </el-input>
      <div style="color: #888888; font-size: 12px">AccessKey是您的账号对外提供API调用的唯一凭据。请妥善保管。</div>
      <div style="color: #888888; font-size: 12px">若您的AccessKey被泄露，请重新生成新的AccessKey。</div>
    </el-form-item>
    <el-form-item label="用户状态">
      <span v-if="user.status === 0">正常</span>
      <span v-if="user.status === -1">暂停发言(截止至:{{user.prohibitTime}})</span>
    </el-form-item>
    <el-button style="width: 100%" type="primary" @click="submit">保存</el-button>
  </el-form>
</div>
</template>

<style scoped lang="less">
.uc{
  padding: 16px;
}
</style>