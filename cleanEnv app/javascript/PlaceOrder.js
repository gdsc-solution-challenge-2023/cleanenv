'use strict';
var map;
var directionsService;
var directionsRenderer;
var sourceAutocomplete;
var destAutocomplete;
const arr = ['papers', 'plastic', 'metal', 'glass', 'cloth', 'electronics'];
const price= [11, 3, 10, 3, 70, 3000];

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
// Order Summary Page


  let flagRecycleItems = sessionStorage.getItem('data');
  flagRecycleItems = JSON.parse(flagRecycleItems);

  let currDivOrder = document.getElementById('OrderSummary');
  let count = 1;
  let total = 0;

  for(let i = 0; i < arr.length; i++)
  {
    if(flagRecycleItems[i] === 1)
    {
      currDivOrder = document.getElementById('OrderSummary');
      let div = document.createElement("div");
      div.classList.add('row');
      div.classList.add('text-center');
      div.classList.add(`Product${count}`);
      currDivOrder.appendChild(div);

      total += price[i];
      currDivOrder = document.querySelector(`.Product${count}`);
      div = document.createElement("div");
      div.classList.add('col-6');
      div.innerHTML += arr[i];
      currDivOrder.appendChild(div);
      div = document.createElement("div");
      div.classList.add('col-6');
      div.innerHTML += `${price[i]} /-`;
      currDivOrder.appendChild(div);
      div = document.createElement('hr');
      currDivOrder.appendChild(div);


      count ++;
    }
  }
  // set total to local storage
  
  let myJsonString = JSON.stringify(total);
  sessionStorage.setItem('total', myJsonString);


  currDivOrder = document.querySelector(`.Product${count-1}`)
  let div = document.createElement("div");
  div.classList.add('col-6');
  div.innerHTML += 'Total';
  div.style.fontWeight = "bold";
  currDivOrder.appendChild(div);
  div = document.createElement("div");
  div.classList.add('col-6');
  div.innerHTML += `${total} /-`;
  div.style.fontWeight = "bold";
  currDivOrder.appendChild(div);
  div = document.createElement('hr');
  currDivOrder.appendChild(div);

//reference firebase
const cleanEnvDB = firebase.database().ref('orders');
document.getElementById('PlaceOrder').addEventListener("click", submitForm);
    function submitForm(){
      //window.location.href="TrackOrder.html";
      let addressValue = sessionStorage.getItem('address');
      addressValue = JSON.parse(addressValue);
      let totalValue = sessionStorage.getItem('total');
      totalValue = JSON.parse(totalValue);
      let flatValue = sessionStorage.getItem('flat');
      flatValue = JSON.parse(flatValue);
      let landmarkValue = sessionStorage.getItem('landmark');
      landmarkValue = JSON.parse(landmarkValue);
      addressValue += `, ${flatValue}, ${landmarkValue}`;

      let arrayValue = sessionStorage.getItem('data');
      arrayValue = JSON.parse(arrayValue);

      let phoneValue = sessionStorage.getItem('phone');
      phoneValue = JSON.parse(phoneValue);

      // get selected recycle items
      let itemsSelected = []

      for(let i=0; i<arrayValue.length; i++)
      {
        if(arrayValue[i])
          itemsSelected.push(arr[i])
      }

      // bank details
      let input1 = document.getElementById("account");
      let  input2 = document.getElementById("accno");
      let  input3 = document.getElementById("ifsc");

      myJsonString = JSON.stringify(input1.value);
      sessionStorage.setItem('account', myJsonString);
      myJsonString = JSON.stringify(input2.value);
      sessionStorage.setItem('accno', myJsonString);
      myJsonString = JSON.stringify(input3.value);
      sessionStorage.setItem('ifsc', myJsonString);

      if(input1.value === "" || input2.value == "" ||input3.value =="")
      {
        alert("Fill in the fields");
      }
      else
      {
        window.location.href="TrackOrder.html";
      }


      //console.log(addressValue, totalValue, itemsSelected);
      saveMessages(addressValue, "unassigned", totalValue, itemsSelected, phoneValue);
  }



const saveMessages = (address, emp, money, order, orderBy) => {
  let currOrder = cleanEnvDB.push({
    address: address,
    emp : emp,
    money: money,
    order: order,
    orderBy : orderBy

    // const postListRef = ref(db, 'order'); 
    // const newPostRef = push(postListRef);
    // set(newPostRef, {
    //   items: itemsSelected
    // }
  });
  let currId = currOrder.key;
  myJsonString = JSON.stringify(currId);
      sessionStorage.setItem('userid', myJsonString);
};



const savePhone = (phone) => {
  cleanEnvDB.push({
    phone: phone
  });
};
