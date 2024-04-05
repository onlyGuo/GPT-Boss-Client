import{_ as T,r as n,a as l,bN as B,c as v,k as e,t as p,b as h,w as _,D as k,n as S,Q as w,i as A,o,d as f,O as N,p as P,j as G,aE as E}from"./index-C9Y2eLVK.js";const x=i=>(P("data-v-d5f315c3"),i=i(),G(),i),D={class:"install"},O={key:0,class:"install-content"},V={class:"install-title"},H={class:"install-desc"},j={class:"install-body"},K={style:{padding:"10px"}},M={class:"install-log"},Q={style:{height:"50px","text-align":"center"}},R={key:1,class:"install-content"},$=x(()=>e("div",{class:"install-title"},"ChatGPT CN Client 安装程序",-1)),q=x(()=>e("div",{class:"install-desc"},"ChatGPT CN Client 是一款基于 ChatGPT 的聊天机器人，可以用于聊天、问答、翻译、绘图、制表、情感分析等多种场景。",-1)),z={class:"install-body"},F={style:{height:"50px","text-align":"center"}},J={__name:"Install",setup(i){const c=n(""),t=n({});l.get("../install.md").then(s=>{c.value=s,l.get("/api/v1/system/manager-key").then(a=>{c.value=c.value.replace("{{managerKey}}",a)}),l.get("/api/v1/system/web-info").then(a=>{t.value=a,t.value.status===1&&g()})});const d=n(`使用须知：
1. 请确认你已经在GPT-BOSS中配置了正确的数据库。
2. 点击初始化按钮完成数据库初始化.
3. 初始化完成后，点击开始使用按钮进入聊天界面。


欢迎使用AI对话程序！我们致力于提供一个安全、友好、有益的对话环境。为了保证每位用户都能享受到最佳的体验，请遵循以下守则：

请以礼貌和尊重的态度与AI进行交流。
避免使用侮辱性、歧视性或攻击性的语言。

不要共享您或他人的个人信息，包括但不限于姓名、地址、电话号码或电子邮件地址。
不要要求AI透露其它用户的个人信息。

不要利用AI进行任何违法行为，包括传播非法内容或参与非法活动。
不要尝试破坏或干扰AI程序的正常运行。

避免发布不适当的内容，如色情、暴力或其他可能令人不适的材料。
请保持对话的文明和专业，即使在不同意见的情况下也应如此。

尽可能清晰地表达您的问题或评论，这有助于AI更准确地理解和响应。
如果AI的回答不准确，请耐心指正或重新表述您的问题。`),r=n(null),y=()=>{E().then(()=>{r.value.setScrollTop(r.value.wrapRef.scrollHeight-r.value.$el.clientHeight)})},I=()=>{l.get("/api/v1/system/web-info").then(s=>{t.value=s})},m=n(!1),b=()=>{d.value=`准备安装数据库...
`,y(),l.fetch("/api/v1/system/init-db","POST",{},(s,a)=>{if(s){d.value.includes("点击”完成“按钮")&&(m.value=!0);return}d.value+=a,y()})},g=()=>{B.replace({path:"/"})};return(s,a)=>{const C=w,u=A;return o(),v("div",D,[t.value.status===2?(o(),v("div",O,[e("div",V,"["+p(t.value.name)+"] 数据库初始化",1),e("div",H,"["+p(t.value.name)+"] 是一款基于 ChatGPT 的聊天机器人，可以用于聊天、问答、翻译、绘图、制表、情感分析等多种场景。",1),e("div",j,[e("div",K,[h(C,{height:"400px",ref_key:"scrollBar",ref:r},{default:_(()=>[e("pre",M,p(d.value),1)]),_:1},512)]),e("div",Q,[m.value?(o(),k(u,{key:1,type:"primary",onClick:g},{default:_(()=>[f("完成")]),_:1})):(o(),k(u,{key:0,type:"primary",onClick:b},{default:_(()=>[f("初始化数据库")]),_:1}))])])])):(o(),v("div",R,[$,q,e("div",z,[h(S(N),{"model-value":c.value},null,8,["model-value"]),e("div",F,[h(u,{type:"primary",onClick:I},{default:_(()=>[f("下一步")]),_:1})])])]))])}}},U=T(J,[["__scopeId","data-v-d5f315c3"]]);export{U as default};
