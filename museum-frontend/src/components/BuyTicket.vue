<template>
  <!-- UI to buy tickets -->
  <b-container class="bv-example-row">
    <b-row>
    <!--Title For Page-->
      <h1>Buy a Ticket</h1>
    </b-row>

    <b-row>
      <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
        <b-col md="auto">
        <!--Interactive Bootstrap Calendar-->
          <b-calendar v-model="value" locale="en-US" @context="context"></b-calendar>
        </b-col>
      </b-col>
      <b-col>
        <b-overlay :show="checkoutShow" spinner-variant="primary" spinner-type="grow" spinner-small rounded="sm">
        <!--overlay for animation at checkout-->
          <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
            <p v-show="!dateShow" style="color: #42b983">Thanks for your purchase!</p>
            <h1 v-show="dateShow">{{ value }}</h1>
            <div class="box1">
              <b-row>
              <!--prompt if date is unavailable-->
                <p1 style="font-size: 25px;text-align-all: center" v-show="promptShow">{{ noDatePrompt }}</p1>
              </b-row>
              <b-row>
                <p1 style="font-size: 25px;text-align-all: center; color: rebeccapurple" v-show="!show">Please Enter
                  Your Payment Information To Confirm Your Purchase:</p1>
              </b-row>
            </div>
            <b-row>
              <div class="box2">
              <!--checkout information-->
                <b-form inline>
                  <label class="sr-only" for="inline-form-input-name" v-show="!show">Name</label>
                  <b-input-group prepend="Name" class="p-2 mb-2 mr-sm-2 mb-sm-0" v-show="!show">
                    <b-form-input id="inline-form-input-username" placeholder="Name on card"
                      v-show="!show"></b-form-input>
                  </b-input-group>

                  <label class="sr-only" for="inline-form-input-username" v-show="!show">Username</label>
                  <b-input-group prepend="Credit Card" class="p-2 mb-2 mr-sm-2 mb-sm-0" v-show="!show">
                    <b-form-input id="inline-form-input-username" placeholder="Credit Card Number"
                      v-show="!show"></b-form-input>
                  </b-input-group>

                  <b-input-group prepend="CVV" class="p-1 mb-2 mr-sm-2 mb-sm-0" style="width: 130px;" v-show="!show">
                    <b-form-input id="inline-form-input-username" placeholder="xxx" v-show="!show"></b-form-input>
                  </b-input-group>
                </b-form>
              </div>
            </b-row>
            <b-row>
              <b-col class="pb-3">
                <p3 class="shadow p-3 my-3 mx-1 bg-white rounded" style="font-weight: bold; font-size: 40px"
                  v-show="!show">
                  ${{ price }}
                </p3>
              </b-col>
              <b-col>
                <b-button style="font-size: 40px" variant="primary" class="p-1" v-show="!show" @click="checkout">
                  Checkout
                </b-button>
              </b-col>
            </b-row>
          </b-col>
        </b-overlay>
      </b-col>
    </b-row>
  </b-container>

</template>
<script>
import axios from 'axios'
import { BIconHandThumbsDown } from 'bootstrap-vue'
import router from '../router'

export default {
  name: 'BuyTicket',
  mounted() {
    // get the ticket id list first
    axios.get(process.env.NODE_ENV === "development"
      ? 'http://localhost:8080/customer/' + localStorage.getItem('uid') : 'production_link')
      .then(res => this.user = res.data)
      .catch(e => console.log(e))
  },
  data() {
    return {
      value: "No Date Selected",
      noDatePrompt: "",
      price: '',
      tickets: [],
      allowedDates: [],
      show: true,
      dateShow: true,
      currentTicket: '',
      newTicket: '',
      checkoutShow: false,
      promptShow: false,
      errorTicket: '',
      user: {},
      uid: localStorage.getItem('uid') || 0,
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
          this.promptShow = false
          return 1
        }
      }
      this.show = true
      for (let i = 0; i < this.user.tickets; i++) {
        if (String(this.value) === String(this.user.customerTickets[i].day)) {
          this.noDatePrompt = "You Have Already Purchased a Ticket For This Date"
          return 1
        }
      }
      if (String(this.value) != "No Date Selected") {
        this.noDatePrompt = "No Tickets For Selected Date"
      }
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
    },
    context() {
      console.log(this.user.customerTickets)
      this.dateShow = true
      this.promptShow = true
      this.getAllowedDates()
      this.checkDate()
    },
    checkout() {
      this.checkoutShow = true
      this.customerBuyTicket()
      setTimeout(() => {
        this.checkoutShow = false
        this.show = true
        this.dateShow = false
        this.promptShow = false
      }, 1000);
    },
    customerBuyTicket() {
      let url = "http://localhost:8080/customer/" + localStorage.getItem('uid') + "/update";
      console.log('ticcket when buy~~', this.currentTicket)
      console.log('user when buy~~', this.user)
      let data = {
        ...this.user, customerTickets: this.user.customerTickets ?
          [...this.user.customerTickets, this.currentTicket] : [this.currentTicket]
      }
      console.log('url buy~~', url)
      console.log('data buy~~', data)

      axios.post(url, data)
        .then(response => {
          console.log(response);
        })
        .catch((error) => console.log(error))
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
