'use strict';

// Changing colour on selection of Recycle Items
const arr = ['papers', 'plastic', 'metal', 'glass', 'cloth', 'electronics'];
let flagRecycleItems = [0, 0, 0, 0, 0, 0];
let selected = 0;


if(document.body.classList.contains('Recycle_file'))
{
  // Selecting elements
  const collapseBar = document.querySelector('.navbar-toggler');
  const collapseOptions = document.querySelector('.navbar-collapse');
  let showBar = true;

  collapseBar.addEventListener('click', function(){
    if(showBar){
      collapseOptions.classList.remove('collapse');
      collapseOptions.classList.add('show');
      showBar = false;
    }
    else{
      collapseOptions.classList.remove('show');
      collapseOptions.classList.add('collapse');
      showBar = true;
    }

  });

  //iterate through each class and add Event listener

  for(let i = 0; i < arr.length; i++){
    let recycleItem = document.querySelector(`.${arr[i]}`);
    let imageElement = document.querySelector(`.${arr[i]}-img`);

    recycleItem.addEventListener('click', function()
    {
      if(!flagRecycleItems[i])
      {
        recycleItem.style.backgroundColor = "#81A969";
        imageElement.src = `images/${arr[i]}_dark.png`;
        flagRecycleItems[i] = 1;
        selected += 1;
      }
      else if(flagRecycleItems[i])
      {
        recycleItem.style.backgroundColor = "#ffffff";
        imageElement.src = `images/${arr[i]}_light.png`;
        flagRecycleItems[i] = 0;
        selected -= 1;
      }

    });
  }

  let continueButton = document.querySelector('.continue-button');
  continueButton.addEventListener('click', function(event){
    let myJsonString = JSON.stringify(flagRecycleItems);
    sessionStorage.setItem('data', myJsonString);
    if(selected)
    {
      window.location.href = 'Address Details.html';
    }
    else{
      alert('Please Select Items');
    }
  })
}

// Order Summary Page

else if(document.body.classList.contains('OrderSummary_file')){
  flagRecycleItems = sessionStorage.getItem('data');
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
  localStorage.setItem('total', myJsonString);

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

}

