<%--
	Here you could do any styling job you want , all CSS stuff.
--%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

body{
	background-color:black;
}

.z-imageslider{
    color:white;
    width: 680px;
    height: 200px;
    position: fixed;
    top: 50%;
    left: 50%;
    margin-top: -100px;
    margin-left: -340px;
    overflow: hidden;
}

.z-imageslider-content {
    width: 1000px;
    height: 200px;
    float: left;
}

#z-imageslider-scrollDiv {
    height: 200px;
    overflow: hidden;
    float: left;
}

.z-image {
    width: 200px;
    height: 200px;
    float: left;
}

#z-imageslider-leftButton {
    width: 40px;
    height: 40px;
    margin-top: 80px;
    float: left;
}

#z-imageslider-rightButton {
    width: 40px;
    height: 40px;
    margin-top: 80px;
    float: left;
}