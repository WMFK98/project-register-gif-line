import './assets/main.css'
import Vue3Toasity from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
const pinia = createPinia()
const app = createApp(App).use(Vue3Toasity, {
  autoClose: 3000
})

app.use(router)
app.use(pinia)
app.mount('#app')
