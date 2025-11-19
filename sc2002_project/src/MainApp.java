import java.util.List;

import UI.LoginUI;
import controller.*;
import models.CompanyRepresentative;
import models.User;

import models.*;



public class MainApp {
    public static void main(String[] args) {
        //Initialize controllers
        SystemController systemController = new SystemController();

        System.out.println("============Staring Login UI==========");
        systemController.initializeSystem();
        systemController.startSystem();
    }
}