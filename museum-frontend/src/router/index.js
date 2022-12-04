import Vue from 'vue'
import Router from 'vue-router'
import Hello from '../components/Hello'
import Home from "../components/Home";
import Ticket from "../components/Ticket";
import YourTickets from "../components/YourTickets";
import CreateTicket from "../components/CreateTicket";
import Login from "../components/Login";
import Register from "../components/Register";
import Profile from "../components/Profile";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/Home',
      name: 'Home',
      component: Home
    },
    {
      path: '/buyTicket',
      name: 'Ticket',
      component: Ticket
    },
    {
      path: '/tickets',
      name: 'tickets',
      component: YourTickets
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
      path: '/eticket',
      name: 'eticket',
      component: CreateTicket
    }
  ]
})
