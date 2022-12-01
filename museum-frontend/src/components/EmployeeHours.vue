<template>
    <div id = 'employeeHours'>
        <h1>Search Employee Hours</h1>
    <div>
    <b-nav-form>
      <BIconSearch class="h4 mb-2 mr-2" />
      <b-form-input v-model="filter" size="lg" class="mr-sm-2" placeholder="From"></b-form-input>
    </b-nav-form>
    </div>
    <div>
    <b-nav-form>
      <BIconSearch class="h4 mb-2 mr-2" />
      <b-form-input v-model="filter" size="lg" class="mr-sm-2" placeholder="To"></b-form-input>
    </b-nav-form>
    </div>
    <div id="view-employee-hours">
      <b-table striped hover :items="employeeHours"> Employee Hours </b-table>
    </div>
    </div>


</template>

<script>
export default {
  data(){
    return {
        filter: '',
        startTime: '',
        endTime: '',
        date: '',
        date1: '',
        employeeHours: [
        { date: '2022-12-12', open_time: '08:00:00', close_time: '17:00:00' },
        { date: '2022-12-14', open_time: '09:30:00', close_time: '14:30:00' },
        { date: '2022-12-20', open_time: '10:00:00', close_time: '15:30:00' }
    ]
    }
  }
}

</script>

<style>
</style>