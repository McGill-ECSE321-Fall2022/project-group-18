<template>
    <div id = 'employeeHours'>
        <h1>Employee Hours</h1>
    <b-calendar v-model="date" @context="onContext" locale="en-US"></b-calendar>
    <div>
        <label>Start Time</label>
        <input type="text" placeholder="Start Time" v-model="startTime" />
    </div>
    <div>
        <label>End Time </label>
        <input type="text" placeholder="End Time" v-model="endTime" />
    </div>
    </div>


</template>

<script>
export default {
  data(){
    return {
        startTime: '',
        endTime: '',
        date: ''
    }
  }
}

</script>

<style>
</style>