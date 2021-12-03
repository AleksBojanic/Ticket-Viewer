package main;

// This is the main class, it instantiates an instance of runMainMenu, in order to keep things simplified and easier to
// run by just having to run Main. I was trying to keep Main relatively simple, and not have any significant logic kept
// in it.
public class Main {
    public static void main(String args[]) {
        MenuController menuController = new MenuController();

        ConnectionManager connectionManager = menuController.runAuthMenu();

        menuController.runMainMenu(connectionManager);
    }
}
