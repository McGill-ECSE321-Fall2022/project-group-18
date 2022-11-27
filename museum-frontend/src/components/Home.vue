<template>
  <div class="home">
    <h1>Welcome to the museum app!</h1>
    <h2>Artifacts</h2>
    <b-nav-form>
      <b-form-input v-model="filter" size="lg" class="mr-sm-2" placeholder="Search"></b-form-input>
      <div>
        <label class="ml-lg">Loanable: </label>
        <input type="checkbox" v-model="loanableFilter">
      </div>
      <div>
        <label>Price range: </label>
        <div class="row mx-xl-4">
          <div class="">
            <label>From: </label>
            <input min="0" v-model="fromPrice" type="number">
          </div>
          <div class="">
            <label>To: </label>
            <input v-model="toPrice" type="number">
          </div>
        </div>
      </div>
    </b-nav-form>
    <!-- <b-list-group class="art-container">
      <b-list-item v-for="art in artifacts.filter(a => filter ? a.name.toLowerCase().includes(filter.toLowerCase()) : a)">
        <div class="card">
          <h5>{{art.name}}</h5>
        </div>
      </b-list-item>
    </b-list-group> -->
    <h5 class="m-lg-4 text-danger" v-if="loanError">There was an error when processing your loan request. (One of the
      artifacts you requested might already be loaned)</h5>
    <h5 class="m-lg-4 text-danger" v-if="updateArtifactError">There was an error when updating the Artifact</h5>
    <div class="container">
      <div class="grid-container">
        <div v-for="art in filteredArtifacts">
          <div class="card">
            <input :disabled="selectedArtifacts.length >= 5 && !selectedArtifacts.includes(art.artID)"
              v-if="utype === 'customer'" :value="art.artID" id="art.artID" v-model="selectedArtifacts"
              @change="handleSelect($event)" class="mr-auto" type="checkbox">
            <h5>{{ art.name }}</h5>
            <h6>Type: {{ art.type }}</h6>
            <div v-if="utype === 'employee'">
              <div>
                <label>Loanable: </label>
                <input type="checkbox" v-model="art.loanable">
              </div>
              <div>
                <label>Loaned: </label>
                <input type="checkbox" v-model="art.loaned">
              </div>
            </div>
            <h6 v-if="!utype || utype === 'customer'">Loan fee: {{ art.loanFee }}</h6>
            <div v-else>
              <label>Loan fee: </label>
              <input min="0" type="number" v-model="art.loanFee">
            </div>
            <div v-if="utype === 'employee' || utype === 'owner'">
              <label>Room:</label>
              <input type="number" min="1" max="11" @change="handleArtifactToRoom"
                v-model.number="artifactToRoom[art.artID]">
            </div>
            <button v-if="utype === 'employee' || utype === 'owner'" @click="(e) => handleUpdateArtifact(e, art)"
              class="px-2 py-2 w-25 align-self-end rounded-lg bg-white">Edit</button>
          </div>
        </div>
      </div>
      <div v-if="selectedArtifacts.length > 0">
        <h4>Selected artifacts ({{ selectedArtifacts.length }} / 5)</h4>
        <button @click="handleLoan" class="rounded-lg bg-white px-4 py-2">
          <h4>Loan</h4>
        </button>
        <div class="art-container">
          <div v-for="art in selectedArtifacts">
            <div class="card">
              <h5>{{ artifacts.find(a => a.artID === art).name }}</h5>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'hello',
  mounted() {
    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/artifact/all' : 'production_link')
      .then(res => {
        this.artifacts = res.data
      })
      .catch(e => console.log(e))

    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/room/all/artifacts' : 'production_link')
      .then(res => {
        this.initArtifactToRoom = { ...res.data };
        this.artifactToRoom = res.data;
      })
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
      artifacts: [],
      filteredArtifacts: [],
      loanableFilter: false,
      fromPrice: 0,
      toPrice: 100,
      utype: localStorage.utype,
      selectedArtifacts: [],
      loanError: false,
      updateArtifactError: false,
      initArtifactToRoom: {},
      artifactToRoom: {},
    }
  },
  methods: {
    handleSelect: function (e) {
      // console.log(e.target.value)
      // console.log(this.selectedArtifacts)
    },
    handleLoan: function (e) {
      this.loanError = false;
      let artifactIDStr = "?artifactIDList="
      for (let i = 0; i < this.selectedArtifacts.length; i++) {
        artifactIDStr = artifactIDStr + this.selectedArtifacts[i];
        if (i == this.selectedArtifacts.length - 1) continue
        artifactIDStr = artifactIDStr + ','
      }
      console.log(artifactIDStr)
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan${artifactIDStr}` : 'production_link')
        .catch(e => this.loanError = true)
        .then(this.selectedArtifacts = [])
    },
    handleUpdateArtifact: function (e, art) {
      this.updateArtifactError = false;
      axios.post(process.env.NODE_ENV === "development" ? `http://localhost:8080/artifact/update/${art.artID}` : 'production_link', {
        "artID": art.artID,
        "name": art.name,
        "type": art.type,
        "loanable": art.loanable,
        "loaned": art.loaned,
        "loanFee": art.loanFee
      })
        .catch(e => this.updateArtifactError = true)
        .then(res => console.log(res))

      // Check if room number was changed.
      if (this.artifactToRoom[art.artID] !== this.initArtifactToRoom[art.artID]) {
        let srcRoomID = '?srcRoomID=' + this.initArtifactToRoom[art.artID]
        let destRoomID = '&destRoomID=' + this.artifactToRoom[art.artID]
        let artifactID = '&artifactID=' + art.artID
        let transferParam = srcRoomID + destRoomID + artifactID
        axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/room/artifacts/move${transferParam}` : 'production_link')
          .catch(e => this.updateArtifactError = true)
          .then(this.initArtifactToRoom = {...this.artifactToRoom})
      }
    },
    handleArtifactToRoom: function (e) {
      console.log('init', this.initArtifactToRoom)
      console.log('new', this.artifactToRoom)
    }
  },
  watch: {
    filter: function (filter, prevFilter) {
      this.filteredArtifacts = this.artifacts.filter(a => filter ? a.name.toLowerCase().includes(filter.toLowerCase()) : a)
    },
    artifacts: function (artifacts, prevArtifacts) {
      this.filteredArtifacts = artifacts
    },
    loanableFilter: function (val, prevVal) {
      this.filteredArtifacts = this.artifacts.filter(a => val === true ? a.loanable === true : a)
    },
    fromPrice: function (val) {
      if (val >= this.toPrice) this.fromPrice = this.toPrice
      this.filteredArtifacts = this.artifacts.filter(a => a.loanFee >= val)
    },
    toPrice: function (val) {
      if (val <= this.fromPrice) this.toPrice = this.fromPrice
      this.filteredArtifacts = this.artifacts.filter(a => a.loanFee <= val)
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
  max-width: 100%;
  margin: auto;
}

.grid-container {
  display: grid;
  grid-template-columns: auto auto auto;
  margin: auto;
  padding: 10px;
}

.card {
  background-color: rgb(255, 255, 255);
  color: rgb(0, 0, 0);
  margin: 10px;
  padding: 10px;
  border-radius: 8px;
  border-color: black;
}

.container {
  width: 150%;
  display: flex;
  flex-direction: row;
}
</style>
