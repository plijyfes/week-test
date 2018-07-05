var dragSource = document.getElementsByClassName('img');
var align = [document.getElementById('1').offsetLeft, document.getElementById('1').offsetTop]; //置中位移量
var endDivClass; //交換
var startDiv; //起始元素
var startMPos; //起始鼠標
var startRMP; //起始鼠標相對起始元素內座標
var green = document.getElementsByClassName('green')[0]; //綠框

document.addEventListener('DOMContentLoaded', function (event) {
    for (var i = 0; i <= 8; i++) {
        dragSource[i].addEventListener('mousedown', dragStart);
        dragSource[i].addEventListener('mouseup', dropped)
    }
});

function dragStart(e) {
    pauseEvent(e);
    startDiv = this;
    startMPos = [e.clientX, e.clientY];
    startRMP = [e.clientX - this.offsetLeft, e.clientY - this.offsetTop];
    green.style.left = this.offsetLeft + 'px';
    green.style.top = this.offsetTop + 'px';
    green.style.visibility = 'visible';
    document.addEventListener('mousemove', move);
    // console.log(this)
    green.addEventListener('mouseup', dropped)
}

function dropped(e) {
    pauseEvent(e);
    var endMPos = [e.clientX, e.clientY];
    if (endMPos[0] > align[0] && endMPos[0] < align[0] + 450 && endMPos[1] > align[1] && endMPos[1] < align[1] + 450) { //判斷是否在拼圖內
        var endDivId = (Math.floor((endMPos[0] - align[0]) / 150) + 1) + Math.floor((endMPos[1] - align[1]) / 150) * 3;
        var endDiv = document.getElementById(endDivId);
        if (Math.abs(startDiv.offsetLeft - endDiv.offsetLeft) + Math.abs(startDiv.offsetTop - endDiv.offsetTop) == 150) { //判斷相鄰
            if (Math.abs(endMPos[0] - startMPos[0]) >= 75 || Math.abs(endMPos[1] - startMPos[1]) >= 75) { //判斷過半
                change(endDiv);
            }
        }
    }
    document.removeEventListener('mousemove', move);
    green.style.visibility = 'hidden';
}

function move(e) {
    green.style.left = (e.clientX - startRMP[0]) + 'px';
    green.style.top = (e.clientY - startRMP[1]) + 'px';
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