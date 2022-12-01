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
        <b-container class="bv-example-row">
        <b-row>
            <b-col>Select Schedule Date
            <div>
                <b-calendar v-model="date" @context="onContext" locale="en-US"></b-calendar>
            </div>
            </b-col>
            <b-col>Sent Emloyee Starting Time
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
            <b-col>Set Emloyee Ending Time
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
export default {
  data(){
    return {
        date: '',
        startHour: '',
        startMin: '',
        endHour: '',
        endMin: '',
        employeeHours: [],

        employee:[
            {name:"Employee1"},
            {name: "Employee2"},
            {name: "Employee3"}
        ]
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
  },

  methods: {
    /*
    async createEmployeeHour(date, openHour, openMin, closeHour, closeMin) {
      console.log("CREATING EMPLOYEE HOUR")
      if (date && day <= 31 && day >= 1 && openHour <= 23 && openHour >= 0 && closeHour <= 23 && closeHour >= 0 && openMin <= 59 && openMin >= 0 && closeMin <= 59 && closeMin >= 0 && (openHour < closeHour || (openHour == closeHour && openMin < closeMin))) {
        await axios.post('http://localhost:8080/businessHour', {
          day: "20" + year + "-" + month + "-" + day,
          openTime: openHour + ":" + openMin + ":" + "00",
          closeTime: closeHour + ":" + closeMin + ":" + "00"
        })
        .catch(error => {
          console.log(error)
        })
      }else{
        console.log("Invalid employee hour parameters")
      }
    }*/

  }
}
</script>
<style>
</style>