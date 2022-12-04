<template>
  <b-container class="bv-example-row">
    <b-row>
      <h1>Your Tickets</h1>
    </b-row>
    <b-row>
      <b-col>
        <b-row class="shadow p-3 my-3 mx-1 bg-white rounded">
          <b-col>
            <b-row>
              <h1>{{ user.customerTickets[currTicketNumber].day }}</h1>
            </b-row>
            <b-row>
              <p2 style="font-size: 25px;text-align-all: center">8:00am to 5:00pm</p2>
            </b-row>
          </b-col>
        </b-row>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-button @click="prev">
          Previous
        </b-button>
      </b-col>
      <b-col>
        <b-button @click="next">
          Next
        </b-button>
      </b-col>
    </b-row>
    <b-row>
      <div class="container">
        <div class="row">
          <div class="col text-center">
            <b-button variant="success" href="#/buyTicket">Purchase Ticket</b-button>
          </div>
        </div>
      </div>
    </b-row>


  </b-container>

</template>
<script>
import axios from 'axios'
import router from '../router'

export default {
  name: 'BuyTicket',
  mounted() {
    // get the ticket id list first
    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/customer/' + localStorage.getItem('uid') || 0 : 'production_link')
      .then(res => this.user = res.data)
      .then(console.log(this.user))
      .catch(e => console.log(e))

    console.log(this.user)
  },
  data() {
    return {
      user: {},
      uid: localStorage.getItem('uid') || 0,
      customerTickets: [],
      currTicket: {},
      currDay: '',
      currTicketNumber: 0
    }
  },
  methods: {
    prev() {
      if (this.currTicketNumber != 0) {
        this.currTicketNumber--
        this.currTicket = this.user.customerTickets[this.currTicketNumber]
      }
    },
    next() {
      if (this.currTicketNumber != (this.user.customerTickets.length - 1)) {
        this.currTicketNumber++
        this.currTicket = this.user.customerTickets[this.currTicketNumber]
      }
    }
  }
}
</script>
<style>
p1 {
  text-align: center;
}

.btn-xl {
  padding: 100px 200px;
  font-size: 40px;
  border-radius: 50px;
}
</style>
