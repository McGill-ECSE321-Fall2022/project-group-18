<template>
  <div id ='business'>
    <h1> Add new business hour </h1>
    <div id ='business-hour-input'>
    <b-container class="bv-example-row">
      <b-row>
        <b-col>Select Business Hour Date
          <div id = 'business-hour-year'>
            <!--element which is going to render the TextBox-->
            <input v-model ="year"
                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                   type="number"
                   maxlength = "2"
                   placeholder="Enter Year (yy)"
            />
          </div>
          <div id='business-hour-month'>
            <!--element which is going to render the TextBox-->
            <input v-model ="month"
                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                   type="number"
                   maxlength = "2"
                   placeholder="Enter Month (mm)"
            />
          </div>
          <div id="business-hour-day">
            <!--element which is going to render the TextBox-->
            <input v-model ="day"
                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                   type="number"
                   maxlength = "2"
                   placeholder="Enter Day (dd)"
            />
          </div>
        </b-col>
        <b-col>Select Business Hour Opening Time
          <div id="business-hour-open-hour">
            <!--element which is going to render the TextBox-->
            <input v-model ="openHour"
                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                   type="number"
                   maxlength = "2"
                   placeholder="Opening hour (hh)"
            />
          </div>
          <div id="business-hour-open-minute">
            <!--element which is going to render the TextBox-->
            <input v-model ="openMin"
                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                   type="number"
                   maxlength = "2"
                   placeholder="Opening minutes (mm)"
            />
          </div>
        </b-col>
        <b-col>Select Business Hour Closing Time
          <div id="business-hour-close-hour">
            <!--element which is going to render the TextBox-->
            <input v-model ="closeHour"
                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                   type="number"
                   maxlength = "2"
                   placeholder="Closing hour (hh)"
            />
          </div>
          <div id="business-hour-close-minute">
            <!--element which is going to render the TextBox-->
            <input v-model ="closeMin"
                   oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength)"
                   type="number"
                   maxlength = "2"
                   placeholder="Closing minutes (mm)"
            />
          </div>
        </b-col>
      </b-row>
    </b-container>
    </div>
    <div id="create-business-hour-btn">
      <b-button :disabled="!year || !month || !day || !openHour || !openMin || !closeHour || !closeMin" type="submit"
      @click="createBusinessHour(year, month, day, openHour, openMin, closeHour, closeMin)">
        Create Business Hour
      </b-button>
    </div>
    <div id="view-business-hours">
      <b-table striped hover :items="businessHours"> Business Hours </b-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      year: '',
      month: '',
      day: '',
      openHour: '',
      openMin: '',
      closeHour: '',
      closeMin: '',
      businessHours: []
    }
  },
  mounted() {
    axios.get('http://localhost:8080/businessHour/all')
    .then(response => {
      this.businessHours = response.data;
    })
    .catch(error =>{
      console.log(error)
    })
  },
  methods: {
    /*
     create a business hour for the business
     constraints: year >= 22
     month: [1,12]
     day: [1,31] (no checks for smaller months)
     openHour, closeHour: [0, 23]
     openMin, closeMin: [0, 59]
     opening time must be before closing time
     */
    async createBusinessHour(year, month, day, openHour, openMin, closeHour, closeMin) {
      console.log("CREATING BUSINESS HOUR")
      if (year >= 22 && month <= 12 && month >= 1 && day <= 31 && day >= 1 && openHour <= 23 && openHour >= 0 && closeHour <= 23 && closeHour >= 0 && openMin <= 59 && openMin >= 0 && closeMin <= 59 && closeMin >= 0 && (openHour < closeHour || (openHour == closeHour && openMin < closeMin))) {
        await axios.post('http://localhost:8080/businessHour', {
          day: "20" + year + "-" + month + "-" + day,
          openTime: openHour + ":" + openMin + ":" + "00",
          closeTime: closeHour + ":" + closeMin + ":" + "00"
        })
        .catch(error => {
          console.log(error)
        })
      }else{
        console.log("Invalid business hour parameters")
      }
      //reload the window to make the changes displayed on the page
      window.location.reload();
    }
  }
}
</script>

<style scoped>

</style>
