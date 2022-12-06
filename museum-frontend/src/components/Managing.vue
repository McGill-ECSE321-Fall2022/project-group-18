<template>
  <!-- Employee management page -->
  <div id ='employee'>
    <h1> Add new employee </h1>
    <div id ='employee-input'>
      <b-container class="bv-example-row">
        <b-row>
          <b-col>Select employee username
            <div id = 'emp-username'>
              <!--element which is going to render the TextBox-->
              <input v-model ="usernameEmployee"
                     placeholder="Enter employee username"
              />
            </div>
          </b-col>
          <b-col>Select Employee password
            <div id="emp-password">
              <!--element which is going to render the TextBox-->
              <input v-model ="passwordEmployee"
                     placeholder="Enter employee password"
              />
            </div>
          </b-col>
          <b-col> Select Employee first name
            <div id="emp-first-name">
              <!--element which is going to render the TextBox-->
              <input v-model ="firstNameEmployee"
                     placeholder="First name employee"
              />
            </div>
          </b-col>
          <b-col> Select Employee last name
            <div id="emp-last-name">
              <!--element which is going to render the TextBox-->
              <input v-model ="lastNameEmployee"
                     placeholder="Last name employee"
              />
            </div>
          </b-col>
        </b-row>
      </b-container>
    </div>
<!--    Create an employee by using the fields above when clicking on the button and we make sure that we need everything inputed to proceed-->
    <div id="create-employee">
      <b-button :disabled="!usernameEmployee || !passwordEmployee || !firstNameEmployee || !lastNameEmployee" type="submit"
                @click="createEmployee(usernameEmployee, passwordEmployee, firstNameEmployee, lastNameEmployee)">
        Create Employee
      </b-button>
    </div>
    <br>
    <h1>Delete employee</h1>

    <div id ='employee-delete'>
      <b-container class="bv-example-row">
        <b-row>
          <b-col>
            <div id = 'emp-delete'>
              <!--element which is going to render the TextBox-->
              <input v-model ="deleteId"
                     placeholder="Enter Account ID"
              />
            </div>
          </b-col>

        </b-row>
      </b-container>
      <div id="delete-employee">
<!--        Again on click we call delete employee which will happen when everything is inputted-->
        <b-button :disabled="!deleteId" type="submit"
                  @click="deleteEmployee(deleteId)">
          Delete Employee
        </b-button>
      </div>
    </div>
    <br>
    <h1>List of employees</h1>


    <div id="view-employees">
<!--      Show a list with all the employees-->
      <b-table striped hover :items="employees"> Employees </b-table>
    </div>
  </div>
</template>

<script>

import axios from 'axios'
import {BIconSearch} from "bootstrap-vue";

export default {
  components: {
    BIconSearch
  },
  //Get all the employees
  mounted() {
    axios.get(process.env.NODE_ENV === "development"
    ? 'http://localhost:8080/employee/all' : 'production_link')
      .then(res => {
        this.employees = res.data
      })
      .catch(e => console.log(e))
  },
  data(){
    return {
      //Create the fields to use
      employees: [],
      filteredEmployees: [],
      usernameEmployee: '',
      passwordEmployee: '',
      firstNameEmployee: '',
      lastNameEmployee: '',
      filter:'',
      deleteId: ''
      // employeesList: []
    }
  },
  methods: {
    //Calling the employees link (POST method) to produce an employee with the inputted arguments
     async createEmployee(usernameEmployee,passwordEmployee,firstNameEmployee,lastNameEmployee){
        await axios.post('http://localhost:8080/employee', {
        username: usernameEmployee,
        password : passwordEmployee,
        firstName : firstNameEmployee,
        lastName : lastNameEmployee,
      })
        .catch(error => {
          console.log(error)
        })
       window.location.reload();
    },
    //Delete an employee if the id matches by using the delete method from the backend.
    deleteEmployee(deleteId){
       for(let i = 0; i < this.employees.length; i++){
         if(this.employees[i].accountID == deleteId){
           axios.post('http://localhost:8080/employee/delete/' + this.employees[i].accountID)
             .catch(error => {
               console.log("http://localhost:8080/employee/delete/" + this.employees[i].accountID)
             })
         }

       }
       //Empty the list so that the next call of mounted, we will not have anymore that employee
      this.employees = []

      window.location.reload();
     }

  }
}

</script>

<style scoped>
.myDiv{
  left: 300px;
  position: absolute;
}
.hire{
  left: 800px;
  position: absolute;
}
</style>
