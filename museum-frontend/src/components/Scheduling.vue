<template>
    <div id ='scheduling'>
        <h1>Hours</h1>
    <div>
        <b-dropdown id="dropdown-left" text="Employee" variant="primary" class="m-2">
            <b-dropdown-item href="#">Employee</b-dropdown-item>
            <b-dropdown-item href="#">Employee1 </b-dropdown-item>
            <b-dropdown-item href="#">Employee2 </b-dropdown-item>
        </b-dropdown>
    </div>
    <div>
        <div>
            <label>Start Time</label>
            <input type="text" placeholder="Start Time" v-model="startTime" />
        </div>
        <div>
            <label>End Time </label>
            <input type="text" placeholder="End Time" v-model="endTime" />
        </div>
        <button :disabled="!startTime || !endTime" type="submit">Create</button>
    </div>

    <b-calendar v-model="date" @context="onContext" locale="en-US"></b-calendar>

    </div>

 


</template>

<script>
export default {
  data(){
    return {
        employee:[
            {name:"Employee1"},
            {name: "Employee2"},
            {name: "Employee3"}
        ],
        startTime: '',
        endTime: '',
        date: ''
    }
  }
}
</script>
<style>
</style>