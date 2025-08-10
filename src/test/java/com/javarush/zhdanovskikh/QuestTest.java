package com.javarush.zhdanovskikh;
import com.javarush.zhdanovskikh.cmd.Quest;
import com.javarush.zhdanovskikh.cmd.QuestData;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuestTest {
    @Test
    public void positiveNumberOfSteps() {
        Quest quest = new Quest();
        int numberOfSteps = quest.getMaxStep();
        assertTrue(numberOfSteps > 0);
    }

    @Test
    public void currentStepExists() {
        Quest quest = new Quest();
        int step = quest.getStep();
        assertTrue(step >=0);
    }

    @Test
    void questBtn1() {
        //Test for Button1 on jsp
        HttpServletRequest request;
        request=mock(HttpServletRequest.class);
        QuestData testQuestData = new QuestData();
        Quest quest = new Quest();
        testQuestData.setBtn1Result(0);
        testQuestData.setBtn2Result(1);

        when (request.getParameter("Btn1")).thenReturn(null);
        when (request.getParameter("Btn2")).thenReturn("Choice 2");

        quest.doPost(request);
        assertEquals(testQuestData.getBtn1Result(), quest.getBtn1Result());
    }

    @Test
    void questBtn2() {
        //Test for Button2 on jsp
        HttpServletRequest request;
        request=mock(HttpServletRequest.class);
        QuestData testQuestData = new QuestData();
        Quest quest = new Quest();
        testQuestData.setBtn1Result(1);
        testQuestData.setBtn2Result(0);
        when (request.getParameter("Btn1")).thenReturn("Choice 1");
        when (request.getParameter("Btn2")).thenReturn(null);

        quest.doPost(request);
        assertEquals(0, testQuestData.getBtn2Result());
    }

    @Test
    void questBtnsBadStatusAllEmpty() {
        HttpServletRequest request;
        Quest quest = new Quest();
        request=mock(HttpServletRequest.class);
        when (request.getParameter("Btn1")).thenReturn(null);
        when (request.getParameter("Btn2")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> quest.doPost(request));
    }

    @Test
    void questBtnsBadStatusAllFill() {
        HttpServletRequest request;
        Quest quest = new Quest();
        quest.setBtn1Result(1);
        quest.setBtn2Result(0);
        request=mock(HttpServletRequest.class);
        when (request.getParameter("Btn1")).thenReturn("Choice 1");
        when (request.getParameter("Btn2")).thenReturn("Choice 2");

        assertThrows(RuntimeException.class, () -> quest.doPost(request));
    }


}
