package com.javarush.zhdanovskikh.cmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Quest implements Command{
    private int step=0;
    private int maxStep=Integer.MAX_VALUE;
    private boolean questFailed=false;
    private int btn1Result;
    private int btn2Result;

    @Override
    public String doGet(HttpServletRequest req) {
        QuestData questData = new QuestData();
        String clientIp = req.getRemoteAddr();
        if (questFailed ) {
            questData.setBtn1Result(0);
            questData.setBtn2Result(0);
            questData.setStepDescription(String.format("Тест пройден НЕУСПЕШНО. Правильных шагов: %d  Всего шагов: %d"+
                    "<br> Client IP: %s", step, maxStep, clientIp));
            req.setAttribute("questData",questData);
            questFailed=false;
            step=0;
        } else {
            if (getStep() < maxStep) {
                req.setAttribute("questData",prepareQuest(req, step));
            } else {
                questData.setStepDescription(String.format("Тест пройден УСПЕШНО. Правильных шагов: %d  Всего шагов: %d"+
                        "<br> Client IP: %s", step, maxStep, clientIp));
                req.setAttribute("questData",questData);
                step=0;
            }
        }


        return getView();
    }

    @Override
    public String doPost(HttpServletRequest req) {
        String button1 = req.getParameter("Btn1");
        String button2 = req.getParameter("Btn2");
        if ((button1 == null && getBtn1Result()>0) || (button2 == null && getBtn2Result()>0))  {
            setQuestFailed();
        } else {incStep();};
        return getView();
    }


    public QuestData prepareQuest(HttpServletRequest request, int step){
        //load quest from json
        ServletContext context = request.getServletContext();
        QuestData result = new QuestData();
        String rp="";
        rp = context.getRealPath("/");
        File prevFile1 = new File(rp);
        String prevDirectory1 = prevFile1.getParent();
        File prevFile2 = new File(prevDirectory1);
        String prevDirectory2 = prevFile2.getParent();
        final String QUEST_JSON = prevDirectory2.concat("/src/main/resources/zhdanovskikh/quest.json");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            QuestData[] quests = objectMapper.readValue(Paths.get(QUEST_JSON).toFile(), QuestData[].class);
            setMaxStep(quests.length);
            setBtn1Result(quests[step].getBtn1Result());
            setBtn2Result(quests[step].getBtn2Result());
            result = quests[step];
        } catch (RuntimeException | IOException e) {
            result.setStepDescription("Cannot read config "+ rp);
            result.setBtn1Text("Error");
            result.setBtn2Text("Error");
            result.setBtn1Result(0);
            result.setBtn2Result(0);
        }
        return result;
    }

    public int getStep(){return this.step;}
    public void incStep(){if (this.step < getMaxStep()) this.step = step+1;}

    public int getMaxStep(){return this.maxStep;}
    public void setMaxStep(int value){this.maxStep = value;}
    public boolean getQuestFailed(){return this.questFailed;}
    public void setQuestFailed(){this.questFailed = true;}
    public int getBtn1Result(){return this.btn1Result;}
    public void setBtn1Result(int value){this.btn1Result = value;}
    public int getBtn2Result(){return this.btn2Result;}
    public void setBtn2Result(int value){this.btn2Result = value;}


}
