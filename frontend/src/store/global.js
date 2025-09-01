import _ from 'lodash';

/* global.js는 첨에 APP.VUE 띄울떄 가져올 데이터들을 저장하는 공간이다 */

// API 호출 하기 위해 적음 
import {
    CRE_LIST,
    DELETE_CRE,
    UPDATE_CRE,
} from '@/api/cre.js';

const getState = () => {
    return {
        cre: [],
        mouse: [],
    };
};

const state = getState();

const getters = {
    GET_CRELIST : state => state.creList,
    GET_MOUSELIST : state => state.mouseList,
    GET_SUMLIST : state => state.sumList, 
    GET_DIALOG_HTML: state => state.dialogHTML,
};

const mutations = {
    SET_CRELIST :  (state, payload) => (state.creList = payload),
    SET_MOUSELIST : (state, payload) => (state.mouseList = payload),
    SET_SUMLIST : (state, payload) => (state.sumList = [...state.sumList, payload]), // state 기존값과 새로운 값을 합침
    SET_DIALOG_HTML: (state, payload) => (state.dialogHTML = [...state.dialogHTML, payload]), // markdown 용 
    
    SET_STATE(state) {
        Object.assign(state, getState());
    },
};

const actions = {

    // 크레 리스트 조회 
    FETCH_CRELIST: async({commit, dispatch}, cre) => {
        const axios = getAXIOS();
        const {response} = await CRE_LIST(cre);
        if(!(response instanceof Object)) {
            throw 'JSON Sytax Error';
        } else if(response.status === 'fail') {
            throw response.errorMessage;
        }
        commit('SET_CRELIST', response);
    },
};

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
}