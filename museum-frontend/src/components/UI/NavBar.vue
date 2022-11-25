<template>
    <div>
        <b-navbar toggleable="lg" type="dark" variant="info">
            <b-navbar-brand href="/">Museum Website</b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <!-- Right aligned nav items -->
                <b-navbar-nav class="ml-auto">
                    <b-nav-item v-if="uid <= 0" href="#/login">Login</b-nav-item>
                    <b-nav-item-dropdown v-else right>
                        <!-- Using 'button-content' slot -->
                        <template #button-content>
                            <em>User</em>
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
export default {
  name: 'navbar',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      uid: localStorage.getItem('uid') || 0
    }
  },
  methods: {
    logOut () {
        localStorage.removeItem('uid');
        this.uid = 0;
    }
  }
}
</script>