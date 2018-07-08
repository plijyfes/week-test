<%--
	Here you could do any styling job you want , all CSS stuff.
--%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

.z-imageSlider{
     color:black;
}

body {
    background-color: black;
}

#container div {
    float: left;
}

#container {
    width: 680px;
    height: 200px;
    position: fixed;
    top: 50%;
    left: 50%;
    margin-top: -100px;
    margin-left: -340px;
}

#content {
    width: 1000px;
    height: 200px;
}

#scrollDiv {
    width: 600px;
    overflow: hidden;
}

#content>.img {
    width: 200px;
    height: 200px;
}

.img1 {
    background-image: url("../test_img/ironman-01.jpg");
}

.img2 {
    background-image: url("../test_img/ironman-02.jpg");
}

.img3 {
    background-image: url("../test_img/ironman-03.jpg");
}

.img4 {
    background-image: url("../test_img/ironman-04.jpg");
}

.img5 {
    background-image: url("../test_img/ironman-05.jpg");
}

#leftButton {
    background-image: url("../test_img/40_40_left_wb.PNG");
    width: 40px;
    height: 40px;
    margin-top: 80px;
}

#rightButton {
    background-image: url("../test_img/40_40_right_wb.PNG");
    width: 40px;
    height: 40px;
    margin-top: 80px;
}