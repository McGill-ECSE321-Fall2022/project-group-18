<template>
  <div v-if="utype === 'employee' || utype === 'owner'">
    <h1>Loan Approval List</h1>
    <b-card class="mt-3 mx-auto text-center" style="width: 40rem;" v-if="checkLoanExist">
      <b-card-title>
        No Loan Request
      </b-card-title>
    </b-card>
    <b-card class="mt-3 mx-auto text-center" style="width: 40rem;" v-for="loan in loansDetailList">
      <b-card-title>
        Loan {{ loan.loanID }}
      </b-card-title>
      <b-card-text>
        Loan Fee: {{ loan.loanFee }}
      </b-card-text>
      <b-card-text>
        Status: {{ loan.loanStatus ? 'Approved' : 'Not Approved' }}
      </b-card-text>
      <b-card-text>
        <b-list-group>
          <b-list-group-item v-for="loanArtifact in loan.loanArtifactList">
            <h4> {{ loanArtifact.name }}</h4>
            <p class="mb-1">
              Type: {{ loanArtifact.type }}
            </p>
            <p class="mb-1">
              Fee: {{ loanArtifact.loanFee }}
            </p>
            <p class="mb-1">
              Status: {{ loanArtifact.loaned ? 'Loaned' : 'Not Loaned' }}
            </p>
          </b-list-group-item>
        </b-list-group>
      </b-card-text>
      <b-button v-if="approveButton(loan)" variant="primary" @click="loanApprove(loan)">Approve</b-button>
      <b-button v-if="rejectButton(loan)" variant="danger" @click="loanReject(loan)">Reject</b-button>
      <b-button v-else variant="primary" @click="loanReturn(loan)">Return</b-button>
    </b-card>
  </div>
  <div v-else>
    <h1>Cannot access this page!</h1>
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
      })
      .catch(e => console.log(e))
  },
  data() {
    return {
      loansIDList: [],
      loansDetailList:[],
      utype: localStorage.getItem('utype') || ''
    }
  },
  methods: {
    // checkUserLevel() {
    //   return this.utype === 'employee' || this.utype === 'owner';
    // },
    rejectButton(loan) {
      return loan.loanStatus === false;
    },
    approveButton(loan) {
      // check if every artifact in the loan is loaned
      for (let i = 0; i < loan.loanArtifactList.length; i++) {
        if (loan.loanArtifactList[i].loaned === true) {
          return false
        }
      }
      return true
    },
    handleLoanIDUpdate: function () {
      this.loansIDList = []
      axios.get(process.env.NODE_ENV === "development"
        ? 'http://localhost:8080/loan/customer/all' : 'production_link')
        .then(res => {
          this.loansIDList.concat(Object.keys(res.data))
        })
        .catch(e => console.log(e))
    },
    loanReturn(loan) {
      let loanID = loan.loanID
      let customerID = loan.customerID
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/customer/${customerID}/loans/delete?loanID=${loanID}` : 'production_link')
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/update/return?loanID=${loanID}` : 'production_link')
      this.handleLoanIDUpdate()
      window.location.reload()
    },
    loanReject(loan) {
      let loanID = loan.loanID
      let customerID = loan.customerID
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/customer/${customerID}/loans/delete?loanID=${loanID}` : 'production_link')
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/update/remove?loanID=${loanID}` : 'production_link')
      this.handleLoanIDUpdate()
      window.location.reload()
    },
    loanApprove(loan) {
      let loanID = loan.loanID
      let customerID = loan.customerID
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/update/approve?loanID=${loanID}` : 'production_link')
      axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/customer/${customerID}/loans/approve?loanID=${loanID}` : 'production_link')
      this.handleLoanInfoUpdate()
      window.location.reload()
    },

    handleLoanInfoUpdate: function () {
      for (let i = 0; i < this.loansDetailList.length; i++) {
        this.loansDetailList.pop()
      }
      for (let i = 0; i < this.loansIDList.length; i++) {
        let retrievedLoanID = this.loansIDList[i]
          axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/${retrievedLoanID}` : 'production_link')
          .catch(e => console.log(e))
          .then(res => {
            console.log(res.data)
            this.loansDetailList.push(res.data)
          })
      }
    }
  },
  watch: {
    loansIDList() {
      this.handleLoanInfoUpdate()
    }
  },
  computed: {
    checkLoanExist() {
      return this.loansIDList.length === 0;
    }
  }
}
</script>
<style>
  b-card {
    max-width: 10px;
  }
</style>
