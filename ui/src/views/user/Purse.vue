<script setup>

import {ArrowUp, Plus, SetUp} from "@element-plus/icons-vue";
import {ref, watch} from "vue";
import api from "../../libs/api/index.js";
import Globel from "../../libs/globel/index.js";
const balance = ref({})
api.get('/api/v1/users/balance').then(res => {
  balance.value = res;
})
const activeName = ref('xiaofei');

const orders = ref([])
api.get('/api/v1/order/my-orders').then(res => {
  orders.value = res.list
})

const data = ref([]);
const form = ref({
  page: 1,
  pageSize: 15,
  total: 0
})
const loading = ref(false)
const getData = () => {
  loading.value = true
  api.get('/api/v1/chat/tokens?page=' + form.value.page + '&limit=' + form.value.pageSize).then(res => {
    data.value = res.list;
    form.value.total = res.totalSize;
  }).finally(() => {
    loading.value = false
  })
}
getData();
watch(() => form.value.page, () => {
  getData();
})
const openRecharge = ref(false)
const rechargeMoney = ref('1')
const payValue = ref('7.3')
const doRecharge = () => {
  api.post('/api/v1/order/create', {
    orderType: 0,
    payType: 0,
    orderAmount: rechargeMoney.value,
    orderPayType: 0,
    description: Globel.site.name + '充值' + rechargeMoney.value + '刀',
    subject: '充值' + rechargeMoney.value + '刀',
    returnUrl: location.href,
  }).then(res => {
    window.location.href = res.payUrl
  })
}
watch(() => rechargeMoney.value, () => {
  if(rechargeMoney.value){
    // 解决JS浮点数计算精度问题
    payValue.value = (rechargeMoney.value * 7.3).toFixed(2)
  }else{
    payValue.value = 0
  }
})

</script>

<template>
<div class="purse">
  <div class="cards">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>钱包余额</span>
          <el-button class="button" text type="primary" @click="openRecharge = true"><el-icon><plus></plus></el-icon>充值余额</el-button>
        </div>
      </template>
      <div>
        <div>余额：{{balance.balance}} $</div>
        <div style="font-size: 12px; color: gray; margin-bottom: 10px">钱包余额（美元）会在您使用付费模型时进行扣除，若您的账户余额<=0时，将无法使用付费模型（其中1token≈0.5汉字≈0.7单词）</div>
        <div style="font-size: 12px; color: gray">您可以<a href="javascript;" class="link">点击此处</a> 查看各个模型的价格。此外您还可以<a href="javascript;" class="link">点击此处</a>查看价格计算公式。</div>
      </div>
    </el-card>
  </div>
  <el-tabs v-model="activeName" class="demo-tabs">
    <el-tab-pane label="额度消耗记录" name="xiaofei">
      <el-table :data="data" v-loading="loading">
        <el-table-column prop="modelName" label="使用模型"></el-table-column>
        <el-table-column prop="createTime" label="消耗时间"></el-table-column>
        <el-table-column prop="promptTokenCount" label="prompt Tokens" width="135"></el-table-column>
        <el-table-column prop="generatedTokenCount" label="complete Tokens" width="143"></el-table-column>
        <el-table-column prop="price" label="官方费用" width="100">
          <template #default="{row}">
            <span v-if="row.price">{{row.price}} $</span>
          </template>
        </el-table-column>
        <el-table-column prop="userDiscountRate" label="折扣倍率" width="100"></el-table-column>
        <el-table-column prop="userFinalPrice" label="最终消耗" width="100">
          <template #default="{row}">
            <span v-if="row.userFinalPrice">{{row.userFinalPrice}} $</span>
            <span v-else>0.0000 $</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top: 10px" background layout="prev, pager, next" :total="form.total" :page-size="form.pageSize" :default-current-page="form.page"  v-model:current-page="form.page"/>
    </el-tab-pane>
    <el-tab-pane label="支付账单记录" name="chongzhi">
      <el-table :data="orders">
        <el-table-column prop="orderCreateTime" label="下单时间" width="170"></el-table-column>
        <el-table-column prop="subject" label="订单商品"></el-table-column>
        <el-table-column prop="orderAmount" label="订单金额" width="80">
          <template #default="{row}">
            <span>{{row.orderAmount}} $</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderPayAmount" label="实付金额" width="100">
          <template #default="{row}">
            <span>{{row.orderPayAmount}} ￥</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="订单状态" width="80">
          <template #default="{row}">
            <el-tag v-if="row.orderStatus === 0" type="warning">待支付</el-tag>
            <el-tag v-else-if="row.orderStatus === 1" type="success">已支付</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="订单说明"></el-table-column>
      </el-table>
    </el-tab-pane>
  </el-tabs>
  <el-dialog v-model="openRecharge" title="账户充值">
    <el-form>
      <el-form-item label="充值数额">
        <el-radio-group v-model="rechargeMoney">
          <el-radio label="1">1刀</el-radio>
          <el-radio label="5">5刀</el-radio>
          <el-radio label="10">10刀</el-radio>
          <el-radio label="20">20刀</el-radio>
          <el-radio label="50">50刀</el-radio>
          <el-radio label="100">100刀</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="自定义金额">
        <el-input type="number" v-model="rechargeMoney"></el-input>
      </el-form-item>
      <el-form-item label="实付金额">
        <el-tag>{{payValue}} ￥</el-tag>
      </el-form-item>
      <el-button @click="doRecharge" style="width: 100%" type="primary">开始充值</el-button>
    </el-form>
  </el-dialog>
</div>
</template>

<style scoped lang="less">
.link{
  color: #409EFF;
  text-decoration: none;
}
.purse{
  padding: 16px;
  .cards{
    // 横向排列
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    .box-card{
      width: 100%;
      margin-bottom: 20px;
      .card-header{
        display: flex;
        justify-content: space-between;
      }
      .tips{
        display: inline-block;
        background-color: #dbebff;
        padding: 5px 10px;
        border-radius: 20px;
      }
    }
  }
  .plus-items{
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    .el-radio{
      width: 45%;
      margin-bottom: 10px;
      .title{
        font-size: 18px;
        font-family: Source Han Sans CN-Heavy, Source Han Sans CN;
        font-weight: 800;
        color: #000235;
        line-height: 25px;
        margin-top: 8px;
      }
      .price{
        font-size: 18px;
        font-family: Source Han Sans CN-Heavy, Source Han Sans CN;
        font-weight: 800;
        color: orangered;
        line-height: 25px;
        margin-top: 8px;
      }
      .descriptions{
        font-size: 12px;
        color: gray;
        margin-top: 8px;
        line-height: 12px;
        .item{
          margin-bottom: 5px;
        }
      }
    }
  }
}
</style>