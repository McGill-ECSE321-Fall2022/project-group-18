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
      <b-button :disabled="!year || !month || !day || !openHour || !openMin || !closeHour || !closeMin" type="submit">
        Create Business Hour
      </b-button>
    </div>
    <div id="view-business-hours">
      <b-table striped hover :items="businessHours"> Business Hours </b-table>
    </div>
  </div>
</template>

<script>
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

      businessHours: [
        { date: '2022-10-10', open_time: '08:00:00', close_time: '13:00:00' },
        { date: '2022-11-19', open_time: '09:00:00', close_time: '14:00:00' },
        { date: '2022-12-04', open_time: '10:00:00', close_time: '15:00:00' }
      ]
    }
  }
}
</script>

<style scoped>

</style>
