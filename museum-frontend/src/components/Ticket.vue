<template onload="this.getAllowedDates()">
  <b-container class="bv-example-row">
    <b-row>
      <h1>Buy a Ticket</h1>
    </b-row>

    <b-row>
      <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
        <b-col md="auto">
          <b-calendar v-model="value" locale="en-US" @context="checkDate"></b-calendar>
        </b-col>
      </b-col>
      <b-overlay :show="show" rounded="sm" >
        <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
          <h1>{{ value }}</h1>
          <div class="box1">
            <b-row>
              <p1 style="font-size: 25px;text-align-all: center">Valid From:</p1>
            </b-row>
            <b-row>
              <p2 style="font-size: 25px;text-align-all: center">8:00am to 5:00pm</p2>
            </b-row>
          </div>
          <b-row>
            <div class="box2">
              <b-form inline>
                <label class="sr-only" for="inline-form-input-name">Name</label>
                <b-input-group prepend="Name" class="p-2 mb-2 mr-sm-2 mb-sm-0">
                  <b-form-input id="inline-form-input-username" placeholder="Name on card"></b-form-input>
                </b-input-group>

                <label class="sr-only" for="inline-form-input-username">Username</label>
                <b-input-group prepend="Credit Card" class="p-2 mb-2 mr-sm-2 mb-sm-0">
                  <b-form-input id="inline-form-input-username" placeholder="Credit Card Number"></b-form-input>
                </b-input-group>

                <b-input-group prepend="CVV" class="p-1 mb-2 mr-sm-2 mb-sm-0" style="width: 130px;">
                  <b-form-input id="inline-form-input-username" placeholder="xxx"></b-form-input>
                </b-input-group>
              </b-form>
            </div>
          </b-row>
          <b-row>
            <b-col class="pb-3">
              <p3 class="shadow p-3 my-3 mx-1 bg-white rounded" style="font-weight: bold; font-size: 40px">
                ${{ price }}
              </p3>
            </b-col>
            <b-col>
              <b-button style="font-size: 40px" variant="primary" class="p-1">Checkout</b-button>
            </b-col>
          </b-row>
        </b-col>
      </b-overlay>
    </b-row>
    <b-row>
      <b-row>
        <p3 style="font-weight: bold; font-size: 40px">{{ this.allowedDates }}</p3>
      </b-row>

    </b-row>
    <b-row>
      <b-button style="font-size: 40px" variant="primary" class="p-1" @click="checkDate">REF</b-button>
    </b-row>
    <b-row>
      <b-button style="font-size: 40px" variant="primary" class="p-1" @click="getAllowedDates">REF</b-button>
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
  name: 'BuyTicket',
  mounted() {
    // get the ticket id list first
    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/ticket/all' : 'production_link')
      .then(res => {
        this.tickets = Object.keys(res.data)
      })
      .catch(e => console.log(e))
  },
  data() {
    return {
      value: '',
      price: '',
      tickets: [],
      allowedDates: [],
      show: true,
      currentTicket: '',
      newTicket: '',
      errorTicket: ''
    }
  },
  methods: {
    checkDate() {
      this.ticketNotFound = true
      for (let i = 0; i < this.allowedDates.length; i++) {
        if (String(this.value) === String(this.allowedDates[i])) {
          this.currentTicket = this.tickets[i]
          this.price = this.currentTicket.price
          this.show = false
          return 1
        }
      }
      this.show = true
    },
    getTickets() {
      axios.get('http://localhost:8080/ticket/all').then(response => {
        this.tickets = response.data
      }).catch(e => {
        this.errorTicket = e
      })
    },
    getAllowedDates() {
      this.getTickets()
      for (let i = 0; i < this.tickets.length; i++) {
        this.allowedDates[i] = String(this.tickets[i].day)
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
