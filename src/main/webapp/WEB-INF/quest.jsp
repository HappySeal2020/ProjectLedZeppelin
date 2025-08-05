<!DOCTYPE html>
<%@page import="com.javarush.zhdanovskikh.cmd.QuestData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="head.jsp"%>
<html>

<body onload="displayText()">
<form method="post" >
<% QuestData quest; %>
<%quest = (QuestData) request.getAttribute("questData");%>


<h1><%="Проект квест"%>

</h1>
    <table id="questTbl">
        <tr>
            <td></td>
        </tr>
        <tr>
            <td> <br> Ваши действия:</td>
        </tr>
    </table>
    <button type="submit" name="Btn1" id="Btn1" >
        Button_1
    </button>
    <button type="submit" name="Btn2" id="Btn2" >
        Button_2
    </button>
    <a href="/" id="backLnk" >Go back </a>

<script>
var isQuestFailed = false;


    function displayText(){
        let row = document.getElementById("questTbl").rows;
        let col = row[0].cells;
        col[0].innerHTML = '<%= quest.getStepDescription() %>';
        let col1 = row[1].cells;
        if('<%=quest.getBtn2Result()%>' == 0 && '<%=quest.getBtn1Result()%>' == 0){
            document.getElementById('Btn1').style.visibility = 'hidden';
            document.getElementById("Btn2").style.visibility = 'hidden';
            col1[0].innerHTML = '';
        }
        else {
            let lnk=document.getElementById('backLnk');
            //lnk.setAttribute("class", "disabled");
            document.getElementById('backLnk').style.visibility = 'hidden';
            document.getElementById('Btn1').style.visibility = 'visible';
            document.getElementById('Btn2').style.visibility = 'visible';
            document.getElementById("Btn1").innerHTML = '<%= quest.getBtn1Text() %>'
            document.getElementById("Btn2").innerHTML = '<%= quest.getBtn2Text() %>'
        }

    }
    function btn1Click(){
        let isQuestFailed = false;
        if ('<%=quest.getBtn1Result()%>' == 0) {
            isQuestFailed = true;
        }
        questResult(isQuestFailed);
    }


    function btn2Click(){
        let isQuestFailed = false;
        if ('<%=quest.getBtn2Result()%>' == 0) {
            isQuestFailed = true;
        }
        questResult(isQuestFailed);
    }

function questResult (questResult){
    document.getElementById('Btn1').style.visibility = 'hidden';
    document.getElementById('Btn2').style.visibility = 'hidden';
    let row = document.getElementById("questTbl").rows;
    let col = row[1].cells;
    if (questResult){
        col[0].innerHTML = '<br>You lose';
    } else {
        col[0].innerHTML = '<br>OK';
    }
}
</script>


</form>
</body>
</html>