package com.javarush.zhdanovskikh.cmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Quest implements Command{

    @Getter
    private int step=0;
    @Setter
    @Getter
    private int maxStep=Integer.MAX_VALUE;
    @Getter
    @Setter
    private boolean questFailed=false;
    @Setter
    @Getter
    private int btn1Result;
    @Setter
    @Getter
    private int btn2Result;

    final int VALID=1;
    final int INVALID=0;

    @Override
    public String doGet(HttpServletRequest req) {
        QuestData questData = new QuestData();
        String clientIp = req.getRemoteAddr();
        if (questFailed ) {
            questData.setBtn1Result(INVALID);
            questData.setBtn2Result(INVALID);
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

        //throw new RuntimeException("Bad Response");

        if ((button1 != null && button2 != null) || (button1 == null && button2 == null))  {
            //raise error
            throw new RuntimeException("Bad Response");
        } else {
            if ((button1 == null && getBtn1Result() ==VALID ) || (button2 == null && getBtn2Result() ==VALID)) {
                setQuestFailed();
            } else {
                incStep();
            }
        }
        return getView();
    }


    public QuestData prepareQuest(HttpServletRequest request, int step){
        //load quest from json
        ServletContext context = request.getServletContext();
        QuestData result = new QuestData();
        String rp;
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
            result.setBtn1Result(INVALID);
            result.setBtn2Result(INVALID);
        }
        return result;
    }

    public void incStep(){if (this.step < getMaxStep()) this.step = step+1;}

    public void setQuestFailed(){this.questFailed = true;}


}
