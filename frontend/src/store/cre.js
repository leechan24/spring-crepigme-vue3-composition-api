import store from './';
//import _ from 'lodash';
//import dayjs from 'dayjs';
import axios from 'axios';
import { marked } from 'marked';
import Cookies from 'js-cookie';
//import { start } from 'chromedriver';

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

// markdown을 HTML로 변환
const markdonwToHtml = markdown => {
    if (!markdown) return ''; // ★ undefined/null이면 빈 문자열 반환
    let text = marked(markdown, { render: render });
    text = text.replaceAll('&lt;', '<');
    text = text.replaceAll('&gt;', '>');
    text = text.replaceAll('&quot;', '"');
    return text;
};

// state를 함수형으로 반환
const state = () => ({
    creList: [],
    cre: [],
    mouse: [],
    mouseList: [],
    sumList: [],
    dialogHTML: [],
});

const getters = {
    GET_CRELIST : state => state.creList,
    GET_MOUSELIST : state => state.mouseList,
    GET_SUMLIST : state => state.sumList, 
    GET_DIALOG_HTML: state => state.dialogHTML,
};

const mutations = {
    SET_CRELIST :  (state, payload) => (state.creList = payload),
    SET_MOUSELIST : (state, payload) => (state.mouseList = payload),
    SET_SUMLIST : (state, payload) => (state.sumList = [...state.sumList, payload]),
    SET_DIALOG_HTML: (state, payload) => (state.dialogHTML = [...state.dialogHTML, payload]),
};

const actions = {

    // 크레 리스트 조회 후 자동으로 마크다운 HTML 생성
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


            // 각 항목에 대해 마크다운 HTML 준비
            for (const { idx, name, morph, etc } of creArray) {
                // 필요한 경우 날짜/쿠키 조건 처리 가능
                // 쿠키 조건
               //  쿠키 확인 (오늘 하루 보지 않기 설정된 경우 continue)
                const x = document.cookie; 
                if (Cookies.get(`cre-${idx}`) === "hidden") {
                    continue;
                }
                // FETCH_MARKDOWN 호출
                await dispatch('FETCH_MARKDOWN', { idx, name, morph, etc });

                 //const isDate = startDate.replaceAll('-', '') <= today && today <= endDate.replaceAll('-','');
                 //if(!isDate || !isCookie) continue;
                 
                // 조건에 맞는 항목만 마크다운 생성
                //if (isCookie) {
                //    await dispatch("FETCH_MARKDOWN", { idx, name, morph, etc });
                //}
                // FETCH_MARKDOWN 호출
                //await dispatch('FETCH_MARKDOWN', { idx, name, morph, etc });
            }

        } catch (error) {
            console.error(error);
            alert(error);
        }
    },

    // 마크다운 팝업 만들기 
    FETCH_MARKDOWN: ({commit}, { idx, name, morph, etc }) => {
        try { 
            const markdonwText = etc || ''; // ★ undefined/null이면 빈 문자열
             // 문자열 안에 \n이 들어있으면 실제 줄바꿈으로 치환
            const normalizedText = markdonwText.replace(/\\n/g, '\n');
            const htmlText = markdonwToHtml(normalizedText);
            commit('SET_DIALOG_HTML', {
                name: idx,
                idx: idx,
                html: htmlText,
            });
        } catch (error) {
            alert(error);
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
