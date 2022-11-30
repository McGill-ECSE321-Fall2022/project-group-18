import Vue from 'vue'
import Router from 'vue-router'
import Hello from '../components/Hello'
import Home from "../components/Home";
import Ticket from "../components/Ticket";
import YourTickets from "../components/YourTickets";
import CreateTicket from "../components/CreateTicket";

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
      path: '/BuyTicket',
      name: 'Ticket',
      component: Ticket
    },
    {
      path: '/YourTickets',
      name: 'YourTickets',
      component: YourTickets
    },
    {
      path: '/CreateTicket',
      name: 'CreateTicket',
      component: CreateTicket
    }
  ]
})
