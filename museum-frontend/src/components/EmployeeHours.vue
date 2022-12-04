<template>
    <div id = 'employeeHours'>
      <h1>Search Employee Hours</h1>
    <div>
    <div>
      <label>From: </label>
      <input min="0" v-model="fromDate" type="number">
    </div>
    <div>
      <label>To: </label>
      <input v-model="toDate" type="number">
    </div>
    </div>
    <div id="view-employee-hours">
      <b-table striped hover :items="filter"> Employee Hours </b-table>
    </div>
    </div>


</template>

<script>

export default {
  mounted() {
    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/employeeHour/all' : 'production_link')
      .then(res => {
        this.employeeHours = res.data
      })
      .catch(e => console.log(e))

    },
  data(){
    return {
        employeeHours: [],
        filteredHours: [],
        startTime: '',
        endTime: '',
        date: '',
        date1: '',
        /*employeeHours: [
        { date: '2022-12-12', open_time: '08:00:00', close_time: '17:00:00' },
        { date: '2022-12-14', open_time: '09:30:00', close_time: '14:30:00' },
        { date: '2022-12-20', open_time: '10:00:00', close_time: '15:30:00' }
    ]*/


    }
  },

watch:{
  filter: function (filter, prevFilter) {
      this.filteredHours = this.employeeHours.filter(a => filter ? a.name.toLowerCase().includes(filter.toLowerCase()) : a)
  },
  employeeHours: function (employeeHours, prevemployeeHours) {
    this.filteredHours = employeeHours
  },
  fromDate: function (val) {
    if (val >= this.toDate) this.fromDate = this.toDate
    this.filteredArtifacts = this.employeeHours.filter(a => a.loanFee >= val)
  },
  toDate: function (val) {
    if (val <= this.fromDate) this.toDate = this.fromDate
    this.filteredHours = this.employeeHours.filter(a => a.loanFee <= val)
  },
  
}
}

</script>

<style>
</style>