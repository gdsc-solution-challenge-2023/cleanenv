'use strict';
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
// refer to database
const cleanEnvDB = firebase.database().ref('users');
cleanEnvDB.child(order).on("value", function(snapshot));

