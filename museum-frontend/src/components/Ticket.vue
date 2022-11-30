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
        <b-col>
          <p>Value: <b>'{{ value }}'</b></p>
          <p class="mb-0">Context:</p>
          <pre class="small">{{ context }}</pre>
        </b-col>
      </b-col>
      <b-col class="shadow p-3 my-3 mx-1 bg-white rounded">
        <h1>DD-MM-YYYY</h1>
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
            <p3 class="shadow p-3 my-3 mx-1 bg-white rounded" style="font-weight: bold; font-size: 40px">$33.00</p3>
          </b-col>
          <b-col>
            <b-button style="font-size: 40px" variant="primary" class="p-1">Checkout</b-button>
          </b-col>
        </b-row>
      </b-col>
    </b-row>
  </b-container>

</template>
<script src="./TicketScript.js">
export default {
  data() {
    return {
      value: '',
      context: null
    }
  },
  created: function () {
  },
  methods: {
    onContext(ctx) {
      this.context = ctx
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
