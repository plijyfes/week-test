var dragSource = document.getElementsByClassName('img');
var startDiv;
var endDivClass;
var startMPos;
var green = document.getElementById('green');

document.addEventListener('DOMContentLoaded', function (event) {
    for (var i = 0; i <= 8; i++) {
        dragSource[i].addEventListener('mousedown', dragStart);
        dragSource[i].addEventListener('mouseup', dropped,true)
    }
    green.addEventListener('mouseup', www,true)
});

function dragStart(e) {
    pauseEvent(e);
    startDiv = this;
    startMPos = [e.clientX, e.clientY];
    green.style.left = this.offsetLeft + 'px';
    green.style.top = this.offsetTop + 'px';
    green.style.visibility = 'visible';
    for (var i = 0; i <= 8; i++) {
        dragSource[i].addEventListener('mousemove', move);
    }
}

function dropped(e) {
    pauseEvent(e);
    var endMPos = [e.clientX, e.clientY];
    if (Math.abs(startDiv.offsetLeft - this.offsetLeft) + Math.abs(startDiv.offsetTop - this.offsetTop) == 150) { //判斷相鄰
        if (Math.abs(endMPos[0] - startMPos[0]) >= 75 || Math.abs(endMPos[1] - startMPos[1]) >= 75) { //判斷過半
            change(this);
        }
    }
    for (var i = 0; i <= 8; i++) {
        dragSource[i].removeEventListener('mousemove', move);
    }
    green.style.visibility = 'hidden';
}

function move(e) {
    pauseEvent(e);
    green.style.left = (e.clientX-50) + 'px';
    green.style.top = (e.clientY-50) + 'px';
    console.log(green.offsetLeft);
}

function change(end) {
    endDivClass = end.className;
    end.className = startDiv.className;
    startDiv.className = endDivClass;
}

function pauseEvent(e) {
    e.stopPropagation();
    e.preventDefault();
}

function www(){
    console.log('www')
}