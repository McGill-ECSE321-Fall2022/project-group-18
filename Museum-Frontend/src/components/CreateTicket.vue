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
            <b-col>
              <div class="col">
                <div class="row-xs-0 my-auto">
                  <p1 style="font-size: 25px;text-align-all: center">Ticket Price:</p1>
                </div>
              </div>
            </b-col>
            <b-col>
              <b-input-group prepend="$" class="p-1 mb-2 mr-sm-2 mb-sm-0" style="width: 115px;">
                <b-form-input id="inline-form-input-username" placeholder="Price"></b-form-input>
              </b-input-group>
            </b-col>
          </b-row>
          <b-row>
            <b-col>
              <p2 style="font-size: 25px;text-align-all: center">Quantity:</p2>
            </b-col>
            <b-col>
              <div>
                <label for="sb-inline"></label>
                <b-form-spinbutton id="sb-inline" v-model="value" inline></b-form-spinbutton>
              </div>
            </b-col>
          </b-row>
        </div>
      </b-col>
    </b-row>
  </b-container>

</template>
<script src="./TicketScript.js">
export default {
  data() {
    return {
      value: 50
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
