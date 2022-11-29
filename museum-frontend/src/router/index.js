import Vue from 'vue'
import Router from 'vue-router'
import Hello from '../components/Hello'
import Home from "../components/Home";
import Ticket from "../components/Ticket";

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
      path: '/Ticket',
      name: 'Ticket',
      component: Ticket
    }
  ]
})
