<script setup>
import {Close, Delete} from "@element-plus/icons-vue";
import {ref} from "vue";
import api from "../../libs/api/index.js";
import {ElMessage} from "element-plus";
import globel from "../../libs/globel/index.js";

const props = defineProps({
  post: {
    type: Object,
    required: true,
    default: {
      id: '1',
      content: "内容",
      commentsList: [],
      comments: 0,
      createTime: "2022-02-02 02:02:02",
      like: false,
      likes: 0,
      userId: 0,
      userNicker: 0,
      images: '[]',
      userAvatar: undefined,
      imagesArray: [],
      userType: 0,  // 0 == 普通， 1 = 管理员, 2 = 作者
    }
  },
  listComment: {
    type: Boolean,
    default: false,
  },
  min: {
    type: Boolean,
    default: false,
  }
})
props.post.imagesArray = JSON.parse(props.post.images)
const displaySrc = ref('')
if (props.listComment){
  api.get('/api/v1/post/' + props.post.id + '/comment?page=1&size=100').then(res => {
    props.post.commentsList = res.data
  })
}
const doLike = () => {
  props.post.like = !props.post.like
  if(props.post.like){
    props.post.likes++
  }else{
    props.post.likes--
  }
  api.post('/api/v1/post/like', {
    id: props.post.id,
    like: props.post.like,
  }).then(res => {
    console.log(res)
  }).catch((err) => {
    ElMessage.error(err.message)
    props.post.like = !props.post.like
    if(props.post.like){
      props.post.likes++
    }else{
      props.post.likes--
    }
  })
}
const doLikeComment = (comment) => {
  comment.like = !comment.like
  if(comment.like){
    comment.likes++
  }else{
    comment.likes--
  }
  api.post('/api/v1/post/' + props.post.id + '/comment/like', {
    id: comment.id,
    like: comment.like,
  }).then(res => {
    console.log(res)
  }).catch((err) => {
    ElMessage.error(err.message)
    comment.like = !comment.like
    if(comment.like){
      comment.likes++
    }else{
      comment.likes--
    }
  })
}
const deletePost = () => {
  api.post('/api/v1/post/delete/' + props.post.id).then(res => {
    ElMessage.success('删除成功')
    props.post.isDelete = true;
  }).catch(err => {
    ElMessage.error(err.message)
  })
}
const openReply = (comment) => {
  comment.reply = !comment.reply;
  replyContent.value = ''
}
const replyContent = ref('')
const doReplyComment = (comment) => {
  const content = {
    postId: props.post.id,
    content: replyContent.value,
    replyUserId: comment.userId,
    replyUserNicker: comment.userNicker,
  }
  api.post('/api/v1/post/' + props.post.id + '/comment', content).then(res => {
    ElMessage.success('回复成功')
    comment.reply = false;
    replyContent.value = ''
    props.post.commentsList.push(res)
  }).catch(err => {
    ElMessage.error(err.message)
  })
}

const deleteComment = (comment) => {
  api.post('/api/v1/post/' + props.post.id + '/comment/delete/' + comment.id).then(res => {
    ElMessage.success('删除成功')
    props.post.commentsList = props.post.commentsList.filter(item => item.id !== comment.id)
  }).catch(err => {
    ElMessage.error(err.message)
  })
}
const replyPost = ref(false)
const onReplyPost = () => {
  replyPost.value = !replyPost.value
  replyContent.value = ''
}
const doReplyPost = () => {
  const content = {
    postId: props.post.id,
    content: replyContent.value,
  }
  api.post('/api/v1/post/' + props.post.id + '/comment', content).then(res => {
    ElMessage.success('回复成功')
    replyContent.value = ''
    replyPost.value = false
    props.post.commentsList.push(res)
  }).catch(err => {
    ElMessage.error(err.message)
  })
}
</script>

