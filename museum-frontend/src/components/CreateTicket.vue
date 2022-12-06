<template>
  <!-- UI to create tickets -->
  <b-container class="bv-example-row">
    <b-row>
    <!--Title for page-->
      <h1>Create a Ticket</h1>
    </b-row>

    <b-row>
      <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
        <b-col md="auto">
        <!--Interactive Calendar-->
          <b-calendar v-model="value" @context="onContext" locale="en-US"></b-calendar>
        </b-col>
      </b-col>
      <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
        <h1 v-show="!initialShow">{{ value }}</h1>
        <div class="box1">
          <b-row>
            <!--            <h1 v-show="initialShow">Please Select a Date</h1>-->
          </b-row>
          <b-row>
            <p1 style="font-size: 25px;text-align-all: center" v-show="promptShow">{{ noDatePrompt }}</p1>
          </b-row>
          <b-row>
          <!--Ticket information in order to create ticket-->
            <b-col>
              <div class="col">
                <div class="row-xs-0 my-auto">
                  <p style="font-size: 25px;text-align-all: center" v-show="!show">Ticket Price:</p>
                </div>
              </div>
            </b-col>
            <b-col>
              <b-input-group prepend="$" class="p-1 mb-2 mr-sm-2 mb-sm-0" style="width: 115px;" v-show="!show">
                <b-form-input v-show="!show" v-model="price" id="inline-form-input-username" placeholder="Price"></b-form-input>
              </b-input-group>
            </b-col>
          </b-row>
          <b-row>
            <b-col>
            <!--create ticket button-->
              <b-button v-show="!show" style="font-size: 40px" variant="primary" class="p-1" @click="createTicket">Create</b-button>
            </b-col>
          </b-row>
        </div>
      </b-col>
    </b-row>
  </b-container>

</template>
<script>
import axios from 'axios'

function TicketDto(date, price) {
  this.date = date
  this.price = price
}

export default {
  name: 'CreateTicket',
  mounted() {
    // get the ticket id list first
    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/ticket/all' : 'production_link')
      .then(res => {
        console.log(res.data)
        this.tickets = res.data
      })
      .catch(e => console.log(e))

    this.getAllowedDates()
    console.log(this.show)
  },
  data() {
    return {
      show: true,
      promptShow: true,
      noDatePrompt: '',
      price: '',
      value: '',
      tickets: [],
      newTicket: '',
      errorTicket: '',
      date: '',
      newDate: '',
      initialShow: false,
      varD: false,
      created: false,
      allowedDates: []
    }
  },
  methods: {
  //verifies if date already has ticket created
    checkDate() {
      for (let i = 0; i < this.allowedDates.length; i++) {
        if (String(this.value) === String(this.allowedDates[i])) {

          this.show = true
          this.promptShow = true
          this.noDatePrompt = "A Ticket Already Exists For This Date"
          return 1
        }
      }
      this.show = false
      this.promptShow = false
      },
    getAllowedDates() {
      this.getTickets()
      for (let i = 0; i < this.tickets.length; i++) {
        this.allowedDates[i] = String(this.tickets[i].day)
      }
    },
    //creates ticket for selected day and price
    createTicket() {
      this.date = String(this.value);
      axios.post(process.env.NODE_ENV === "development"
          ? 'http://localhost:8080/ticket' : 'production_link',
        {
          day: this.date,
          price: this.price
        })
        .then()
        .catch(e => console.log(e))
    },
    //gets all tickets
    getTickets() {
      axios.get(process.env.NODE_ENV === "development"
        ? 'http://localhost:8080/ticket/all' : 'production_link')
        .then(res => {
          this.tickets = Object.keys(res.data)
        })
        .catch(e => console.log(e))
    },
    //refreshes on calendar input
    onContext() {
      if(this.varD){
        this.getAllowedDates()
        this.checkDate()
      }else{
        this.varD = true
      }
    }
  }
}
</script>
<style>
p1 {
  text-align: center;
}

.box1 {
  height: 100px;
  width: 500px;
}

.box2 {
  height: 200px;
  width: 500px;
}
</style>
