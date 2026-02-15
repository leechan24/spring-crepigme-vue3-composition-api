//import axios from 'axios'; 기존 axios 사용버전
import axios from "@/api/axios";
// 크레 리스트 조회 (GET)
export const CRE_LIST = async () => {
    //return await axios.get('/api/cre/list'); 기존 axios 사용버전 
    return await axios.get('/cre/list'); // 커스텀 axios는 기본 api가 붙어서 나와서 빼야한다 
}

// 크레 생성 (POST)
export const CREATE_CRE = async (cre) => {
    return await axios.post('/cre', cre);
}

// 크레 삭제 (DELETE)
export const DELETE_CRE = async (creId) => {
    return await axios.delete(`/api/cre/delete/${creId}`);
}

// 크레 수정 (PUT)
export const UPDATE_CRE = async (cre) => {
    return await axios.put('/api/cre/update', cre);
}
