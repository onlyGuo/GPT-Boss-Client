<script setup>

import Globel from "../../libs/globel/index.js";
</script>

<template>
<div class="default-page">
  <div class="welcome">
    欢迎来到{{Globel.site.name}}
  </div>
  <div class="desc">
    <p>在这里，您可以自由的创建独立的属于你的私有AI，并可以自由的训练你的模型，以便在在多种场景中灵活作业。您可以通过调整上下文来精确的控制您的额度消耗。</p>
  </div>
  <div class="action-title">试试下面的场景</div>
  <div class="actions">
    <div class="action">
      <div class="title">问题问答</div>
      <div class="action-desc">
        <p>您可以向AI提问任何问题，AI会给出最优解。</p>
        <p>例如：你相信杀生在任何情况下都是错误的吗？</p>
      </div>
    </div>
    <div class="action">
      <div class="title">方案制定</div>
      <div class="action-desc">
        <p>您在生活或工作中遇到需要制定方案的场景时，可以交给AI帮你完成。</p>
        <p>例如：帮我拟定一份为其一个月的节约用电实施方案表，如有需要我补充的，请告知我。</p>
      </div>
    </div>
    <div class="action">
      <div class="title">文案编写</div>
      <div class="action-desc">
        <p>它可以帮你编写任何风格的文案，您的要求越丰富越严格，它给出的回答越准确</p>
        <p>例如：请帮我写一篇关于“超级吃货铺”的小红书风格的推荐文案。</p>
      </div>
    </div>
    <div class="action">
      <div class="title">文章大纲</div>
      <div class="action-desc">
        <p>当你需要写一个长篇文章时，可以先让AI帮你创建大纲整理思路</p>
        <p>例如：帮我写一篇论文的大纲，以“生成式AI”为核心，要充分论证生成式AI对社会的发展和贡献。</p>
      </div>
    </div>
    <div class="action">
      <div class="title">论文撰写</div>
      <div class="action-desc">
        <p>您可以让AI根据已有的大纲为你精确的编写论文或文章的局部具体内容。</p>
        <p>例如：现有大纲如下：【大纲内容】，请帮我完成大纲中第n部分的内容撰写工作。</p>
      </div>
    </div>
    <div class="action">
      <div class="title">内容理解</div>
      <div class="action-desc">
        <p>它可以阅读并学习你提供的任何内容，但你需要开启足够的上下文支持。</p>
        <p>例如：请阅读下列文章，并回答我接下来的问题：【文章内容】</p>
      </div>
    </div>
    <div class="action">
      <div class="title">代码分析</div>
      <div class="action-desc">
        <p>它可以分析任何编程代码，并给出说明或修改建议。</p>
        <p>例如：请分析下列代码，告诉我这段代码实现了什么操作：【代码内容】</p>
      </div>
    </div>
    <div class="action">
      <div class="title">艺术创作</div>
      <div class="action-desc">
        <p>它可以按照您的要求进行文字艺术的创作，例如歇后语、对对联、七言律诗、现代诗歌等。</p>
        <p>例如：帮我写一篇描述红军英勇无畏精神的现代诗歌。</p>
      </div>
    </div>
    <div class="action">
      <div class="title">代码编写</div>
      <div class="action-desc">
        <p>它可以根据您的要求编写任何编程语言的代码片段，但您需要提供足够的上下文支持。</p>
        <p>例如：请帮我写出一个贪吃蛇的小游戏的完整H5代码。</p>
      </div>
    </div>
  </div>
  <div class="desc">
    点击左侧列表，选择或创建一个会话，开始您的私人AI之旅吧！
  </div>
</div>
</template>

<style scoped lang="less">
.default-page{
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  .welcome{
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 24px;
  }
  .desc{
    width: 80%;
    margin-bottom: 24px;
    font-size: 14px;
    text-align: center;
    color: #666666;
    p{
      margin-bottom: 12px;
    }
  }
  .action-title{
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 12px;
  }
  // 一块一块的，每行三列
  .actions{
    width: 80%;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    .action{
      width: 30%;
      height: 120px;
      border-radius: 8px;
      background: rgba(91, 82, 217, 0.08);
      margin-bottom: 12px;
      padding: 12px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      .title{
        font-size: 14px;
        font-weight: bold;
      }
      .action-desc{
        font-size: 12px;
        color: #666666;
      }
    }
  }
}
</style>