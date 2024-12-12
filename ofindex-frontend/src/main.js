import ElementPlus from "element-plus";
import 'element-plus/dist/index.css';
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App);
//添加ElementPlus
app.use(ElementPlus);
app.use(router);
app.mount('#app');
