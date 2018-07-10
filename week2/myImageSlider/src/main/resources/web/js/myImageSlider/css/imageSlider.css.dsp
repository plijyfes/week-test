<%--
	Here you could do any styling job you want , all CSS stuff.
--%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

.z-imageslider{
    color:black;
    height: 200px;
    margin: 0 auto;
    overflow: hidden;
}

.z-imageslider-content {
    height: 200px;
    float: left;
}

.z-imageslider-scroll-div {
    height: 200px;
    overflow: hidden;
    float: left;
}

.z-image {
    width: 100%;
    height: 100%;
}

.z-imageslider-image{
	width: 200px;
    height: 200px;
    float: left;
}

.z-imageslider-image-selected{
	width: 200px;
    height: 200px;
    float: left;
    border: 2px green solid;
}

.z-imageslider-left-button {
	background-image: url(${c:encodeURL('/test_img/40_40_left_wb.PNG')});
    width: 40px;
    height: 40px;
    margin-top: 80px;
    float: left;
}

.z-imageslider-right-button {
	background-image: url(${c:encodeURL('/test_img/40_40_right_wb.PNG')});
    width: 40px;
    height: 40px;
    margin-top: 80px;
    float: left;
}