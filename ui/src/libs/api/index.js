import axios from "axios";
import {ElMessage, ElMessageBox} from "element-plus";
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'
import globel from "../globel/index.js";
import router from "../../router/index.js";
import Globel from "../globel/index.js";


axios.defaults.timeout = 10000
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'

axios.interceptors.request.use(
    config => {
        let token = localStorage.getItem('token')
        if (token){
            config.headers['Authorization'] = 'Bearer ' + token
        }
        return config
    }
)
axios.interceptors.response.use(
    response => {
        if (response.status === 200) {
            return Promise.resolve(response.data)
        } else {
            console.log(response.data.message)
            return Promise.reject(response.data)
        }
    },
    // 服务器状态码不是200的情况
    error => {
        if (error.response){
            if (error.response.status) {
                // error.response.data = JSON.parse(error.response.data)
                if (error.response.status === 401) {
                    // 跳到登录页
                    // globel.openLogin = true
                    globel.isLogin = false;
                    globel.currentUser = undefined;
                    const msg = error.response.data.message;
                    // ElMessage.error(msg);
                    if(!router.currentRoute.value.meta.noAuth){
                        router.replace({path: '/'})
                    }
                    return Promise.reject(error.response.data)
                } else if (error.response.status === 400) {
                    const msg = error.response.data.message;
                    // Notification.error({
                    //     title: '提示',
                    //     content: msg,
                    // })
                    ElMessage.error(msg);
                } else if (error.response.status === 403){
                    console.error('您无权访问该内容')
                } else if (error.response.status === 406){
                    router.replace({path: '/install'})
                } else {
                    console.error('网络错误，请稍后再试')
                }
                return Promise.reject(error.response.data)
            }
        }
    }
)
axios.fetch = (url, method, sendMsg, call) => {
    const then = fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Authorization': 'Bearer ' + Globel.token
        },
        body: JSON.stringify(sendMsg)
    }).then(response => {
            const reader = response.body.getReader();
            const decoder = new TextDecoder('utf-8');
            return reader.read().then(function processText({ done, value }) {
                if (done) {
                    return;
                }
                call(done, decoder.decode(value), response.headers);
                return reader.read().then(processText);
            }).finally(() => {
                return call(true, '')
            });
        })
}
export default axios
