import store from './';
//import _ from 'lodash';
//import dayjs from 'dayjs';
//import axios from 'axios';
import axios from "@/api/axios";

import { marked } from 'marked';
import Cookies from 'js-cookie';

import {
    CRE_LIST,
    CREATE_CRE,
    DELETE_CRE,
    UPDATE_CRE,
} from '@/api/cre.js';

 marked.setOptions({
    headerIds: false,
    breaks: true,
    pedantic: false,
    sanitize: false,
    smartLists: true,
    smartTypants: false,
}); 

const render = new marked.Renderer();
render.link = function(href, title, text) {
     return '<a href="' + href + '" target="_blank" title="' + text + '">'+ '</a>';
}

//const today = dayjs().format('YYYYMMDD');
const getAXIOS = () => store.getters['AXIOS'];

// state를 함수형으로 반환
const state = () => ({
    creList: [],
    cre: [],
    mouse: [],
    mouseList: [],
    sumList: [],
});

const getters = {
    GET_CRELIST : state => state.creList,
    GET_MOUSELIST : state => state.mouseList,
    GET_SUMLIST : state => state.sumList, 
};

const mutations = {
    SET_CRELIST :  (state, payload) => (state.creList = payload),
    SET_MOUSELIST : (state, payload) => (state.mouseList = payload),
    SET_SUMLIST : (state, payload) => (state.sumList = [...state.sumList, payload]),
    CLEAR_CRE_LIST : (state)  => (state.creList = []),
};

const actions = {

    FETCH_CRELIST: async({commit, dispatch}) => {
        try {
            const res = await CRE_LIST();
            if(!(res.data instanceof Object)) throw 'JSON Syntax Error';
            if(res.data.status === 'fail') throw res.data.errorMessage;

            const creArray = res.data; // 실제 배열

            // state에 리스트 반영
            commit('SET_CRELIST', creArray);

            // 오늘 날짜 (yyyyMMdd)
            const today = new Date().toISOString().slice(0, 10).replace(/-/g, "");

        } catch (error) {
            console.error(error);
            //alert(error);
        }
    },
    // 크레 추가생성 
    CREATE_CRE: async ({ commit }, cre) => {
        const res = await CREATE_CRE(cre);
        return res.data;
    },

    // 크레 삭제 
    REMOVE_CRE: async ({ commit }, creId) => {
        const res = await DELETE_CRE(creId);
        return res.data;
    },

    // 크레 수정 
    UPDATE_CRE: async ({ commit }, cre) => {
        const res = await UPDATE_CRE(cre);
        return res.data;
    },
};

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
};
