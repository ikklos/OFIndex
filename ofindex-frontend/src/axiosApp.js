//从配置文件中读取后端ip和端口
import axios from "axios";

let url = '';

async function axiosApp(){
    let app;
    await axios.get('/back-end-config.json').then((response) => {
        url = 'http://' + response.data.ip + ':' + response.data.port;
        app = axios.create({
            baseURL: url,
            timeout: 10000,
        });
            //定义请求拦截器
        app.interceptors.request.use(config => {
            const token = localStorage.getItem('Token');
            if (token) {
                config.headers.Authorization = `${token}`;
            }
            return config;
        }, error => {
            return Promise.reject(error);
        });
    }).catch((error) => {
        console.log('加载配置文件失败');
    });
    return app;
}

export default axiosApp;

