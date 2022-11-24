import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Login from '@/components/Login'
import Register from '@/components/Register'
import Owner from '@/components/Owner'
import Employee from '@/components/Employee'
import Customer from '@/components/Customer'


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
      path: '/owner',
      name: 'Owner',
      component: Owner
    },
    {
      path: '/employee',
      name: 'Employee',
      component: Employee
    },
    {
      path: '/customer',
      name: 'Customer',
      component: Customer
    }
  ]
})
