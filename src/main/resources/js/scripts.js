  // Code for menu selector buttons
function showContent(id) {
    // Open section
    var contents = document.querySelectorAll('.active');
    for (var i = 0; i < contents.length; i++) {
        contents[i].classList.remove('active');
    }

    document.getElementById(id).classList.add('active');
    document.getElementById("btn_" + id).classList.add('active');
}

function tempChangeText(button, text) {
    curr_text = button.innerHTML;
    button.innerHTML = text;
    button.classList.add('clicked');
    setTimeout(function() {
      button.innerHTML = curr_text;
      button.classList.remove('clicked');
    }, 2000);
  }