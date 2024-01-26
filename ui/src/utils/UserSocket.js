import Globel from "../libs/globel/index.js";
import {reactive} from "vue";
import globel from "../libs/globel/index.js";
import api from "../libs/api/index.js";

const UserSocket = {
    websocket: null,
    timer: null,
    reconnecting: false,
    start(){
        let start = 'ws';
        if (location.protocol === 'https:') {
            start = 'wss';
        }
        this.websocket = new WebSocket(start + "://" + location.host + "/api/v1/user-session");
        this.websocket.onopen = this.onOpen;
        this.websocket.onclose = this.onClose;
        this.websocket.onmessage = this.onMessage;
        this.websocket.onerror = this.onError;
    },
    onOpen(){
        if (UserSocket.timer){
            clearInterval(UserSocket.timer);
        }
        if (Globel.isLogin){
            UserSocket.websocket.send('login:' + Globel.token);
        }
    },
    onClose(){
        if (UserSocket.timer){
            clearInterval(UserSocket.timer);
        }
        UserSocket.timer = null;
        // 5秒后重连
        if (!UserSocket.reconnecting){
            UserSocket.reconnecting = true;
            setTimeout(() => {
                UserSocket.start();
                UserSocket.reconnecting = false;
            }, 5000);
        }
    },
    onMessage(event){
        const msg = event.data;
        if (msg === 'login:fail'){
            UserSocket.websocket.close();
        }else if (msg === 'login:success') {
            Globel.isLogin = true;
            UserSocket.timer = setInterval(() => {
                UserSocket.websocket.send("ping");
            }, 10000)
        }else if (msg === 'logout'){
            Globel.isLogin = false;
            Globel.token = '';
            Globel.currentUser = undefined
            localStorage.removeItem('token');
            UserSocket.websocket.close();
            location.href = location.protocol + "//" + location.host;
        }else if (msg === 'notice'){
            if (globel.isLogin){
                api.get('/api/v1/system-message/no-read-count').then(r => {
                    globel.noReadMessageCount = r.count;
                })
            }
        }
    },
    onError(event){
    },
    login(){
        if(this.websocket && this.websocket.readyState === WebSocket.OPEN){
            this.websocket.send('login:' + Globel.token);
        }
    }
}

export default reactive(UserSocket);