import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Login from '@/components/Login'
import Register from '@/components/Register'
import Profile from '@/components/Profile'
import Business from '@/components/Business'
import Donation from '@/components/Donation'
import Managing from '@/components/Managing'
import Loan from '@/components/Loan'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/profile/:id',
      name: 'Profile',
      component: Profile
    },
    {
      path: '/business',
      name: 'Business',
      component: Business
    },

    {
      path: '/donate',
      name: 'Donation',
      component: Donation
    },
    {
      path: '/managing',
      name: 'Managing',
      component: Managing
    },
    {
      path: '/loans',
      name: 'Loan',
      component: Loan
    }
  ]
})
