import { createRouter, createWebHashHistory } from 'vue-router'
import Index from "../Index.vue";
import ChatView from "../views/chat/Index.vue";
import UserCenter from "../views/user/Index.vue";
const routes = [
    {
        path: '/',
        component: Index,
        name: 'Index',
        redirect: '/chat',
        children: [
            {
                path: '/chat',
                component: ChatView,
                name: 'chatView',
                children: [
                    {
                        path: ':id',
                        component: () => import('../views/chat/ChatView.vue'),
                        meta: {
                            noAuth: true
                        }
                    },
                ]
            },
            {
                path: '/user',
                component: UserCenter,
                children: [
                    {
                        path: 'user-center',
                        component: () => import('../views/user/UserCenter.vue'),
                    },
                    {
                        path: 'notice',
                        component: () => import('../views/user/Notice.vue'),
                    },
                    {
                        path: 'purse',
                        component: () => import('../views/user/Purse.vue'),
                    }
                ]
            },
        ]
    },
    {
        path: '/install',
        component: () => import('../views/Install.vue'),
    }

]
const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router