import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    port: 5173,
    /* proxy: {
      // /cre, /mouse 로 시작하는 요청은 Spring Boot 서버로 전달
      // '/api': {
      '/cre': {
        target: 'http://localhost:8082', // Spring Boot 서버 포트
        changeOrigin: true,
      },
      '/mouse': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      },
      '/menu': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      },
      '/auth': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      },
    }, */
     proxy: {
      '/api': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      },
    },
    //historyApiFallback: true // 없는 라우트는 index.html로 fallback
  },
  build: {
    outDir: '../src/main/resources/static', // Vue 결과물 → Spring Boot 정적 리소스
    emptyOutDir: true,        // 빌드 전에 static 비우기 [자동으로 static 폴더 비우고 새로 빌드]
  } ,
})
