// Code for menu selector buttons
function showContent(id) {
    // Open section
    var contents = document.querySelectorAll('.content');
    for (var i = 0; i < contents.length; i++) {
        contents[i].classList.remove('active');
    }
    document.getElementById(id).classList.add('active');

    // Colour selected anchor
    var anchors = document.querySelectorAll(".menu_options a");
    for (var i = 0; i < anchors.length; i++) {
        anchors[i].classList.remove("btn_selected");
    }
    document.getElementById("btn_" + id).classList.add("btn_selected");
}

// Update dropdown box text to match selected option
function updateDropdown(text) {
    document.querySelector('.panel .dropbtn span').textContent = text;
}