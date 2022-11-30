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
      <b-button v-if="artifactsLoanedInLoan(loan)" variant="primary" @click="loanApprove(loan)">Approve</b-button>
      <b-button v-if="loanRejectExist(loan)" variant="danger" @click="loanReject(loan)">Reject</b-button>
      <b-button v-else variant="primary" @click="loanReturn(loan)">Return</b-button>
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
      })
      .catch(e => console.log(e))
  },
  data() {
    return {
      loansIDList: [],
      loansDetailList:[],
      loansArtifactList:[],
      approveActive: true
    }
  },
  methods: {
    handleLoanIDUpdate: async function () {
      await axios.get(process.env.NODE_ENV === "development"
        ? 'http://localhost:8080/loan/customer/all' : 'production_link')
        .then(res => {
          this.loansIDList = Object.keys(res.data)
        })
        .catch(e => console.log(e))
    },
    async loanReturn(loan) {
      let loanReturnID = loan.loanID
      await axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/update/return?loanID=${loanReturnID}` : 'production_link')
        .catch(e => console.log(e))
      await this.handleLoanIDUpdate()
      await this.handleLoanInfoUpdate()
    },
    async loanReject(loan) {
      let loanRejectID = loan.loanID
      await axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/update/remove?loanID=${loanRejectID}` : 'production_link')
        .catch(e => console.log(e))
      await this.handleLoanIDUpdate()
      await this.handleLoanInfoUpdate()
    },
    async loanApprove(loan) {
      let loanApproveID = loan.loanID
      await axios.get(process.env.NODE_ENV === "development" ? `http://localhost:8080/loan/update/approve?loanID=${loanApproveID}` : 'production_link')
      await this.handleLoanInfoUpdate()
    },
    loanRejectExist(loan){
      if (loan.loanStatus === false) {
        return true
      }
      return false
    },
    artifactsLoanedInLoan(loan) {
      for (let i = 0; i < loan.loanArtifactDetailList.length; i++) {
        if (loan.loanArtifactDetailList[i].loaned === true) {
          return false
        }
      }
      return true
    },
    noLoanExist: function () {
      return this.loansIDList.length === 0;
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
    loansIDList() {
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
