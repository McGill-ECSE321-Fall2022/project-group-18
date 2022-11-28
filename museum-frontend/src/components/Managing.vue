<template>
<div>
  <div class="myDiv">
    <h1>Delete employee</h1>
      <b-nav-form>
        <BIconSearch class="h4 mb-2 mr-2" />
        <b-form-input v-model="filter" size="lg" class="mr-sm-2" placeholder="Search"></b-form-input>
      </b-nav-form>
  </div>

  <div class="hire">
      <h1>Hire Employee</h1>
      <label>Employee Username</label>
      <input type="text" placeholder="Username" v-model="usernameEmployee"/><br>
      <label>Employee Password</label>
      <input type="text" placeholder="Password" v-model="passwordEmployee"/><br>
      <label>Employee First Name</label>
      <input type="text" placeholder="First Name" v-model="firstNameEmployee"/><br>
      <label>Employee Last Name</label>
      <input type="text" placeholder="Last Name" v-model="lastNameEmployee"/><br>

      <button :disabled="!usernameEmployee || !passwordEmployee || !firstNameEmployee || !lastNameEmployee">Create Employee</button>

  </div>
</div>
</template>

<script>
export default {
  data(){
    return {
      usernameEmployee: '',
      passwordEmployee: '',
      firstNameEmployee: '',
      lastNameEmployee: '',
    }
  }
}

</script>

<style scoped>
.myDiv{
  left: 300px;
  position: absolute;
}
.hire{
  left: 800px;
  position: absolute;
}
</style>
