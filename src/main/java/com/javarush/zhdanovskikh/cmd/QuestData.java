package com.javarush.zhdanovskikh.cmd;

import lombok.Data;

@Data
public class QuestData {
    private String stepDescription;
    private int btn1Result;
    private int btn2Result;
    private String btn1Text;
    private String btn2Text;

    @Override
    public String toString() {
        return String.format("%s %s %s %d %d", getStepDescription(), getBtn1Text(), getBtn2Text(), getBtn1Result(), getBtn2Result());
    }
}
