<template>
  <div>
    <h1>Loan Approval</h1>
    <b-card class="mt-3 mx-auto text-center" style="width: 40rem;" v-for="loanID in loans">
      <b-card-title>
        Loan {{ loanID }}
      </b-card-title>
      <b-card-text>
        An example
      </b-card-text>
      <b-button href="#" variant="primary">Approve</b-button>
      <b-button variant="danger">Reject</b-button>
    </b-card>
    <b-button @click="handleLoanOutput">Button</b-button>
  </div>
</template>
<script>
import axios from "axios";

export default {
  mounted() {
    axios.get(process.env.NODE_ENV === "development"
    ? 'http://localhost:8080/loan/customer/all' : 'production_link')
      .then(res => {
        this.loans = Object.keys(res.data)
      })
      .catch(e => console.log(e))

  },
  data() {
    return {
      loanID: 1,
      loans: []
    }
  },
  methods: {
    handleLoanOutput: function () {
      console.log(this.loans)
    }
  }
}
</script>
<style>
  b-card {
    max-width: 10px;
  }
</style>
