var imgSize = document.getElementById('1').offsetWidth,
    dragging, //拖曳中
    startDiv, //起始元素
    startMPos, //起始鼠標
    startRMP, //起始鼠標相對起始元素內座標
    green = document.getElementById('green'); //綠框

document.addEventListener('DOMContentLoaded', function (event) {
    var dragSource = document.getElementById('blockContainer').children;
    for (var i = 0; i < dragSource.length; i++) {
        dragSource[i].addEventListener('mousedown', dragStart);
    }
    document.addEventListener('mousemove', move);
    green.addEventListener('mouseup', dropped);
});

function dragStart(e) {
    pauseEvent(e);
    startDiv = this;
    startMPos = [e.clientX, e.clientY];
    startRMP = [e.clientX - this.offsetLeft, e.clientY - this.offsetTop];
    dragging = true;
    green.style.left = this.offsetLeft + 'px';
    green.style.top = this.offsetTop + 'px';
    green.style.visibility = 'visible';
}

function dropped(e) {
    pauseEvent(e);
    var blockContainer = document.getElementById('blockContainer');
    var endMPos = [e.clientX, e.clientY];
    if (isInPuzzle(endMPos)) { //判斷是否在拼圖內
        var endDivId = (Math.floor((endMPos[0] - blockContainer.offsetLeft) / imgSize) + 1) + Math.floor((endMPos[1] - blockContainer.offsetTop) / imgSize) * 3;
        var endDiv = document.getElementById(endDivId);
        if (isNeighbour(endDiv)) { //判斷相鄰
            if (isHalf(endMPos)) { //判斷過半
                change(endDiv);
            }
        }
    }
    dragging = false;
    green.style.visibility = 'hidden';
}

function move(e) {
    if (dragging) {
        green.style.left = (e.clientX - startRMP[0]) + 'px';
        green.style.top = (e.clientY - startRMP[1]) + 'px';
    }
}

function change(end) {
    var endDivClass = end.className;
    end.className = startDiv.className;
    startDiv.className = endDivClass;
}

function isInPuzzle(endMPos) {
    if (endMPos[0] > blockContainer.offsetLeft && endMPos[0] < blockContainer.offsetLeft + blockContainer.offsetWidth && endMPos[1] > blockContainer.offsetTop && endMPos[1] < blockContainer.offsetTop + blockContainer.offsetHeight) {
        return true;
    } else {
        return false;
    }
}

function isNeighbour(endDiv) {
    if (Math.abs(startDiv.offsetLeft - endDiv.offsetLeft) + Math.abs(startDiv.offsetTop - endDiv.offsetTop) == imgSize) {
        return true;
    } else {
        return false;
    }
}

function isHalf(endMPos) {
    if (Math.abs(endMPos[0] - startMPos[0]) >= 0.5 * imgSize || Math.abs(endMPos[1] - startMPos[1]) >= 0.5 * imgSize) {
        return true;
    } else {
        return false;
    }
}

function pauseEvent(e) {
    e.stopPropagation();
    e.preventDefault();
}