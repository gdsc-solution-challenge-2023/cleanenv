'use strict';
var map;
var directionsService;
var directionsRenderer;
var sourceAutocomplete;
var destAutocomplete;

const firebaseConfig = {

    apiKey: "AIzaSyAOGp673sFqTb-uDscPYcwnMPygrvU4qz8",
    authDomain: "cleanenv-4ca72.firebaseapp.com",
    databaseURL: "https://cleanenv-4ca72-default-rtdb.firebaseio.com",
    projectId: "cleanenv-4ca72",
    storageBucket: "cleanenv-4ca72.appspot.com",
    messagingSenderId: "467200291841",
    appId: "1:467200291841:web:b826e461e753467db8fc00",
    measurementId: "G-32SPFFWV7J"

  };

//initialize firebase
firebase.initializeApp(firebaseConfig);

//reference firebase
const cleanEnvDB = firebase.database().ref('users');
const cleanEnvDBemail = firebase.database().ref('EmailToPhone');

if(document.body.classList.contains('signUpPage')){
  document.getElementById("signUpForm").addEventListener("submit", submitForm);
    function submitForm(e) {
      e.preventDefault();
      let passwordEle = document.getElementById("password");
      passwordEle.type = 'password';

      let emailid = getElementVal("email");
      let name = getElementVal("fullName");
      let phone = getElementVal("phoneNumber");
      let password = getElementVal("password");
      let phoneNum = document.getElementById("phoneNumber");
      phone = '+91' + phone;
      let myJsonString = JSON.stringify(phone);
      sessionStorage.setItem('phone', myJsonString);
      let pass = document.getElementById("password");
      let phoneFlag = 0;
      let passwordFlag = 0;
      if(phone.length != 13){
          phoneFlag = 0;
          phoneNum.classList.add("is-invalid");
          phoneNum.style.borderColor = "red";
      }
      else
      {
        phoneFlag = 1;
        phoneNum.classList.remove("is-invalid");
        phoneNum.style.borderColor = "#81a969";
      }
      if(password.length > 16 || password.length < 8){
          passwordFlag = 0;
          pass.classList.add("is-invalid");
          pass.style.borderColor = "red";
      }
      else{
        passwordFlag = 1;
        pass.classList.remove("is-invalid");
        pass.style.borderColor = "#81a969";

      }
      if(phoneFlag === 1 && passwordFlag === 1){
        phoneNum.classList.remove("is-invalid");
        phoneNum.style.borderColor = "#81a969";
        pass.classList.remove("is-invalid");
        pass.style.borderColor = "#81a969";

        cleanEnvDB.child(phone).once('value', function(snapshot) {
          let exists = (snapshot.val() !== null);
          if(exists)
          {
            alert("User exists, Please login")
          }
          else{
            saveMessages(phone, name, emailid, password);
          window.location.href="Recycle.html";
          }
        });
    
      }
        //   reset the form
        document.getElementById("signUpForm").reset();
    }
}

else if(document.body.classList.contains('loginPage'))
{
  document.getElementById("loginForm").addEventListener("submit", submitForm);
  function submitForm(e){
      e.preventDefault();
      let phone = getElementVal("loginPhone");
      let currPassword = getElementVal("loginPassword");
      let phoneNum = document.getElementById("loginPhone");
      let passwordEle = document.getElementById("loginPassword");
      passwordEle.type = 'password';
      phone = '+91' + phone;
      let phoneFlag = 0;

      if(phone.length != 13){
          phoneFlag = 0;
          phoneNum.classList.add("is-invalid");
          phoneNum.style.borderColor = "red";
      }
      else
      {
          phoneFlag = 1;
          phoneNum.classList.remove("is-invalid");
          phoneNum.style.borderColor = "#81a969";
      }

      cleanEnvDB.child(phone).once('value', function(snapshot) {
          let exists = (snapshot.val() !== null);

          if(exists)
          {
            let password = snapshot.val()['password'];
            if(password === currPassword)
                window.location.href="Recycle.html";
            else
              alert("User does not exist, Please Sign Up or Check your Password");
          }
          else{
            alert("User does not exist, Please Sign Up or Check your Password");
          }
      });


  }


}

function myFunction(elementId) {
  let x = document.getElementById(elementId);
  if (x.type === "password") {
    x.type = "text";
  } else {
    x.type = "password";
  }
}

const saveMessages = (phone, name, emailid, password) => {
    cleanEnvDB.child(phone).set({
    name: name,
    email: emailid,
    password: password,
    phone: phone
  });
};
     

const getElementVal = (id) => {
  let val = document.getElementById(id).value;
  return val;
};