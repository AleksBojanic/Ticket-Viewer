package main;

import main.ConnectionManager;

public class Main {
    public static void main(String args[]) {
        MenuController menuController = new MenuController();

        ConnectionManager connectionManager = menuController.runAuthMenu();

        menuController.runMainMenu(connectionManager);
    }
}
