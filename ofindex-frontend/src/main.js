import ElementPlus from "element-plus";
import 'element-plus/dist/index.css';
import 'element-plus/theme-chalk/base.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from "axios";
import Icon from 'view-ui-plus'
import 'view-ui-plus/dist/styles/viewuiplus.css'

//从配置文件中读取后端ip和端口
let url = '';
await axios.get('/back-end-config.json').then((response) => {
    url = 'http://' + response.data.ip + ':' + response.data.port;
}).catch((error) => {
    console.log('加载配置文件失败');
});
const axiosApp = axios.create({
    baseURL: url,
    timeout: 10000,
});
//定义请求拦截器
axiosApp.interceptors.request.use(config => {
    const token = localStorage.getItem('Token');
    if (token) {
        config.headers.Authorization = `?0`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});
const app = createApp(App);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

//添加ElementPlus
app.use(ElementPlus);
app.component(Icon);
app.use(router);
app.mount('#app');
export default axiosApp;
