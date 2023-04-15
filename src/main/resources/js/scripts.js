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
  makeLastRowReadonly();
  makeColumnsHidden();
  changeHeaderSelector();
  validateForms();
  toggleCheckboxes();
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
  
  hideForm();

  uncheckAll();

  document.getElementById(id).classList.add('visible');
  document.getElementById("page").classList.add('overlay');

  localStorage.setItem('active_form', id);
}

// Code for forms
function hideForm() {
  // Open section
  var elements = document.querySelectorAll('.util_page');
  for (var i = 0; i < elements.length; i++) {
    elements[i].classList.remove('visible');
  }

  var elements = document.querySelectorAll('.text_input');
  for (var i = 0; i < elements.length; i++) {
    elements[i].value = "";
  }

  document.getElementById("page").classList.remove('overlay');
  document.getElementById("category_error").classList.add('hidden')

  uncheckAll();

  localStorage.removeItem('active_form');
}

// Code for dynamic header selector
function changeHeaderSelector() {
  document.getElementById('c_type_select').addEventListener('change', function() {
    if (this.value === 'Expenses') {
        document.getElementById('c_income_header_select').classList.add('hidden');
        document.getElementById('c_expense_header_select').classList.remove('hidden');
    } else if (this.value === 'Incomes') {
        document.getElementById('c_expense_header_select').classList.add('hidden');
        document.getElementById('c_income_header_select').classList.remove('hidden');
    }
  });
}

// Code to validate forms
function validateForms() {
  document.getElementById("create_category_form").addEventListener("submit", function(event) {
    var nameInput = document.getElementById("c_name_input");
    if (nameInput.value.trim() === "") {
        alert("Name input cannot be blank");
        event.preventDefault();
    }
  });
  document.getElementById("create_header_form").addEventListener("submit", function(event) {
    var nameInput = document.getElementById("h_name_input");
    if (nameInput.value.trim() === "") {
        alert("Name input cannot be blank");
        event.preventDefault();
    }
  });
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
  var tables = document.querySelectorAll('.budget_table tbody');
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

// Code to make last table row read only
function makeLastRowReadonly() {
  var tables = document.querySelectorAll('.budget_table tbody');
  for (var t = 0; t < tables.length; t++) {
    var table = tables[t];
    var row = tables[t].rows[tables[t].rows.length - 1];
    var cells = row.querySelectorAll('td');
    for (var j = 0; j < cells.length; j++) {
      cells[j].querySelector('input').setAttribute('readonly', true);
    } 
  }
}

// Code to hide the first two columns of all tables
function makeColumnsHidden() {
  
  // Code to hide the head
  var table_heads = document.querySelectorAll('.budget_table thead');
  for (var t = 0; t < table_heads.length; t++) {
    var head = table_heads[t];
    var cell = head.querySelectorAll('th');
    for (var i = 0; i < 2; i++) {
      cell[i].classList.add('hidden');
    }
  }

  // Code to hide the data
  var table_bodies = document.querySelectorAll('.budget_table tbody');
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

function toggleCheckboxes() {
  var headerCheckbox = document.querySelector('thead input[name="select_all"]');
  var bodyCheckboxes = document.querySelectorAll('tbody input[name="c_add_checkbox"]');
  headerCheckbox.addEventListener('change', function() {
    for (var i = 0; i < bodyCheckboxes.length; i++) {
      bodyCheckboxes[i].checked = headerCheckbox.checked;
    }
  });
}

function uncheckAll() {
  var checkboxes = document.querySelectorAll('#category_table input[type="checkbox"]');
  for (var i = 0; i < checkboxes.length; i++) {
      checkboxes[i].checked = false;
  }
}