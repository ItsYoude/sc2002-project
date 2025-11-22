import controller.*;

/**
 * The {@code MainApp} class serves as the entry point of the application.
 * It initializes the system controller and begins the login UI workflow.
 */







public class MainApp {
        /**
        * The main method that starts the application.
        * <p>
        * This method performs the following steps:
        * <ul>
        *   <li>Creates a {@link SystemController} instance</li>
        *   <li>Prints a start-up message for the Login UI</li>
        *   <li>Initializes the system via {@code initializeSystem()}</li>
        *   <li>Starts the system via {@code startSystem()}</li>
        * </ul>
        *
        * @param args Command-line arguments (not used in this application)
        */
    
        public static void main(String[] args) {
        //Initialize controllers
        SystemController systemController = new SystemController();

        System.out.println("============Staring Login UI==========");
        systemController.initializeSystem();
        systemController.startSystem();

    

    }
}