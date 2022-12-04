<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand href="/"><BIconHouseFill class="h2 mb-2" color="white" /></b-navbar-brand>
      <b-navbar-brand href="/" class="text-monospace font-weight-bolder my-2">Museum Website</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <!-- Right aligned nav items -->
        <b-navbar-nav v-if="uid <= 0" class="ml-auto">
          <b-nav-item href="#/login">Login</b-nav-item>
          <b-nav-item href="#/register">Register</b-nav-item>
        </b-navbar-nav>
        <b-navbar-nav v-else class="ml-auto">
          <b-nav-item v-if="utype === 'customer'" href="#/donate">Donate</b-nav-item>
          <b-nav-item v-if="utype === 'customer'" href="#/tickets">Tickets</b-nav-item>
          <b-nav-item v-if="utype === 'employee' || utype === 'owner'" href="#/requests">Requests</b-nav-item>
          <b-nav-item v-if="utype === 'employee'" href="#/eticket">Hours</b-nav-item>
          <b-nav-item v-if="utype === 'owner'" href="#/scheduling">Scheduling</b-nav-item>
          <b-nav-item v-if="utype === 'owner'" href="#/managing">Managing</b-nav-item>
          <b-nav-item v-if="utype === 'owner'" href="#/business">Business</b-nav-item>
          <b-nav-item v-if="utype === 'owner'" href="#/create-tickets">Create Tickets</b-nav-item>
          <b-nav-item-dropdown right>
            <!-- Using 'button-content' slot -->
            <template #button-content>
              <em><BIconPersonCircle /></em>
            </template>
            <b-dropdown-item v-bind:href="'#/profile/' + uid">Profile</b-dropdown-item>
            <b-dropdown-item v-on:click="logOut" href="#">Sign Out</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
import { BIconHouseFill, BIconPersonCircle } from 'bootstrap-vue'
export default {
  components: {
    BIconHouseFill,
    BIconPersonCircle
  },
  name: 'navbar',
  data() {
    return {
      msg: 'Welcome to Your Vue.js App',
      uid: localStorage.getItem('uid') || 0,
      utype: localStorage.getItem('utype') || ''
    }
  },
  methods: {
    logOut() {
      localStorage.removeItem('uid');
      localStorage.removeItem('utype')
      this.$router.go()
      this.uid = 0;
    }
  }
}
</script>
