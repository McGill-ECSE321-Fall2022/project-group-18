<template>
  <div id ='donation'>
  <h1> Add new donation </h1>
  <div>
    <label>Name</label>
    <input type="text" placeholder="Name Donated Artifact" v-model="nameDonatedArtifact"/>
  </div>

    <form>
      <input type="radio" id="sculpture" name="type" value="Sculpture" v-model="typeSculptureDonated">
      <label for="sculpture">Sculpture</label><br>
      <input type="radio" id="painting" name="type" value="Painting" v-model="typePaintingDonated">
      <label for="painting">Painting</label>
    </form>

    <button :disabled="!nameDonatedArtifact || (!typePaintingDonated && !typeSculptureDonated)" >Donate</button>

  </div>

</template>
<script>
export default {
  data(){
    return {
      nameDonatedArtifact: '',
      typeSculptureDonated: '',
      typePaintingDonated: '',

    }
  }
}
</script>
<style>
</style>
