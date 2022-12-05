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
      path: '/buyTicket',
      name: 'BuyTicket',
      component: BuyTicket
    },
    {
      path: '/yourTickets',
      name: 'Tickets',
      component: ViewTickets
    },
    {
      path: '/createTicket',
      name: 'CreateTicket',
      component: CreateTicket
    },
    {
      path: '/managing',
      name: 'Managing',
      component: Managing
    },
    {
      path: '/scheduling',
      name: 'Scheduling',
      component: Scheduling
    },
    {
      path: '/employeeHours',
      name: 'EmployeeHours',
      component: EmployeeHours
    },
    {
      path: '/ticket',
      name: 'Ticket',
      component: Ticket
    }
  ]
})