<template>
<div class="post" v-if="!props.post.isDelete" :style="min ? 'padding: 0' : ''">
  <div class="left">
    <div class="avatar">
      <img :src="'/api/v1/file/display/' + props.post.userAvatar" v-if="props.post.userAvatar" />
      <img src="../../assets/icons/default-avatar.svg" v-else-if="props.post.userNicker && props.post.userId !== 0" />
      <img src="../../assets/logo.svg" v-else />
    </div>
  </div>
  <div class="right">
    <div class="header">
      <div class="nicker" :style="min? 'font-size: 13px': ''">
        <el-tag v-if="props.post.userType === 2" :style="min? 'font-size: 12px': ''">本站作者</el-tag>
        {{props.post.userNicker}}
      </div>
      <div class="time" :style="min? 'font-size: 12px': ''">{{props.post.createTime}}</div>
    </div>
    <div class="content">
      <div>{{props.post.content}}</div>
      <div class="images">
        <img :src="'/api/v1/file/display/' + src" v-for="src in props.post.imagesArray" :key="src" @click="displaySrc = src" />
        <div class="image-display" v-if="displaySrc" @click="displaySrc = ''">
          <div class="close" @click="displaySrc = ''"><close></close></div>
          <img :src="'/api/v1/file/display/' + displaySrc">
        </div>
      </div>
    </div>
    <div class="tool-box" :style="min? 'margin-top:6px;margin-bottom:6px' : ''">
      <div class="tool-item" :class="{active: props.post.like}" @click="doLike" :style="min? 'font-size: 12px': ''">
        <img src="../../assets/icons/icon_like.svg" v-if="!props.post.like" />
        <img src="../../assets/icons/icon_like-hl.svg" v-else/>
        {{props.post.likes ? props.post.likes : '赞'}}
      </div>
      <div class="tool-item" @click="onReplyPost" >
        <img src="../../assets/icons/icon_comment.svg" /> {{props.post.comments ? props.post.comments : '评论'}}
      </div>
      <div class="tool-item" v-if="globel.currentUser && globel.currentUser.id === 1" @click="deletePost">
        删除
      </div>
    </div>
    <div class="comment-box" v-if="props.post.commentsList.length > 0 || replyPost" :style="min? 'padding: 8px; margin-bottom: 10px': ''">
      <div class="comment" v-for="comment in props.post.commentsList">
        <div class="comment-left">
          <div class="avatar">
            <img :src="'/api/v1/file/display/' + comment.userAvatar" v-if="comment.userAvatar" />
            <img src="../../assets/icons/default-avatar.svg" v-else-if="comment.userNicker && comment.userId !== 0" />
            <img src="../../assets/logo.svg" v-else />
          </div>
        </div>
        <div class="comment-right">
          <div class="header">
            <div class="nicker" :style="min? 'font-size: 12px': ''">
              <el-tag v-if="comment.userType === 2">本站作者</el-tag>
              {{comment.userNicker}}
              <span v-if="comment.replyUserNicker"><img src="../../assets/icons/sanjiao.svg" style="vertical-align: middle" /> {{comment.replyUserNicker}}</span>
            </div>
            <div class="time" :style="min? 'font-size: 12px': ''">{{comment.createTime}}</div>
          </div>
          <div class="content" :style="min? 'font-size: 12px': ''">
            {{ comment.content }}
          </div>
          <div class="tool-box" :style="min? 'font-size: 12px': ''">
            <div class="tool-item" @click="doLikeComment(comment)" :class="{active: comment.like}">
              <img src="../../assets/icons/icon_like.svg" v-if="!comment.like" />
              <img src="../../assets/icons/icon_like-hl.svg" v-else/>
              {{comment.likes ? comment.likes : '赞'}}
            </div>
            <div class="tool-item" @click="openReply(comment)">
              <img src="../../assets/icons/icon_comment.svg" /> {{comment.reply ? '取消' : '回复'}}
            </div>
            <div class="tool-item" @click="deleteComment(comment)" v-if="globel.currentUser && (globel.currentUser.id === 1 || globel.currentUser.id === comment.userId)">
              删除
            </div>
          </div>
          <div class="comment--reply-input-box" v-if="comment.reply">
            <el-input type="text" :placeholder="'回复 ' + comment.userNicker" v-model="replyContent">
              <template #append>
                <el-button type="primary" @click="doReplyComment(comment)">回复</el-button>
              </template>
            </el-input>
          </div>
        </div>
      </div>
      <div class="comment--reply-input-box" v-if="replyPost">
        <el-input type="text" placeholder="回复楼主" v-model="replyContent">
          <template #append>
            <el-button type="primary" @click="doReplyPost">回复</el-button>
          </template>
        </el-input>
      </div>

    </div>
  </div>
