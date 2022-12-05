<template>
  <div class="home">
    <h1 class="text-uppercase font-weight-bold">Welcome to the museum app!</h1>
    <h2>Artifacts</h2>
    <b-nav-form>
      <BIconSearch class="h4 mb-2 mr-2" />
      <b-form-input v-model="filter" size="lg" class="mr-sm-2" placeholder="Search"></b-form-input>
      <div class="d-inline-flex">
        <label>Loanable: </label>
        <input type="checkbox" v-model="loanableFilter">
      </div>
      <div class="mx-3">
        <label class="">Price range: </label>
        <div class="w-25">

          <div class="d-inline-flex">
            <label>From: </label>
            <input class="rounded-lg" min="0" v-model="fromPrice" type="number">
          </div>
          <div class="d-inline-flex">
            <label>To: </label>
            <input class="rounded-lg" v-model="toPrice" type="number">
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
            <input
              :disabled="((selectedArtifacts.length >= 5 && !selectedArtifacts.includes(art.artID)) || (art.loaned || !art.loanable))"
              v-if="utype === 'customer'" :value="art.artID" id="art.artID" v-model="selectedArtifacts"
              @change="handleSelect($event)" class="mr-auto" type="checkbox">
            <div class="image-container">
              <img v-bind:src="art.image" />
            </div>
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
              <input :disabled="!initArtifactToRoom[art.artID]" type="number" min="1" max="11"
                @change="handleArtifactToRoom" v-model.number="artifactToRoom[art.artID]">
            </div>
            <button v-if="utype === 'employee' || utype === 'owner'" @click="(e) => handleUpdateArtifact(e, art)"
              class="px-2 py-2 w-25 align-self-end rounded-lg bg-white">Edit</button>
          </div>
        </div>
      </div>
      <div v-if="selectedArtifacts.length > 0">
        <div class="row">
          <h4>Selected artifacts ({{ selectedArtifacts.length }} / 5)</h4>
          <BIconCartFill class="h3 mb-2" v-if="selectedArtifacts.length < 5" />
          <BIconCartCheckFill color="green" class="h3 mb-2" v-else />
        </div>
        <b-button v-b-modal.modal-1
          class="text-uppercase font-weight-bold bg-success rounded-lg px-4 py-2 justify-content-center">
          Loan
        </b-button>
        <div class="art-container">
          <div v-for="art in selectedArtifacts">
            <div class="card">
              <h5>{{ artifacts.find(a => a.artID === art).name }}</h5>
            </div>
          </div>
        </div>
      </div>
    </div>
    <b-modal id="modal-1" title="Checkout">
      <h3 class="my-4">Checking out</h3>
      <div>
        <p>Selected artifacts: </p>
        <div class="card" v-for="art in selectedArtifacts">
          <p><b>Name:</b> {{ artifacts.find(a => a.artID === art).name }}</p>
          <p><b>Fee:</b> {{ artifacts.find(a => a.artID === art).loanFee }}</p>
        </div>
        <div class="card">
          <h5><b>Total: </b>{{ totalLoanFee }}</h5>
        </div>
      </div>
      <b-button @click="handleLoan" class="bg-success">
        <BIconCartCheckFill color="white" class=""/>
        Checkout
      </b-button>
    </b-modal>
  </div>
</template>

<script>
import axios from 'axios';
import { BForm, BIconCartCheckFill, BIconCartFill, BIconSearch } from 'bootstrap-vue';

