import {reactive} from "vue";

export default reactive({
    openLogin: false,
    currentUser: undefined,
    token: null,
    isLogin: false,
    site: {
        logo: '',
        name: '',
        icp: '',
        status: 0,
        modules: [],
        fun: {
            enable: true,
            enableEmail:false,
            enableGuest:false,
            enableRegister:false
        }
    },
    noReadMessageCount: 0,
    inFrame: false,
    chatModules: [],
    plugins: [],
    clearDefaultRoomMessage: function () {},
    sysSetting: {
        name: 'MyChatGPT',
        logo: '',
        hostServiceUrl: '',
        ipc: '',
        payAppId: '',
        payAppSecret: '',
        payOrderCreateUrl: 'https://newpc.icoding.ink/api/internal/order',
        openaiApiToken: '',
        openaiApiUrl: '',
    }
})