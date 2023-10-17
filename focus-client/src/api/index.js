import fetch from 'electron-fetch'
import { ElMessage } from "element-plus";
const url = 'http://127.0.0.1:8081';

// 登录
const login = async (data) => {
    return request(url + '/app/user/login', {
        method: 'post',
        body: JSON.stringify(data),
    })
}
//封装
const request = (url, option = {}) => {
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem("token")
    }
    return fetch(url, {
        ...option,
        headers: headers
    }).then(res => {
        return res.json()
            .then(data => ({
                data,
                headers: res.headers,
                status: res.status,
            }))
            .catch(error => {
                ElMessage({
                    message: '请求失败啦，请重试',
                    type: "error",
                });
                throw new Error(`json err`);
            });
    }).then(result => {
        console.log(result)
        //响应统一拦截处理
        if (result.status === 401
            || result.data.code === 401
            || result.data.code == 500) {
            ElMessage({
                message: result.data.msg,
                type: "error",
            });
            throw new Error('401');
        }
        return result.data;
    })
}
export { login };