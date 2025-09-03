import store from '.';
import axios from 'axios';
import { marked } from 'marked';
import Cookies from 'js-cookie';

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
    dialogHTML: [], // app.vue에서 사용하는 html 변환 결과 
    notices: [], // 공지사항 리스트
});

const getters = {
    GET_DIALOG_HTML: state => state.dialogHTML,
    GET_NOTICES: state => state.notices,

};

const mutations = {
    SET_DIALOG_HTML: (state, payload) => (state.dialogHTML = [...state.dialogHTML, payload]),
};

const actions = {

    // 공지사항 
    // 깃랩 Markdown 파일 목록 가져오기
    FETCH_NOTICES: async ({ commit }) => {
        try {
            const res = await axios.get("/api/notice/gitlab"); 
            // → Spring Boot가 GitLab raw md 가져와서 JSON으로 반환
            for (const notice of res.data) {
            const html = markdonwToHtml(notice.markdown);
            // 쿠키 체크
            if (!Cookies.get(`notice-${notice.idx}`)) {
                commit('SET_DIALOG_HTML', { idx: notice.idx, html, title: notice.title });
            }
            }
        } catch (error) {
            console.error(error);
        }
    },
};

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
};
