import{r as k,aY as B,bL as N,aT as V,l as E,bM as P,ba as b,w as O,b as z,by as R,P as j,bN as H,bu as y,ao as M,aE as _,av as A,bO as g,br as L,aH as I,aO as Y,bP as q}from"./index-uCgfSdfU.js";function D(t){let e;const l=k(!1),n=B({...t,originalPosition:"",originalOverflow:"",visible:!1});function a(s){n.text=s}function o(){const s=n.parent,r=d.ns;if(!s.vLoadingAddClassList){let u=s.getAttribute("loading-number");u=Number.parseInt(u)-1,u?s.setAttribute("loading-number",u.toString()):(y(s,r.bm("parent","relative")),s.removeAttribute("loading-number")),y(s,r.bm("parent","hidden"))}i(),m.unmount()}function i(){var s,r;(r=(s=d.$el)==null?void 0:s.parentNode)==null||r.removeChild(d.$el)}function v(){var s;t.beforeClose&&!t.beforeClose()||(l.value=!0,clearTimeout(e),e=window.setTimeout(f,400),n.visible=!1,(s=t.closed)==null||s.call(t))}function f(){if(!l.value)return;const s=n.parent;l.value=!1,s.vLoadingAddClassList=void 0,o()}const c=E({name:"ElLoading",setup(s,{expose:r}){const{ns:u,zIndex:S}=P("loading");return r({ns:u,zIndex:S}),()=>{const C=n.spinner||n.svg,T=b("svg",{class:"circular",viewBox:n.svgViewBox?n.svgViewBox:"0 0 50 50",...C?{innerHTML:C}:{}},[b("circle",{class:"path",cx:"25",cy:"25",r:"20",fill:"none"})]),$=n.text?b("p",{class:u.b("text")},[n.text]):void 0;return b(H,{name:u.b("fade"),onAfterLeave:f},{default:O(()=>[j(z("div",{style:{backgroundColor:n.background||""},class:[u.b("mask"),n.customClass,n.fullscreen?"is-fullscreen":""]},[b("div",{class:u.b("spinner")},[T,$])]),[[R,n.visible]])])})}}}),m=N(c),d=m.mount(document.createElement("div"));return{...V(n),setText:a,removeElLoadingChild:i,close:v,handleAfterLeave:f,vm:d,get $el(){return d.$el}}}let p;const F=function(t={}){if(!M)return;const e=G(t);if(e.fullscreen&&p)return p;const l=D({...e,closed:()=>{var a;(a=e.closed)==null||a.call(e),e.fullscreen&&(p=void 0)}});K(e,e.parent,l),w(e,e.parent,l),e.parent.vLoadingAddClassList=()=>w(e,e.parent,l);let n=e.parent.getAttribute("loading-number");return n?n=`${Number.parseInt(n)+1}`:n="1",e.parent.setAttribute("loading-number",n),e.parent.appendChild(l.$el),_(()=>l.visible.value=e.visible),e.fullscreen&&(p=l),l},G=t=>{var e,l,n,a;let o;return A(t.target)?o=(e=document.querySelector(t.target))!=null?e:document.body:o=t.target||document.body,{parent:o===document.body||t.body?document.body:o,background:t.background||"",svg:t.svg||"",svgViewBox:t.svgViewBox||"",spinner:t.spinner||!1,text:t.text||"",fullscreen:o===document.body&&((l=t.fullscreen)!=null?l:!0),lock:(n=t.lock)!=null?n:!1,customClass:t.customClass||"",visible:(a=t.visible)!=null?a:!0,target:o}},K=async(t,e,l)=>{const{nextZIndex:n}=l.vm.zIndex||l.vm._.exposed.zIndex,a={};if(t.fullscreen)l.originalPosition.value=g(document.body,"position"),l.originalOverflow.value=g(document.body,"overflow"),a.zIndex=n();else if(t.parent===document.body){l.originalPosition.value=g(document.body,"position"),await _();for(const o of["top","left"]){const i=o==="top"?"scrollTop":"scrollLeft";a[o]=`${t.target.getBoundingClientRect()[o]+document.body[i]+document.documentElement[i]-Number.parseInt(g(document.body,`margin-${o}`),10)}px`}for(const o of["height","width"])a[o]=`${t.target.getBoundingClientRect()[o]}px`}else l.originalPosition.value=g(e,"position");for(const[o,i]of Object.entries(a))l.$el.style[o]=i},w=(t,e,l)=>{const n=l.vm.ns||l.vm._.exposed.ns;["absolute","fixed","sticky"].includes(l.originalPosition.value)?y(e,n.bm("parent","relative")):L(e,n.bm("parent","relative")),t.fullscreen&&t.lock?L(e,n.bm("parent","hidden")):y(e,n.bm("parent","hidden"))},x=Symbol("ElLoading"),h=(t,e)=>{var l,n,a,o;const i=e.instance,v=s=>I(e.value)?e.value[s]:void 0,f=s=>{const r=A(s)&&(i==null?void 0:i[s])||s;return r&&k(r)},c=s=>f(v(s)||t.getAttribute(`element-loading-${q(s)}`)),m=(l=v("fullscreen"))!=null?l:e.modifiers.fullscreen,d={text:c("text"),svg:c("svg"),svgViewBox:c("svgViewBox"),spinner:c("spinner"),background:c("background"),customClass:c("customClass"),fullscreen:m,target:(n=v("target"))!=null?n:m?void 0:t,body:(a=v("body"))!=null?a:e.modifiers.body,lock:(o=v("lock"))!=null?o:e.modifiers.lock};t[x]={options:d,instance:F(d)}},Z=(t,e)=>{for(const l of Object.keys(e))Y(e[l])&&(e[l].value=t[l])},Q={mounted(t,e){e.value&&h(t,e)},updated(t,e){const l=t[x];e.oldValue!==e.value&&(e.value&&!e.oldValue?h(t,e):e.value&&e.oldValue?I(e.value)&&Z(e.value,l.options):l==null||l.instance.close())},unmounted(t){var e;(e=t[x])==null||e.instance.close(),t[x]=null}};export{Q as v};