</div>
</template>

<style scoped lang="less">
.post{
  padding: 24px;
  .left{
    width: 56px;
    display: inline-block;
    vertical-align: top;
    .avatar{
      width: 40px;
      height: 40px;
      border-radius: 16px 16px 16px 16px;
      overflow: hidden;
      img{
        width: 100%;
        height: 100%;
      }
    }
  }
  .right{
    width: calc(100% - 56px);
    display: inline-block;
    vertical-align: top;
    .header{
      display: flex;
      justify-content: start;
      align-items: center;
      margin-bottom: 8px;
      .nicker{
        font-size: 14px;
        font-weight: bold;
        margin-right: 16px;
      }
      .time{
        font-size: 14px;
        font-weight: normal;
        color: rgba(0,2,53,0.45);
      }
    }
    .content{
      font-size: 14px;
      font-weight: 400;
      color: #333333;
      line-height: 26px;
      // 保留文本格式
      white-space: pre-wrap;

      .images{
        margin-top: 16px;
        display: flex;
        flex-wrap: wrap;
        img{
          width: 100px;
          height: 100px;
          margin-right: 16px;
          margin-bottom: 16px;
          object-fit: cover;
          cursor: pointer;
          box-shadow: 0 0 2px 0 rgba(0,0,0,0.16);
        }

        .image-display{
          background-color: rgba(0,0,0,0.3);
          position: fixed;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
          .close{
            position: absolute;
            top: 16px;
            right: 16px;
            width: 40px;
            height: 40px;
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;
            background-color: rgba(0,0,0,0.3);
            border-radius: 50%;
            svg{
              width: 20px;
              height: 20px;
              color: white;
            }
          }
          img{
            width: calc(100% - 32px);
            height: calc(100% - 32px);
            object-fit: contain;
            cursor: default;
          }
        }
      }
    }
    .tool-box{
      margin-top: 16px;
      display: flex;
      justify-content: end;
      align-items: center;
      font-size: 14px;
      font-family: Source Han Sans CN-Regular, Source Han Sans CN;
      font-weight: 400;
      color: #8A8BA5;
      margin-bottom: 16px;
      .tool-item{
        display: flex;
        justify-content: center;
        align-items: center;
        margin-left: 16px;
        cursor: pointer;
        &.active{
          color: #596FFF;
        }
        img{
          width: 16px;
          height: 16px;
          margin-right: 4px;
        }
      }
    }

    .comment-box{
      background: #F9F9FB;
      padding: 24px;
      .comment-left{
        width: 32px;
        display: inline-block;
        vertical-align: top;
        margin-right: 12px;
        .avatar{
          width: 32px;
          height: 32px;
          border-radius: 16px 16px 16px 16px;
          overflow: hidden;
          img{
            width: 100%;
            height: 100%;
          }
        }
      }
      .time{
        // 右对其
        margin-left: auto;
      }
      .comment-right{
        width: calc(100% - 44px);
        display: inline-block;
        vertical-align: top;

        .comment--reply-input-box{
          margin-bottom: 16px;
        }
      }
    }
  }
}
</style>