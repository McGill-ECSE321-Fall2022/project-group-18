<template>
    <div class="hello">
        <div v-if="!registered">

            <h1>Register</h1>
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
                        <div>
                            <label>First Name</label>
                            <input type="text" placeholder="First Name" v-model="firstName" />
                        </div>
                        <div>
                            <label>Last Name</label>
                            <input type="text" placeholder="Last Name" v-model="lastName" />
                        </div>
                    </div>
                    <button type="submit">Submit</button>
                </form>
            </div>
        </div>
        <div v-else>
            <h1>Registered successfully!</h1>
            <h2><a href="#/login">Login</a></h2>
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
            firstName: '',
            lastName: '',
            registered: false,
        }
    },
    methods: {
        submit() {
            axios.post(process.env.NODE_ENV === "development"
                ? 'http://localhost:8080/customer' : 'production_link',
                {
                    username: this.username,
                    password: this.password,
                    firstName: this.firstName,
                    lastName: this.lastName
                })
                .then(res => console.log(res))
                .then(this.registered = true)
                .catch(e => console.log(e))
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
</style>
  