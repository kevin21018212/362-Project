package Interfaces;

import users.Advisor;

public interface AdvisorInterface {
    void populateData(String id);
    void printSchedule();
    Advisor getAdvisor(String id);
}
