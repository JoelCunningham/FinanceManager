// On page load, retrieve state from local storage
window.onload = function() {
  var active_page = localStorage.getItem('active_page');
  if (active_page) {
      showContent(active_page);
  }

  var active_form = localStorage.getItem('active_form');
  if (active_form) {
      showForm(active_form);
  }

  makeColumnsReadonly();
  makeColumnsHidden();
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

    localStorage.setItem('active_page', id);
}

// Code for forms
function showForm(id) {
  // Open section
  document.getElementById(id).classList.add('visible');
  document.getElementById("page").classList.add('overlay');

  localStorage.setItem('active_form', id);
}

// Code for forms
function hideForm() {
  // Open section
  var elements = document.querySelectorAll('.util_form');
  for (var i = 0; i < elements.length; i++) {
    elements[i].classList.remove('visible');
  }
  document.getElementById("page").classList.remove('overlay');

  localStorage.removeItem('active_form');
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
  var tables = document.querySelectorAll('tbody');
  for (var t = 0; t < tables.length; t++) {
    var table = tables[t];
    var rows = table.querySelectorAll('tr');
    for (var i = 0; i < rows.length; i++) {
      var cells = rows[i].querySelectorAll('td');
      for (var j = 0; j < 3; j++) {
        cells[j].querySelector('input').setAttribute('readonly', true);
      }
      cells[15].querySelector('input').setAttribute('readonly', true);
    }
  }
}

// Code to hide the first two columns of all tables
function makeColumnsHidden() {
  
  // Code to hide the head
  var table_heads = document.querySelectorAll('thead');
  for (var t = 0; t < table_heads.length; t++) {
    var head = table_heads[t];
    var cell = head.querySelectorAll('th');
    for (var i = 0; i < 2; i++) {
      cell[i].classList.add('hidden');
    }
  }

  // Code to hide the data
  var table_bodies = document.querySelectorAll('tbody');
  for (var t = 0; t < table_bodies.length; t++) {
    var body = table_bodies[t];
    var rows = body.querySelectorAll('tr');
    for (var i = 0; i < rows.length; i++) {
      var cells = rows[i].querySelectorAll('td');
      for (var j = 0; j < 2; j++) {
        cells[j].classList.add('hidden');
      }
    }
  }
  
}