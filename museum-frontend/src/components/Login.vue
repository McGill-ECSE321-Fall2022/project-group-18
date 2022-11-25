<template>
    <div class="hello">
        <div v-if="!logged">
            <h1>Login</h1>
            <div>
                <form @submit.prevent="submit">
                    <div>
                        <div>
                            <label>Username</label>
                            <input type="text" placeholder="Username" v-model="username" />
                        </div>
                        <div>
                            <label>Password</label>
                            <input type="password" placeholder="Password" v-model="password" />
                        </div>
                    </div>
                    <button type="submit">Submit</button>
                </form>
                <h5 class="error" v-if="error">Wrong username/password!</h5>
                <h6>You do not have an account? <a href="#/register">Register</a></h6>
            </div>
        </div>
        <div v-else>
            <h1 class="success">You are logged in!</h1>
        </div>
    </div>
</template>
  
<script>
import axios from 'axios'

export default {
    name: 'login',
    data() {
        return {
            username: '',
            password: '',
            logged: false,
            error: false,
        }
    },
    methods: {
        submit() {
            this.error = false;
            axios.post(process.env.NODE_ENV === "development" ? 'http://localhost:8080/customer/login' : 'production_link',
                { username: this.username, password: this.password })
                .then(res => {
                    this.logged = true
                    console.log(res)
                    localStorage.setItem('uid', res.data)
                    this.$router.push('/')
                })
                .catch(e => this.error = true)
        }
    }
}
</script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
    font-weight: normal;
}

ul {
    list-style-type: none;
    padding: 0;
}

li {
    display: inline-block;
    margin: 0 10px;
}

a {
    color: #42b983;
}

input {
    border-radius: 7px;
    padding: 5px;
    margin: 5px;
}

button {
    border-radius: 7px;
    padding: 5px;
}

.sucess {
    color: rgb(65, 198, 38);
    font-weight: bold;
}

.error {
    color: rgb(178, 24, 24);
}
</style>
  