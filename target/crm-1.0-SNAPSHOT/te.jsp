<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/22
  Time: 10:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    $.ajax({
        url : "",
        data : {

        },
        type : "",
        dataType : "json",
        success : function () {

        }
    })

    $(".time").datetimepicker({
    minView: "month",
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    autoclose: true,
    todayBtn: true,
    pickerPosition: "bottom-left"
    });

</body>
</html>
