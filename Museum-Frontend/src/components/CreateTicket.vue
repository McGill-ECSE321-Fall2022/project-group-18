<template>
  <b-container class="bv-example-row">
    <b-row>
      <h1>Buy a Ticket</h1>
    </b-row>

    <b-row>
      <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
        <b-col md="auto">
          <b-calendar v-model="value" @context="onContext" locale="en-US"></b-calendar>
        </b-col>
      </b-col>
      <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
        <h1>{{ value }}</h1>
        <div class="box1">
          <b-row>
            <b-col>
              <div class="col">
                <div class="row-xs-0 my-auto">
                  <p1 style="font-size: 25px;text-align-all: center">Ticket Price:</p1>
                </div>
              </div>
            </b-col>
            <b-col>
              <b-input-group prepend="$" class="p-1 mb-2 mr-sm-2 mb-sm-0" style="width: 115px;">
                <b-form-input v-model="price" id="inline-form-input-username" placeholder="Price"></b-form-input>
              </b-input-group>
            </b-col>
            <b-row>
              <b-col>
                <b-button style="font-size: 40px" variant="primary" class="p-1" @click="createTicket">Create</b-button>
              </b-col>
            </b-row>
          </b-row>
          <b-row><p1>{{created}}</p1></b-row>
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
  data() {
    return {
      price: '',
      value: '',
      tickets: [],
      newTicket: '',
      errorTicket: '',
      created: false
    }
  },
  methods: {
    createTicket() {
      axios.post(process.env.NODE_ENV === "development"
          ? 'http://localhost:8080/ticket' : 'production_link',
        {
          date: this.value,
          price: this.price
        })
        .catch(e => console.log(e))
      this.created = true
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
