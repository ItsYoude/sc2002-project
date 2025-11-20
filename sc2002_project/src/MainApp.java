import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import UI.LoginUI;
import controller.*;
import models.*;
import utility.FileService;
import utility.IInternshipSorter;


public class MainApp {
    public static void main(String[] args) {
        //Initialize controllers
        SystemController systemController = new SystemController();

        System.out.println("============Staring Login UI==========");
        systemController.initializeSystem();
        systemController.startSystem();

    

    }
}