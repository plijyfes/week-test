var startDiv;
var endDivClass;
var startMRelativePos;

document.addEventListener('DOMContentLoaded', function (event) {
    var dragSource = document.getElementsByClassName('img');
    var dropTarget = document.getElementsByClassName('img');
    for (var i = 0; i <= 8; i++) {
        dragSource[i].addEventListener('dragstart', dragStart);
        dropTarget[i].addEventListener('drop', dropped);
        dropTarget[i].addEventListener('dragenter', cancelDefault)
        dropTarget[i].addEventListener('dragover', cancelDefault)
    }
});

function dragStart(e) {
    startDiv = this;
    startMRelativePos = [e.clientX - this.offsetLeft, e.clientY - this.offsetTop];
    if (navigator.userAgent.match('Firefox')) {
        e.dataTransfer.setData('text/html', ''); //firefox需要setData 'text/html'
    }
    if (navigator.userAgent.match('MSIE')) {
        e.dataTransfer.setData('text', ''); //IE需要setData 'text'
    }
}

function dropped(e) {
    var endMRelativePos = [e.clientX - this.offsetLeft, e.clientY - this.offsetTop];
    //方向判斷
    if (startDiv.offsetLeft - this.offsetLeft == -150 && startDiv.offsetTop == this.offsetTop) { //向右
        if (endMRelativePos[0] + (150 - startMRelativePos[0]) >= 75) {
            change(this);
        }
    } else if (startDiv.offsetLeft - this.offsetLeft == 150 && startDiv.offsetTop == this.offsetTop) { //向左
        if (endMRelativePos[0] - startMRelativePos[0] <= 75) {
            change(this);
        }
    } else if (startDiv.offsetTop - this.offsetTop == 150 && startDiv.offsetLeft == this.offsetLeft) { //向上
        if (endMRelativePos[1] - startMRelativePos[1] <= 75) {
            change(this);
        }
    } else if (startDiv.offsetTop - this.offsetTop == -150 && startDiv.offsetLeft == this.offsetLeft) { //向下
        if (endMRelativePos[1] + (150 - startMRelativePos[1]) >= 75) {
            change(this);
        }
    }
}

function cancelDefault(e) {
    e.preventDefault();
    e.stopPropagation();
    return false;
}

function change(end) {
    endDivClass = end.className;
    end.className = startDiv.className;
    startDiv.className = endDivClass;
}