<template>
  <!-- Ticket viewing page -->
  <b-container class="bv-example-row">
    <b-row>
    <!--Title for page-->
      <h1>Your Tickets</h1>
    </b-row>
    <b-row>
      <b-col>
        <b-row class="shadow p-3 my-3 mx-1 bg-white rounded">
          <b-col>
            <b-row>
            <!--date for currently viewed ticket-->
              <h1>{{ user.customerTickets[currTicketNumber].day }}</h1>
            </b-row>
            <b-row>
            </b-row>
          </b-col>
        </b-row>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
      <!--button to click for prev ticket in list-->
        <b-button @click="prev">
          Previous
        </b-button>
      </b-col>
      <b-col>
        <b-button @click="next">
              <!--button to click for nextticket in list-->
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

    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/businessHour/all' : 'production_link')
      .then(res => {
        this.businessHours = Object.keys(res.data)
      })
      .catch(e => console.log(e))

    this.findBusinessHour()
  },
  data() {
    return {
      user: {},
      uid: localStorage.getItem('uid') || 0,
      customerTickets: [],
      currTicket: {},
      currDay: '',
      businessHours: [],
      open: '',
      close:'',
      currTicketNumber: 0
    }
  },
  methods: {
  //finds the business hour for selected ticket
    findBusinessHour(day){
      for(let i=0; i<this.businessHours.length; i++){
        if(day === this.businessHours[i]){
          this.open = this.businessHours.openTime
          this.close = this.businessHours.closeTime
          return
        }
      }
    },
    //loads previous ticket in customerTickets list
    prev() {
      if (this.currTicketNumber != 0) {
        this.currTicketNumber--
        this.currTicket = this.user.customerTickets[this.currTicketNumber]
        this.findBusinessHour()
      }
    },
    //loads next ticket in customerTickets list
    next() {
      if (this.currTicketNumber != (this.user.customerTickets.length - 1)) {
        this.currTicketNumber++
        this.currTicket = this.user.customerTickets[this.currTicketNumber]
        this.findBusinessHour()
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
