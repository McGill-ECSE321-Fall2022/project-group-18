<template>
    <!-- Scheduling page -->
    <div id ='scheduling'>
        <h1>Hours</h1>
    <div>
        <b-dropdown id="dropdown-left" text="Employees" variant="primary" class="m-2">
            <b-dropdown-item v-for="employee in employees" @click="handleSelectEmployee(employee)">
              {{(employee.firstName +''+employee.lastName +''+employee.accountID)}}
            </b-dropdown-item>
        </b-dropdown>
    </div>
    <div>
        <b-container class="bv-example-row">
        <b-row>
            <b-col>Select Schedule Date
            <div>
                <b-calendar v-model="date" @context="onContext" locale="en-US"></b-calendar>
            </div>
            </b-col>
            <b-col>Set Employee Starting Time
            <div id="Emloyee-hour-start-hour">
                <!--element which is going to render the TextBox-->
                <input v-model ="startHour"
                    oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                    type="number"
                    maxlength = "2"
                    placeholder="Starting hour (hh)"
                />
            </div>
            <div id="Emloyee-hour-start-minute">
                <!--element which is going to render the TextBox-->
                <input v-model ="startMin"
                    oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                    type="number"
                    maxlength = "2"
                    placeholder="Starting minutes (mm)"
                />
            </div>
            </b-col>
            <b-col>Set Employee Ending Time
            <div id="Emloyee-hour-end-hour">
                <!--element which is going to render the TextBox-->
                <input v-model ="endHour"
                    oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                    type="number"
                    maxlength = "2"
                    placeholder="Ending hour (hh)"
                />
            </div>
            <div id="Emloyee-hour-end-minute">
                <!--element which is going to render the TextBox-->
                <input v-model ="endMin"
                    oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                    type="number"
                    maxlength = "2"
                    placeholder="Ending minutes (mm)"
                />
            </div>
            </b-col>
        </b-row>
    </b-container>
    </div>
    <div id="create-employee-hour-btn">
      <!-- Disable the button until all required inputs are filled -->
      <b-button :disabled="!date|| !startHour || !startMin || !endHour || !endMin" type="submit"
      @click="createEmployeeHour(date, startHour, startMin, endHour, endMin)">
        Create Employee Hour
      </b-button>
    </div>
    <div id="view-employee-hours">
      <b-table striped hover :items="employeeHours"> Employee Hours </b-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data(){
    return {
        employees: [],
        date: '',
        startHour: '',
        startMin: '',
        endHour: '',
        endMin: '',
        allowedDates: [],
        employeeHours: [],

        /*emp:[
            {name:"Employee1"},
            {name: "Employee2"},
            {name: "Employee3"}
        ]*/
    }
  },
  mounted(){
    axios.get('http://localhost:8080/employeeHour/all')
    .then(response => {
      this.employeeHours = response.data;
    })
    .catch(error =>{
      console.log(error)
    })

    axios.get(process.env.NODE_ENV === "development"
    ? 'http://localhost:8080/employee/all' : 'production_link')
      .then(res => {
        this.employees = res.data
      })
      .catch(e => console.log(e))
  },



  methods: {
    onContext(ctx) {
      this.context = ctx
    },
    handleSelectEmployee(employee) {
      console.log(employee)
      this.selectedEmployee = employee
    },
    getAllowedDates() {
      for(i=0;i<this.employeeHours.length;i++){
        this.allowedDates[i] = this.employeeHour[i].getDay()
      }
    },

   /* getselectedEmployee(){
      for(let i = 0; i < this.employees.length; i++){
         if(this.employees[i].get == this.selectedEmployee){

         }
    }*/

    createEmployeeHour(date, startHour, startMin, endHour, endMin) {
      console.log("CREATING EMPLOYEE HOUR", date, startHour, startMin, endHour, endMin)
      if (startHour <= 23 && startHour >= 0 && endHour <= 23 && endHour >= 0 && startMin <= 59 && startMin >= 0 && endMin <= 59 && endMin >= 0 && (startHour < endHour || (startHour == endHour && startMin < endMin))) {
        axios.post('http://localhost:8080/employeeHour', {
          day: date,
          startTime: startHour + ":" + startMin + ":" + "00",
          endTime: endHour + ":" + endMin + ":" + "00"
        })
        .then(res => {
          const updatedEmployee = {...this.selectedEmployee, employeeHours: this.selectedEmployee.employeeHours ?
            [...this.selectedEmployee.employeeHours, res.data]
            : [res.data]
          }

          axios.post('http://localhost:8080/employee/update/' + this.selectedEmployee.accountID, updatedEmployee)
          .then(res => console.log(res.data))
        })
        .catch(error => {
          console.log(error)
        })
      }else{
        console.log("Invalid employee hour parameters")
      }
    }

  }
}
</script>
<style>
</style>
