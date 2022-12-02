<template>
  <div>
    <h1>Loan Approval List</h1>
    <h3 v-if="noLoanExist">No Loan Request</h3>
    <b-card class="mt-3 mx-auto text-center" style="width: 40rem;" v-for="(loan, index) in loansDetailList" :key="loan.loanID">
      <b-card-title>
        Loan {{ loan.loanID }}
      </b-card-title>
      <b-card-text>
        Loan Status: {{ loan.loanStatus ? 'Approved' : 'Not Approved' }}
      </b-card-text>
      <b-card-text>
        Loan Fee: {{ loan.loanFee }}
      </b-card-text>
      <b-card-text>
        {{ loan.loanStatus ? 'Loan Approved' : 'Loan Not Approved' }}
      </b-card-text>
      <b-card-text>
        <b-list-group>
          <b-list-group-item v-for="n in loansArtifactList[index].length">
            <h4> {{ loansArtifactList[index][n-1].name }} </h4>
            <p class="mb-1">
              Type: {{loansArtifactList[index][n-1].type}}
            </p>
            <p class="mb-1">
              Fee: {{loansArtifactList[index][n-1].loanFee}}
            </p>
            <p class="mb-1">
              Status: {{ loansArtifactList[index][n-1].loaned ? 'Loaned' : 'Not Loaned' }}
            </p>
          </b-list-group-item>
        </b-list-group>
      </b-card-text>
      <b-button variant="primary" @click="loanApprove(loan)">Approve</b-button>
      <b-button variant="danger">Reject</b-button>
    </b-card>
    <b-button @click="handleLoanOutput">Button</b-button>
  </div>
</template>
<script>
import axios from "axios";

export default {
  mounted() {
    // get the loan id list first
    axios.get(process.env.NODE_ENV === "development"
    ? 'http://localhost:8080/loan/customer/all' : 'production_link')
      .then(res => {
        this.loansIDList = Object.keys(res.data)
        this.loanCustomerIDList = Object.entries(res.data)
      })
      .catch(e => console.log(e))
  },
  data() {
    return {
      loanCustomerIDList: [],
      loansIDList: [],
      loansDetailList:[],
      loansArtifactList:[],
      approveActive: true
    }
  },
  methods: {
    loanApprove(loan) {
      let loanID = loan.loanID
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/update/approve?loanID=${loanID}` : 'production_link')
      this.addLoanToCustomer(loanID)
      this.handleLoanInfoUpdate()
    },
    addLoanToCustomer(loanID) {
      let customerID
      for (const [key, value] of this.loanCustomerIDList) {
        if (key === loanID) {
          customerID = value
          break
        }
      }
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/customer/${customerID}/loans/approve?loanID=${loanID}` : 'production_link')
    },
    handleLoanOutput: function () {
      console.log(this.loansIDList)
      console.log(this.loansDetailList)
      console.log(this.loansDetailList[1])
      console.log(this.loansDetailList[1].loanArtifactDetailList)
    },
    handleLoanInfoUpdate: async function () {
      this.loansDetailList = []
      this.loansArtifactList = []
      for (let i = 0; i < this.loansIDList.length; i++) {
        let retrievedLoanID = this.loansIDList[i]
        let loanArtifactIDList = []
          await axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/${retrievedLoanID}` : 'production_link')
          .catch(e => console.log(e))
          .then(res => {
            console.log(res.data)
            this.loansDetailList.push(res.data)
            loanArtifactIDList = res.data.loanArtifactIDList
          })

        let loanArtifactDetailList = []
        for (let j = 0; j < loanArtifactIDList.length; j++) {
          let loanArtifactID = loanArtifactIDList[j]
          await axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/artifact/${loanArtifactID}` : 'production_link')
            .catch(e => console.log(e))
            .then(res => {
              console.log(res.data)
              loanArtifactDetailList.push(res.data)
            })
        }
        this.loansArtifactList.push(loanArtifactDetailList)
        this.loansDetailList[i].loanArtifactDetailList = loanArtifactDetailList
      }
      // now the list
    }
  },
  watch: {
    loanCustomerIDList() {
      this.handleLoanInfoUpdate()
    }
  }
}
</script>
<style>
  b-card {
    max-width: 10px;
  }
</style>
