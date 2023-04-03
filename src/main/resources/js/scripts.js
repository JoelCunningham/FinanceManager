// On page load, retrieve state from local storage
window.onload = function() {
  var active_id = localStorage.getItem('active_id');
  if (active_id) {
      showContent(active_id);
  }

  makeColumnsReadonly();
}

// Code for menu selector buttons
function showContent(id) {
    // Open section
    var contents = document.querySelectorAll('.active');
    for (var i = 0; i < contents.length; i++) {
        contents[i].classList.remove('active');
    }

    document.getElementById(id).classList.add('active');
    document.getElementById("btn_" + id).classList.add('active');

    localStorage.setItem('active_id', id);
}

// Code for the save button (RIP)
function tempChangeText(button, text) {
  curr_text = button.innerHTML;
  button.innerHTML = text;
  button.classList.add('clicked');
  setTimeout(function() {
    button.innerHTML = curr_text;
    button.classList.remove('clicked');
  }, 2000);
}  

// Code to make fist and last table columns read only
function makeColumnsReadonly() {
  var table = document.querySelector('tbody');
  var rows = table.querySelectorAll('tr');
  for (var i = 0; i < rows.length; i++) {
      var first_cell = rows[i].querySelector('td:first-child');
      var last_cell = rows[i].querySelector('td:last-child');
      first_cell.querySelector('input').setAttribute('readonly', true);
      last_cell.querySelector('input').setAttribute('readonly', true);
  }
}