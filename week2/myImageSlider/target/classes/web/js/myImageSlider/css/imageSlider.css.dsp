<%--
	Here you could do any styling job you want , all CSS stuff.
--%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

.z-imageslider{
    color:black;
    margin: 0 auto;
    overflow: hidden;
}

.z-imageslider-content {
    float: left;
}

.z-imageslider-scroll-div {
    overflow: hidden;
    float: left;
}

.z-image {
    width: 100%;
    height: 100%;
}

.z-imageslider-image{
    float: left;
}

.z-imageslider-image-selected{
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

.z-imageslider-left-view {
	background-image: url(${c:encodeURL('/test_img/40_40_left_wb.PNG')});
    width: 40px;
    height: 40px;
    float: left;
}

.z-imageslider-right-button {
	background-image: url(${c:encodeURL('/test_img/40_40_right_wb.PNG')});
    width: 40px;
    height: 40px;
    margin-top: 80px;
    float: left;
}

.z-imageslider-right-view {
	background-image: url(${c:encodeURL('/test_img/40_40_right_wb.PNG')});
    width: 40px;
    height: 40px;
    float: left;
}