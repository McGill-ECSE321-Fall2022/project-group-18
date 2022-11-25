<template>
  <div class="home">
    <h1>Welcome to the museum app!</h1>
    <h2>Artifacts</h2>
    <b-nav-form>
          <b-form-input v-model="filter" size="lg" class="mr-sm-2" placeholder="Search"></b-form-input>
          <b-button size="lg" class="my-2 my-sm-0" type="submit">Search</b-button>
        </b-nav-form>
    <b-list-group class="art-container">
      <b-list-item v-for="art in artifacts.filter(a => filter ? a.name.toLowerCase().includes(filter.toLowerCase()) : a)">
        <div class="card">
          <h5>{{art.name}}</h5>
        </div>
      </b-list-item>
    </b-list-group>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'hello',
  mounted() {
        axios.get(process.env.NODE_ENV === "development"
            ? 'http://localhost:8080/artifact/all' : 'production_link')
            .then(res => this.artifacts = res.data)
            .catch(e => console.log(e))
    },
  data() {
    return {
      filter: '',
      msg: 'Welcome to Your Vue.js App',
      mockArtifacts: [
        { name: "Mona Lisa" },
        { name: "The Starry Night" },
        { name: "Girl with a Pearl Earring" },
        { name: "The Last Supper" }
      ],
      artifacts: []
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
</style>
