import NotFoundPage from '@/views/NotFoundPage.vue'
import PaymentPage from '@/views/PaymentPage.vue'
import RegisterPage from '@/views/RegisterPage.vue'
import ValidatePage from '@/views/ValidatePage.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/:notfound(.*)', component: NotFoundPage },
    {
      path: '/register',
      name: 'register',
      component: RegisterPage
    },
    {
      path: '/validate',
      name: 'validate',
      component: ValidatePage
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
