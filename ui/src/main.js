import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from "./router/index.js";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/es/components/notification/style/css'
import directives from "./utils/resizeObServer/index.js"



const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(router)
app.use(directives)
app.mount('#app')
