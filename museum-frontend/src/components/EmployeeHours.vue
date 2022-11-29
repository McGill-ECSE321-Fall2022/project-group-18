<template>
    <div id = 'employeeHours'>
        <h1>Search Employee Hours</h1>
    <div>
        <label>From</label>
        <input type="text" placeholder="Date" v-model="date" />
    </div>
    <div>
        <label>To</label>
        <input type="text" placeholder="Date" v-model="date2" />
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