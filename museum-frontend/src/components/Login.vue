<template>
    <div class="hello">
        <div v-if="!logged">
            <h1>Login</h1>
            <div>
                <form @submit.prevent="submit">
                    <div>
                        <label for="type">I am a</label>
                        <select v-model="type" id="type" name="type">
                            <option value="customer">Customer</option>
                            <option value="employee">Employee</option>
                            <option value="owner">Owner</option>
                        </select>
                        <div>
                            <label>Username</label>
                            <input type="text" placeholder="Username" v-model="username" />
                        </div>
                        <div>
                            <label>Password</label>
                            <input type="password" placeholder="Password" v-model="password" />
                        </div>
                    </div>
                    <button :disabled="!username || !password" type="submit">Login</button>
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
    mounted() {
        if (localStorage.getItem('uid') && localStorage.getItem('utype')) this.logged = true;
    },
    data() {
        return {
            username: '',
            password: '',
            type: 'customer',
            logged: false,
            error: false,
        }
    },
    methods: {
        submit() {
            this.error = false;
            // Call the login endpoint for the corresponding user type
            axios.post(process.env.NODE_ENV === "development" ? `http://localhost:8080/${this.type}/login` : 'production_link',
                { username: this.username, password: this.password })
                .then(res => {
                    this.logged = true
                    console.log(res)
                    //local storage used to store the user fields to ensure that the logged in user has access to the correct pages
                    //and only the correct pages
                    localStorage.setItem('uid', res.data)
                    localStorage.setItem('utype', this.type)
                    this.$router.go(0)
                })
                .catch(e => this.error = true) // Check if there was an error when loggin in (wrong credentials)
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

select {
    padding: 5px;
    border-radius: 5px;
}

.sucess {
    color: rgb(65, 198, 38);
    font-weight: bold;
}

.error {
    color: rgb(178, 24, 24);
}
</style>
