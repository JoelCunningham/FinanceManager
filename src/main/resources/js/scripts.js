// On page load, retrieve state from local storage
window.onload = function() {
  makeColumnsReadonly();
  makeLastRowReadonly();
  makeColumnsHidden();
  changeHeaderSelector();
  validateForms();
  formatHeaderText();
  toggleCheckboxes();
  categoryToolTips();
  clearCashflowForm();
  validateCashflowForm();
  validateCashflowTable();
  loadState();
  setAlts();
  hideForms();
  isServerDisconnected(1);
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

// Code for the save button 
// TODO make work with event.preventDefault statements or remove
function tempChangeText(button, text) {
  curr_text = button.innerHTML;
  button.innerHTML = text;
  button.classList.add('clicked');
  setTimeout(function() {
    button.innerHTML = curr_text;
    button.classList.remove('clicked');
  }, 2000);
}  

// Code to make first and last table columns read only
function makeColumnsReadonly() {
  var tables = document.querySelectorAll('.budget_table tbody');
  for (var t = 0; t < tables.length; t++) {
    var table = tables[t];
    var rows = table.querySelectorAll('tr');
    for (var i = 0; i < rows.length; i++) {
      var cells = rows[i].querySelectorAll('td');
      cells[0].querySelector('input').setAttribute('readonly', true);
      cells[1].querySelector('input').setAttribute('readonly', true);
      cells[2].querySelector('input').setAttribute('readonly', true);
      cells[cells.length - 1].querySelector('input').setAttribute('readonly', true);
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

function formatHeaderText () {
  const text_elements = document.querySelectorAll('h3');

  text_elements.forEach((text_element) => {
    var text = text_element.textContent;

    var tempTextSpan = document.createElement('span');
    tempTextSpan.style.fontSize = window.getComputedStyle(text_element, null).getPropertyValue('font-size');
    tempTextSpan.style.fontFamily = window.getComputedStyle(text_element, null).getPropertyValue('font-family');
    tempTextSpan.textContent = text;
    document.body.appendChild(tempTextSpan);

    var parentDiv = text_element.parentElement;
    var originalDisplayValues = [];
    while (parentDiv) {
      originalDisplayValues.push(parentDiv.style.display);
      parentDiv.style.display = 'block';
      parentDiv = parentDiv.parentElement;
    }
    parentDiv = text_element.parentElement;

    text_element.style.top = `calc(${parentDiv.offsetHeight}px/2 + ${tempTextSpan.offsetWidth}px/2.2)`;
    
    document.body.removeChild(tempTextSpan);
    while (parentDiv) {
      parentDiv.style.display = originalDisplayValues.shift();
      parentDiv = parentDiv.parentElement;
    }
  });
}

// Code for checkbox column
function toggleCheckboxes() {
  var headerCheckbox = document.querySelector('thead input[name="select_all"]');
  var bodyCheckboxes = document.querySelectorAll('tbody input[name="c_add_checkbox"]');
  headerCheckbox.addEventListener('change', function() {
    for (var i = 0; i < bodyCheckboxes.length; i++) {
      bodyCheckboxes[i].checked = headerCheckbox.checked;
    }
  });
}

// Code to reset checkbox column
function uncheckAll() {
  var checkboxes = document.querySelectorAll('#category_table input[type="checkbox"]');
  for (var i = 0; i < checkboxes.length; i++) {
      checkboxes[i].checked = false;
  }
}

//Code to add tooltips to categories in tables
function categoryToolTips() {
  let tables = document.querySelectorAll('.budget_table');
  for (let t = 0; t < tables.length; t++) {
      let table = tables[t];
      let rows = table.rows;
      for (let i = 0; i < rows.length - 1; i++) {
          let cell = rows[i].cells[2];
          let input = cell.querySelector('input');
          if (input) {
              cell.title = input.value;
          }
      }
  }
}

//Code to enforce type dropdowns
function validateCashflowType() {
  var input = document.getElementById("cashflow_type_input").value;
  var options = document.getElementById("cashflow_types").options;
  var found = false;
  
  for (var i = 0; i < options.length; i++) {
    if (options[i].value === input || input === "") {
    found = true;
    }
  }
  
  if (!found) {
    document.getElementById("cashflow_type_input").value = "";
    document.getElementById("cashflow_header_input").value = "";
    document.getElementById("cashflow_category_input").value = "";
  }
}

//Code to enforce header dropdowns
function validateCashflowHeader() {
  var input = document.getElementById("cashflow_header_input").value;
  var options = document.getElementById("cashflow_headers").options;
  var found = false;
  
  for (var i = 0; i < options.length; i++) {
    if (options[i].value === input || input === "") {
    found = true;
    }
  }
  
  if (!found) {
    document.getElementById("cashflow_header_input").value = "";
    document.getElementById("cashflow_category_input").value = "";
  }
}

//Code to enforce category dropdowns
function validateCashflowCategory() {
  var input = document.getElementById("cashflow_category_input").value;
  var options = document.getElementById("cashflow_categories").options;
  var found = false;
  
  for (var i = 0; i < options.length; i++) {
    if (options[i].value === input || input === "") {
    found = true;
    break;
    }
  }
  
  if (!found) {
    document.getElementById("cashflow_category_input").value = "";
  }
}

//Code to set dropdowns based on other dropdowns
document.addEventListener('DOMContentLoaded', function() {
  let cashflow_types = ["Incomes", "Expenses"];
  let cashflow_headers = window.cashflow_headers;

  document.querySelector('#cashflow_type_select').addEventListener('change', function() {
    let selectedType = this.value;
    let headersSelect = document.querySelector('#cashflow_header_select');
    headersSelect.innerHTML = '<option value="" disabled selected>Header</option>';
    // Find the index of the selected type in cashflow_types
    let typeIndex = cashflow_types.indexOf(selectedType);
    // Get the corresponding headers for the selected type
    let headers = cashflow_headers[typeIndex];
    // Add the headers as options to the select
    headers.forEach(function(header) {
        let option = document.createElement('option');
        option.value = header;
        option.text = header;
        headersSelect.appendChild(option);
    });
  });
});

document.addEventListener('DOMContentLoaded', function() {
  let cashflow_types = ["Incomes", "Expenses"];
  let cashflow_headers = window.cashflow_headers;
  let cashflow_categories = window.cashflow_categories;

  document.querySelector('#cashflow_header_select').addEventListener('change', function() {
    let selectedHeader = this.value;
    let selectedType = document.querySelector('#cashflow_type_select').value;
    let categoriesSelect = document.querySelector('#cashflow_category_select');
    categoriesSelect.innerHTML = '<option value="" disabled selected>Category</option>';
    // Find the index of the selected type in cashflow_types
    let typeIndex = cashflow_types.indexOf(selectedType);
    // Find the index of the selected header in cashflow_headers
    let headerIndex = cashflow_headers[typeIndex].indexOf(selectedHeader);
    // Get the corresponding categories for the selected header
    let categories = cashflow_categories[typeIndex][headerIndex];
    // Add the categories as options to the select
    categories.forEach(function(category) {
        let option = document.createElement('option');
        option.value = category;
        option.text = category;
        categoriesSelect.appendChild(option);
    });
  });
});

//Code to clear the cashflow form
function clearCashflowForm() {
  document.getElementById("cashflow_type_select").selectedIndex = 0;
  document.getElementById("cashflow_header_select").selectedIndex = 0;
  document.getElementById("cashflow_category_select").selectedIndex = 0;

  document.getElementById("cashflow_value").value = "";
  document.getElementById("cashflow_date").value = "";
  document.getElementById("cashflow_details").value = "";
}

//Code for table dropdowns
document.addEventListener('DOMContentLoaded', function() {
  let cashflow_types = ["Incomes", "Expenses"];
  let cashflow_headers = window.cashflow_headers;
  let cashflow_categories = window.cashflow_categories;
  document.querySelectorAll('.cashflow_table select[name="cashflow_table"]').forEach(function(select) {
    if (select.parentElement.cellIndex === 1) { // Type column
      let selectedType = select.value;
      let headersSelect = select.parentElement.nextElementSibling.querySelector('select');
      headersSelect.innerHTML = '<option value="" disabled selected>Header</option>';
      // Find the index of the selected type in cashflow_types
      let typeIndex = cashflow_types.indexOf(selectedType);
      // Get the corresponding headers for the selected type
      let headers = cashflow_headers[typeIndex];
      // Add the headers as options to the select
      headers.forEach(function(header) {
        let option = document.createElement('option');
        option.value = header;
        option.text = header;
        headersSelect.appendChild(option);
      });
      // Set the selected value of the headersSelect
      headersSelect.value = headersSelect.getAttribute('value');

      let categoriesSelect = headersSelect.parentElement.nextElementSibling.querySelector('select');
      categoriesSelect.innerHTML = '<option value="" disabled selected>Category</option>';  
  
      select.addEventListener('change', function() {
        let selectedType = this.value;
        let headersSelect = this.parentElement.nextElementSibling.querySelector('select');
        headersSelect.innerHTML = '<option value="" disabled selected>Header</option>';
        // Find the index of the selected type in cashflow_types
        let typeIndex = cashflow_types.indexOf(selectedType);
        // Get the corresponding headers for the selected type
        let headers = cashflow_headers[typeIndex];
        // Add the headers as options to the select
        headers.forEach(function(header) {
          let option = document.createElement('option');
          option.value = header;
          option.text = header;
          headersSelect.appendChild(option);
        });

        let categoriesSelect = headersSelect.parentElement.nextElementSibling.querySelector('select');
        categoriesSelect.innerHTML = '<option value="" disabled selected>Category</option>';
    
      });
    } else if (select.parentElement.cellIndex === 2) { // Header column
      let selectedHeader = select.getAttribute('value');
      let selectedType = select.parentElement.previousElementSibling.querySelector('select').value;
      let categoriesSelect = select.parentElement.nextElementSibling.querySelector('select');
      categoriesSelect.innerHTML = '<option value="" disabled selected>Category</option>';
      // Find the index of the selected type in cashflow_types
      let typeIndex = cashflow_types.indexOf(selectedType);
      // Find the index of the selected header in cashflow_headers
      let headerIndex = cashflow_headers[typeIndex].indexOf(selectedHeader);
      // Get the corresponding categories for the selected header
      let categories = cashflow_categories[typeIndex][headerIndex];
      // Add the categories as options to the select
      categories.forEach(function(category) {
        let option = document.createElement('option');
        option.value = category;
        option.text = category;
        categoriesSelect.appendChild(option);
      });
      // Set the selected value of the categoriesSelect
      categoriesSelect.value = categoriesSelect.getAttribute('value');
  
      select.addEventListener('change', function() {
        let selectedHeader = this.value;
        let selectedType = this.parentElement.previousElementSibling.querySelector('select').value;
        let categoriesSelect = this.parentElement.nextElementSibling.querySelector('select');
        categoriesSelect.innerHTML = '<option value="" disabled selected>Category</option>';
        // Find the index of the selected type in cashflow_types
        let typeIndex = cashflow_types.indexOf(selectedType);
        // Find the index of the selected header in cashflow_headers
        let headerIndex = cashflow_headers[typeIndex].indexOf(selectedHeader);
        // Get the corresponding categories for the selected header
        let categories = cashflow_categories[typeIndex][headerIndex];
        // Add the categories as options to the select
        categories.forEach(function(category) {
          let option = document.createElement('option');
          option.value = category;
          option.text = category;
          categoriesSelect.appendChild(option);
        });
      });
    }
  });
});

// Code to validate cashflow form
function validateCashflowForm() {
  document.getElementById("cashflow_form").addEventListener("submit", function(event) {
    if (document.getElementById("cashflow_type_select").value.trim() === "") {
      alert("Please choose a Type for the cash flow");
      event.preventDefault();
    }
    else if (document.getElementById("cashflow_header_select").value.trim() === "") {
      alert("Please choose a Header for the cash flow");
      event.preventDefault();
    }
    else if (document.getElementById("cashflow_category_select").value.trim() === "") {
      alert("Please choose a Category for the cash flow");
      event.preventDefault();
    }
    else if (document.getElementById("cashflow_value").value.trim() === "") {
      alert("Please enter a Value for the cash flow");
      event.preventDefault();
    }
    else if (document.getElementById("cashflow_date").value.trim() === "") {
      alert("Please choose a Date for the cash flow");
      event.preventDefault();
    }
  });
}

// Code to validate cashflow table
function validateCashflowTable() {
  document.getElementById("statement_form").addEventListener("submit", function(event) {
    let elements = document.querySelectorAll('.cashflow_table select[name="cashflow_table"], .cashflow_table input[name="cashflow_table"]');
    for (let i = 0; i < elements.length; i++) {
      let element = elements[i];
      if (element.parentElement.cellIndex === 1 && element.value.trim() === "") { // Type column
        alert("Please make sure you have correctly set a Type for all cash flows");
        event.preventDefault();
        break;
      }
      else if (element.parentElement.cellIndex === 2 && element.value.trim() === "") { // Header column
        alert("Please make sure you have correctly set a Header for all cash flows");
        event.preventDefault();
        break;
      }
      else if (element.parentElement.cellIndex === 3 && element.value.trim() === "") { // Category column
        alert("Please make sure you have correctly set a Category for all cash flows");
        event.preventDefault();
        break;
      }
      else if (element.parentElement.cellIndex === 4 && element.value.trim() === "") { // Value column
        alert("Please make sure you have correctly entered a Value for all cash flows");
        event.preventDefault();
        break;
      }
      else if (element.parentElement.cellIndex === 6 && element.value.trim() === "") { // Date column
        alert("Please make sure you have correctly set a Date for all cash flows");
        event.preventDefault();
        break;
      }
    }
  });
}

// Code to load the state of the app when it was last used
function loadState() {
  var active_page = localStorage.getItem('active_page');
  if (active_page) {
      showContent(active_page);
  }

  var active_form = localStorage.getItem('active_form');
  if (active_form) {
      showForm(active_form);
  }

  // This code restores the last selections in dropdown lists.
  // However, it operates after data is loaded
  // Therefore, there is a discrepency between the shown year/month
  // and the data shown.
  // This issue must be resolved before this functionality can be restored

  // var budget_year = localStorage.getItem('budget_year');
  // if (budget_year) {
  //   console.log(budget_year);
  //   document.querySelector('select[name="budget_year_selector"]').value = budget_year;
  // }

  // var summary_year = localStorage.getItem('summary_year');
  // if (summary_year) {
  //   console.log(summary_year);
  //   document.querySelector('select[name="summary_year_selector"]').value = summary_year;
  // }

  // var statement_year = localStorage.getItem('statement_year');
  // if (statement_year) {
  //   console.log(statement_year);
  //   document.querySelector('select[name="statement_year_selector"]').value = statement_year;
  // }

  // var statement_month = localStorage.getItem('statement_month');
  // if (statement_month) {
  //   console.log(statement_month);
  //   document.querySelector('select[name="statement_month_selector"]').value = statement_month;
  // }
}

// Code to save the state of the app
function saveState() {
  var budget_year = document.querySelector('select[name="budget_year_selector"]');
  localStorage.setItem('budget_year', budget_year.value);

  var summary_year = document.querySelector('select[name="summary_year_selector"]');
  localStorage.setItem('summary_year', summary_year.value);

  var statement_year = document.querySelector('select[name="statement_year_selector"]');
  localStorage.setItem('statement_year', statement_year.value);

  var statement_month = document.querySelector('select[name="statement_month_selector"]');
  localStorage.setItem('statement_month', statement_month.value);
} 

// Code to set value of the alternate year and month selector
// Used to allow multiple forms to access the month/year
function setAlts() {
  setAlt('budget_year_selector');
  setAlt('statement_year_selector');
  setAlt('statement_month_selector');
  setAlt('statement_year_selector_alt');
  setAlt('statement_month_selector_alt');
}
function setAlt(selectorName) {
  var selector = document.getElementsByName(selectorName)[0];
  var selectorAlt = document.getElementsByName(selectorName + "_alt")[0];
  var newOption = document.createElement("option");
  newOption.value = selector.value;
  newOption.text = selector.value;
  selectorAlt.add(newOption);
  selectorAlt.value = selector.value;
}

// Code to hide overlaid forms
function hideForms() {
  var elements = document.querySelectorAll('.util_page');
  for (var i = 0; i < elements.length; i++) {
      elements[i].classList.remove('visible');
  }
  document.getElementById("page").classList.remove('overlay');
}

// Code to hide forms on escape key press
document.addEventListener('keydown', function(event) {
  if (event.key === 'Escape') {
      hideForms();
  }
});

//Code to close the app
function quit() {
  var quit = document.getElementById('quit');
  quit.value = "1";
  quit.parentElement.submit();
}

//Code to check if the app is closed
function isServerDisconnected(waitTime) {
  setTimeout(function() {
    let xhr = new XMLHttpRequest();
    xhr.open('HEAD', '/', true);
    xhr.onreadystatechange = function() {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 0) {
          console.log('Server connection is severed');
          showForm('logged_out');
          return true;
        } else {
          console.log('Server connection is active');
          return false;
        }
      }
    };
    xhr.send();
  }, waitTime * 1000);
}