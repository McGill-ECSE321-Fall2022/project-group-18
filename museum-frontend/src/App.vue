<template>
  <div id="app">
    <!-- <div>
      <img src="./assets/logo.png">
      <nav>
        <ul>
          <li>
            <a>Home</a>
          </li>
          <li>
            <a>About</a>
          </li>
          <li>
            <a href="/login">Login</a>
          </li>
        </ul>
      </nav>
    </div> -->
    <NavBar />
    <div id="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/UI/NavBar'
export default {
  name: 'app',
  components: {
    NavBar
  }
}
</script>

<style>
#app {
  font-family: 'Montserrat', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#content {
  margin-top: 60px;
}
</style>
