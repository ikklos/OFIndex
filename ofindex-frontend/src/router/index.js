import {createRouter,createWebHistory} from 'vue-router'
import AccountCard from "@/components/account-cards/AccountCard.vue";
import AccountCardHeader from "@/components/account-cards/AccountCardHeader.vue";
import AccountCardLogin from "@/components/account-cards/AccountCardLogin.vue";

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
            }
        ]
    }
];
const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;