export default {
  components: {
    BIconCartCheckFill,
    BIconCartFill,
    BIconSearch,
    BForm
  },
  name: 'hello',
  mounted() {
    const getRandomInt = function (max) {
      return Math.floor(Math.random() * max);
    }

    const mockImagesPaintings = [
      'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Mona_Lisa%2C_by_Leonardo_da_Vinci%2C_from_C2RMF_retouched.jpg/1200px-Mona_Lisa%2C_by_Leonardo_da_Vinci%2C_from_C2RMF_retouched.jpg',
      'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg/1200px-Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg',
      'https://www.neh.gov/sites/default/files/2022-09/Fall_2022_web-images_Picasso_32.jpg',
      'https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/1665_Girl_with_a_Pearl_Earring.jpg/800px-1665_Girl_with_a_Pearl_Earring.jpg'

    ]
    const mockImagesSculptures = [
      'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/%27David%27_by_Michelangelo_Fir_JBU005_denoised.jpg/800px-%27David%27_by_Michelangelo_Fir_JBU005_denoised.jpg',
      'https://artincontext.org/wp-content/uploads/2021/01/Michelangelo-Pieta-848x530.jpg'
    ]

    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/artifact/all' : 'production_link')
      .then(res => {
        this.artifacts = res.data.map(art => {
          console.log(art.type)
          // Setting random images to each artifact
          return {
            ...art,
            image: art.type == 'Painting' ?
              mockImagesPaintings[getRandomInt(mockImagesPaintings.length)] :
              mockImagesSculptures[getRandomInt(mockImagesSculptures.length)]
          }
        })
      })
      .then(
        console.log(this.artifacts)
      )
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
      mockArtifacts: [ // Used for debugging purposes
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
      uid: localStorage.uid,
      selectedArtifacts: [],
      loanError: false,
      updateArtifactError: false,
      initArtifactToRoom: {},
      artifactToRoom: {},
      totalLoanFee: 0,
    }
  },
  methods: {
    handleSelect: function (e) {
      // console.log(e.target.value)
      // console.log(this.selectedArtifacts)
    },
    handleLoan: async function (e) {
      let newLoanID = -1;
      this.loanError = false;
      let artifactIDStr = "?artifactIDList="
      let loanCustomerStr = "?loanID="
      for (let i = 0; i < this.selectedArtifacts.length; i++) {
        artifactIDStr = artifactIDStr + this.selectedArtifacts[i];
        if (i == this.selectedArtifacts.length - 1) continue
        artifactIDStr = artifactIDStr + ','
      }
      console.log(artifactIDStr)
      // Creating a loan object
      await axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan${artifactIDStr}` : 'production_link')
        .catch(e => this.loanError = true)
        .then(res => {
          console.log("loan ID: " + res.data)
          newLoanID = res.data;
        })
      loanCustomerStr = loanCustomerStr + newLoanID
      console.log("loan add to customer:" + loanCustomerStr)
      // Then adding the loan to the user using the same id string
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/customer/${this.uid}/loans/add${loanCustomerStr}` : 'production_link')
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
        .catch(e => this.updateArtifactError = true) // This will display an error message on the page
        .then(res => console.log(res))

      // Check if room number was changed.
      if (this.artifactToRoom[art.artID] !== this.initArtifactToRoom[art.artID]) {
        let srcRoomID = '?srcRoomID=' + this.initArtifactToRoom[art.artID]
        let destRoomID = '&destRoomID=' + this.artifactToRoom[art.artID]
        let artifactID = '&artifactID=' + art.artID
        let transferParam = srcRoomID + destRoomID + artifactID
        axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/room/artifacts/move${transferParam}` : 'production_link')
          .catch(e => this.updateArtifactError = true)
          .then(this.initArtifactToRoom = { ...this.artifactToRoom })
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
      this.filteredArtifacts = this.artifacts.filter(a => val === true ? a.loanable === true && a.loaned === false : a)
    },
    fromPrice: function (val) {
      if (val >= this.toPrice) this.fromPrice = this.toPrice
      this.filteredArtifacts = this.artifacts.filter(a => a.loanFee >= val)
    },
    toPrice: function (val) {
      if (val <= this.fromPrice) this.toPrice = this.fromPrice
      this.filteredArtifacts = this.artifacts.filter(a => a.loanFee <= val)
    },
    selectedArtifacts: function (selectedArtifacts) {
      // Calculating total loan fee when we update the selected artifacts
      let sum = 0;
      const selectedArtifactsObjects = this.artifacts.filter(art => selectedArtifacts.includes(art.artID))
      console.log(selectedArtifactsObjects)
      for (let i = 0; i < selectedArtifactsObjects.length; i++) {
        sum += selectedArtifactsObjects[i].loanFee;
      }
      this.totalLoanFee = sum
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
  column-gap: 30px;
}

.card {
  background-color: rgb(255, 255, 255);
  color: rgb(0, 0, 0);
  margin: 10px;
  padding: 10px;
  border-radius: 8px;
  border-color: black;
  transition: 0.2s;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.12),
    0 2px 2px rgba(0, 0, 0, 0.12),
    0 4px 4px rgba(0, 0, 0, 0.12),
    0 8px 8px rgba(0, 0, 0, 0.12),
    0 16px 16px rgba(0, 0, 0, 0.12);
}

.card:hover {
  transform: scale(1.1);
}

button {
  transition: 0.2s;
}

button:hover {
  transform: scale(1.1);
}

.container {
  width: 150%;
  display: flex;
  flex-direction: row;
}

.image-container {
  width: 250px;
  height: 250px;
  margin: auto;
}

img {
  object-fit: cover;
  width: 100%;
  height: 100%;
  border-radius: 10px;
}
</style>
