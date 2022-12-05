<template>
  <!-- Profile page -->
    <div class="home">
        <h1>{{ user.firstName }} {{ user.lastName }}</h1>
        <h1><i>{{ user.username }}</i></h1>
        <div class="tickets">
            <h3>Your Tickets</h3>
            <!-- <b-nav-form>
            <b-form-input v-model="filter" size="lg" class="mr-sm-2" placeholder="Search"></b-form-input>
            <b-button size="lg" class="my-2 my-sm-0" type="submit">Search</b-button>
        </b-nav-form> -->
            <b-list-group class="art-container">
                <b-list-item v-for="ticket in user.customerTickets.sort((a, b) => new Date(a.day) - new Date(b.day))">
                    <div class="card">
                        <h5>Date: {{ ticket.day }}</h5>
                        <h6>Price: {{ ticket.price }}</h6>
                    </div>
                </b-list-item>
            </b-list-group>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import router from '../router';

export default {
    mounted() {
        axios.get(process.env.NODE_ENV === "development"
            ? 'http://localhost:8080/customer/' + this.$route.params.id : 'production_link')
            .then(res => this.user = res.data)
            .catch(e => console.log(e))
    },
    name: 'profile',
    data() {
        return {
            filter: '',
            msg: 'Welcome to Your Vue.js App',
            id: 0,
            user: {},
            mockArtifacts: [
                { name: "Mona Lisa" },
                { name: "The Starry Night" },
                { name: "Girl with a Pearl Earring" },
                { name: "The Last Supper" }
            ]
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

.art-container {
    max-width: 15%;
    margin: auto;
}

.card {
    background-color: rgb(255, 255, 255);
    color: rgb(0, 0, 0);
    margin: 10px;
    padding: 10px;
    border-radius: 8px;
    border-color: black;
}

.tickets {
    margin: 20px;
}
</style>
