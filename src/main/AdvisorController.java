package main;

import Interfaces.AdvisorInterface;
import users.Advisor;
import helpers.FileUtils;

import java.util.List;

public class AdvisorController implements AdvisorInterface {
    private Advisor advisor;

    public AdvisorController(String id) {


    }

    @Override
    public Advisor getAdvisor(String id) {
        List<String[]> advisorData = FileUtils.readStructuredData("advisors", "advisors.txt");
        for (String[] advisor : advisorData) {
            if (advisor[0].equals(id)) {
                return new Advisor(advisor[0], advisor[1], advisor[2], advisor[3]);
            }
        }
    }

    @Override
    public void populateData(String id) {

    }

    @Override
    public void printSchedule() {
        advisor.printSchedule();
    }



}
