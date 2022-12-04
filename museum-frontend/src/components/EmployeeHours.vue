<template>
  <div class="home">
      <h1>{{ employee.firstName }} {{ employee.fastName }}</h1>
      <h1><i>{{ employee.username }}</i></h1>
      <div class="Employee Hours">
        <h3>Your Hours</h3>
          <!-- <b-nav-form>
          <b-form-input v-model="filter" size="lg" class="mr-sm-2" placeholder="Search"></b-form-input>
          <b-button size="lg" class="my-2 my-sm-0" type="submit">Search</b-button>
      </b-nav-form> -->
        <b-list-group >
          <b-list-item v-for="hours in employee.employeeHours.sort((a, b) => new Date(a.day) - new Date(b.day))">
            <div class="card">
              <h5> Date: {{ hours.date }}</h5>
            </div>
          </b-list-item>
        </b-list-group> 
      </div> 
               
  </div>


</template>

<script>
import axios from 'axios';

export default {
  mounted() {
    axios.get(process.env.NODE_ENV === "development"
      ? `http://localhost:8080/employee/${localStorage.getItem('uid')}` : 'production_link')
      .then(res => 
        this.employee =res.data)
      .catch(e => console.log(e))

    },
  data(){
    return {
        employee: '',
        hours: ''
        /*employeeHours: [
        { date: '2022-12-12', open_time: '08:00:00', close_time: '17:00:00' },
        { date: '2022-12-14', open_time: '09:30:00', close_time: '14:30:00' },
        { date: '2022-12-20', open_time: '10:00:00', close_time: '15:30:00' }
    ]*/


    }
  }
}

</script>

<style>
</style>