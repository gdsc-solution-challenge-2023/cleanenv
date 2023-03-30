'use strict';

// ENTER ADDRESS PAGE
let inputValue = 0;
var map = "";
let input1 = "hello";

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
const cleanEnvDB = firebase.database().ref('orders');


if(document.body.classList.contains('enterAddress'))
{
	let input1 = document.getElementById("addressInput");
	let  input2 = document.getElementById("flat");
	let  input3 = document.getElementById("landmark");
	let autocomplete1 = new google.maps.places.Autocomplete(input1);
	
	let continueButton = document.querySelector('.continue-button');
  continueButton.addEventListener('click', function(event){
    let myJsonString = JSON.stringify(input1.value);
    sessionStorage.setItem('address', myJsonString);
    myJsonString = JSON.stringify(input2.value);
    sessionStorage.setItem('flat', myJsonString);
    myJsonString = JSON.stringify(input3.value);
    sessionStorage.setItem('landmark', myJsonString);
    if(input1.value === "" || input2.value == "" ||input3.value =="")
    {
    	alert("Fill in the fields");
    }
    else
    {
    	window.location.href="Order Summary.html";
    }
  })
}

// TRACK ORDER PAGE
else if(document.body.classList.contains('trackOrderPage')){
	inputValue = sessionStorage.getItem('address');
	console.log(inputValue)
	inputValue = JSON.parse(inputValue);
	//javascript.js
	//set map options
	var myLatLng = { lat: 38.346, lng: -0.4907 };
	var mapOptions = {
	  center: myLatLng,
	  zoom: 13,
	  mapTypeId: google.maps.MapTypeId.ROADMAP,
	};

	//create map
	var map = new google.maps.Map(document.getElementById("googleMap"), mapOptions);
	//create a DirectionsService object to use the route method and get a result for our request
	var directionsService = new google.maps.DirectionsService();

	//create a DirectionsRenderer object which we will use to display the route
		var directionsDisplay = new google.maps.DirectionsRenderer();

	//bind the DirectionsRenderer to the map
		directionsDisplay.setMap(map);
	

    // CANCEL ORDER

  let continueButton = document.getElementById('cancel');
  continueButton.addEventListener('click', function(event){
    let cancelOrder = sessionStorage.getItem('userid');
    cancelOrder = JSON.parse(cancelOrder);

    cleanEnvDB.child(cancelOrder).remove();

    window.location.href="Recycle.html";

    alert("Your Order is Cancelled");

});
}

//calcroute function
function calcRoute() {
  //create request
  var request = {
    origin: inputValue,
    destination: "Ranikuthi More, Netaji Subhash Chandra Bose Road, Gandhi Colony, Netaji Nagar, Kolkata, West Bengal, India",
    travelMode: google.maps.TravelMode.DRIVING, //WALKING, BYCYCLING, TRANSIT
    unitSystem: google.maps.UnitSystem.IMPERIAL,
  };

  //pass the request to the route method
directionsService.route(request, function (result, status) {
	directionsDisplay.setDirections(result);
    if (status == google.maps.DirectionsStatus.OK) {
      //Get distance and time
      const output = document.querySelector("#output");
      output.innerHTML =
        "<p class='font-style-body'>Arriving in " +
        result.routes[0].legs[0].duration.text +
        "</p>";

      //display route
      directionsDisplay.setDirections(result);
    } else {
      //delete route from map
      directionsDisplay.setDirections({ routes: [] });
      //center map in London
      map.setCenter(myLatLng);

      //show error message
      output.innerHTML =
        "<div class='alert-danger'><i class='fas fa-exclamation-triangle'></i> Could not retrieve driving distance.</div>";
    }
  });


}





