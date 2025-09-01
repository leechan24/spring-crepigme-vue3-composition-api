import axios from 'axios';

const interceptor = app => {
    axios.interceptors.request.use(config => {
        return config;
    })
};

export default interceptor;