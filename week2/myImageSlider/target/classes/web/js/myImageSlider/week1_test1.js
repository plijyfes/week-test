var timer;
var target = 0;
var scrollDiv = document.getElementById('scrollDiv');

document.addEventListener('DOMContentLoaded', function (event) {
    document.getElementById('leftButton').addEventListener('click', scroll);
    document.getElementsByClassName('rightButton')[0].addEventListener('click', scroll);
});

function scroll() {
	consloe.log('ccc');
    if (timer) {
        clearInterval(timer);
    }
    if (this.className == 'leftButton') {
        if (target >= 200) {
            target -= 200;
        }
        if (scrollDiv.scrollLeft > 0) {
            timer = setInterval(function () {
                if (scrollDiv.scrollLeft > target) { scrollDiv.scrollLeft -= 4; } else { clearInterval(timer); }
            }, 10);
        }
    } else if (this.className == 'rightButton') {
        if (target <= 200) {
            target += 200;
        }
        if (scrollDiv.scrollLeft < 400) {
            timer = setInterval(function () {
                if (scrollDiv.scrollLeft < target) { scrollDiv.scrollLeft += 4; } else { clearInterval(timer); }
            }, 10);
        }
    }

}
