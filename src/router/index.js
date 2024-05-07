import PaymentPage from '@/views/PaymentPage.vue'
import RegisterPage from '@/views/RegisterPage.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/register',
      name: 'register',
      component: RegisterPage
    },
    {
      path: '/payment',
      name: 'payment',
      component: PaymentPage
    },
    {
      path: '/',
      redirect: 'register'
    }
  ]
})

export default router
