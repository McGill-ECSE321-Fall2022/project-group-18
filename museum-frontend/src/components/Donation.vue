<template>
  <div id ='donation'>
  <h1> Add new donation </h1>
  <div>
<!--    Input field for the name of the artifact-->
    <label>Name</label>
    <input type="text" placeholder="Name Donated Artifact" v-model="nameDonatedArtifact"/>
  </div>
<!--Radio buttons for the type of the artifact-->
    <form>
      <input type="radio" id="sculpture" name="type" value="Sculpture" v-model="typeSculptureDonated">
      <label for="sculpture">Sculpture</label><br>
      <input type="radio" id="painting" name="type" value="Painting" v-model="typePaintingDonated">
      <label for="painting">Painting</label>
    </form>

    <!-- Only allow the button to be pressed when all fields are entered -->
    <button :disabled="!nameDonatedArtifact || (!typePaintingDonated && !typeSculptureDonated)"
    @click="createDonation(nameDonatedArtifact,typeSculptureDonated,typePaintingDonated)">Donate</button>

  </div>

</template>
<script>

import axios from 'axios'

export default {
  data(){
    return {
      //Create the parameters
      nameDonatedArtifact: '',
      typeSculptureDonated: '',
      typePaintingDonated: '',
      donatedArtifacts : []
    }
  },
  //Create the donation with the parameters passed with a JSON format by calling the good backend POST method
  methods: {
    createDonation(nameDonatedArtifact, typeSculptureDonated, typePaintingDonated){
      if(typeSculptureDonated != ''){
        axios.post('http://localhost:8080/donation', JSON.stringify([{name: nameDonatedArtifact, type: "Sculpture", loanable: false, loaned: false, loanFee: 0}]),{headers:{"Content-Type":"application/json"}}).catch(error => console.log(error))
      }
      else{
        axios.post('http://localhost:8080/donation', JSON.stringify([{name: nameDonatedArtifact, type: "Painting", loanable: false, loaned: false, loanFee: 0}]), {headers:{"Content-Type":"application/json"}}).catch(error => console.log(error))

      }
    }
  }
}
</script>
<style>
</style>
