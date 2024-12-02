package main;

import Interfaces.AdvisorInterface;
import users.Advisor;
import helpers.FileUtils;

public class AdvisorController implements AdvisorInterface {
    private Advisor advisor;

    public AdvisorController(String id) {


    }

    @Override
    public Advisor getAdvisor(String id) {

    }

    @Override
    public void populateData(String id) {

    }

    @Override
    public void printSchedule() {
        advisor.printSchedule();
    }



}
