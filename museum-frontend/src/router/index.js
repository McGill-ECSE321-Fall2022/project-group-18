import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Login from '@/components/Login'
import Register from '@/components/Register'
import Profile from '@/components/Profile'
import Business from '@/components/Business'
import Donation from '@/components/Donation'
import Managing from '@/components/Managing'
import BuyTicket from "@/components/BuyTicket";
import CreateTicket from "@/components/CreateTicket";
import ViewTickets from "@/components/ViewTickets";
import Scheduling from '@/components/Scheduling'
import EmployeeHours from '@/components/EmployeeHours'
import Ticket from '@/components/Ticket'
import Loan from '@/components/Loan'

Vue.use(Router)

const hasPermissions = (to, from, next, utype, expectedTypes) => {
  if (!expectedTypes.includes(utype) || localStorage.getItem("utype") === undefined) {
    next('/');
  } else next()
}

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
      component: Profile,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["owner", 'customer', 'employee'])
      }
    },
    {
      path: '/business',
      name: 'Business',
      component: Business,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["owner"])
      }
    },

    {
      path: '/donate',
      name: 'Donation',
      component: Donation,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["customer"])
      }
    },
    {
      path: '/buyTicket',
      name: 'BuyTicket',
      component: BuyTicket,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["customer"])
      }
    },
    {
      path: '/yourTickets',
      name: 'Tickets',
      component: ViewTickets,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["customer"])
      }
    },
    {
      path: '/createTicket',
      name: 'CreateTicket',
      component: CreateTicket,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["owner"])
      }
    },
    {
      path: '/managing',
      name: 'Managing',
      component: Managing,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["owner"])
      }
    },
    {

      path: '/scheduling',
      name: 'Scheduling',
      component: Scheduling,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["owner"])
      }
    },
    {
      path: '/employeeHours',
      name: 'EmployeeHours',
      component: EmployeeHours,
      beforeEnter(to, from, next) {
          hasPermissions(to, from, next, localStorage.getItem('utype'), ["employee", "owner"])
      }
    },
    {
      path: '/ticket',
      name: 'Ticket',
      component: Ticket,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ['customer'])
      }
    },
    {
      path: '/loans',
      name: 'Loan',
      component: Loan,
      beforeEnter(to, from, next) {
        hasPermissions(to, from, next, localStorage.getItem('utype'), ["employee", "owner"])
      }
    },
    {
      path: '/:pathMatch(.*)*',
      beforeEnter(to, from, next) {
        next('/')
      }
    },
  ]
})
