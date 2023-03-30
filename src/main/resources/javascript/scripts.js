function showContent(id) {
    var contents = document.querySelectorAll('.content');
    for (var i = 0; i < contents.length; i++) {
        contents[i].classList.remove('active');
    }
    var content = document.getElementById(id);
    content.classList.add('active');
}