import {createRouter,createWebHistory} from 'vue-router'
import AccountCard from "@/components/account-cards/AccountCard.vue";
import AccountCardHeader from "@/components/account-cards/AccountCardHeader.vue";
import AccountCardLogin from "@/components/account-cards/AccountCardLogin.vue";
import AccountCardRegister from "@/components/account-cards/AccountCardRegister.vue";
import AccountCardRegisterSucceed from "@/components/account-cards/AccountCardRegisterSucceed.vue";
import Index from "@/components/index/Index.vue";
import IndexExplore from "@/components/index/IndexExplore.vue";
import {Upload} from "@element-plus/icons-vue";
import IndexUploadBook from "@/components/index/IndexUploadBook.vue";
import IndexShelf from "@/components/index/IndexShelf.vue";
import IndexForum from "@/components/index/IndexForum.vue";
import BookDetailPage from "@/components/detail-pages/BookDetailPage.vue";
import PostDetailPage from "@/components/detail-pages/PostDetailPage.vue";
import MessagePage from "@/components/detail-pages/MessagePage.vue";
import ReadIndex from "@/components/reading-pages/ReadIndex.vue";

const routes = [
    {
        path: "/",
        redirect: "/account"
    },
    {
        path: "/account",
        name: "r-account",
        component: AccountCard,
        redirect: {name: "r-account-card-login"},
        children: [
            {
                path: "login",
                name: "r-account-card-login",
                component: AccountCardLogin
            },
            {
                path:"register",
                name: "r-account-card-register",
                component: AccountCardRegister
            },{
                path:"register-succeed/:AccountId",
                name: "r-account-card-register-succeed",
                component: AccountCardRegisterSucceed
            }
        ]
    },{
        path: "/index",
        name: "r-index",
        component: Index,
        redirect: "/index/explore",
        children: [
            {
                path: "explore",
                name: "r-index-explore",
                component: IndexExplore
            },{
                path:"upload-book",
                name: "r-index-upload-book",
                component: IndexUploadBook
            },{
                path:"shelf",
                name: "r-index-shelf",
                component: IndexShelf
            }, {
                path:"forum",
                name:"r-index-forum",
                component: IndexForum
            },{
                path:"detail/book-detail/:bookId",
                name:"r-index-book-detail",
                component: BookDetailPage,
            },{
                path:"detail/post-detail/:postId",
                name:"r-index-post-detail",
                component: PostDetailPage,
            },{
                path:"detail/messages",
                name:"r-index-messages",
                component: MessagePage,
            }
        ]
    },{
        path: '/reading/:bookId/:packId?',
        name: "r-reading",
        component: ReadIndex,
    }
];
const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;