import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Scanner;

public class MenuController {
    public static int QUIT = 0;
    public static int REPEAT = 1;
    public static int BACK = -1;
    private Scanner keyboard = new Scanner(System.in);

    public MenuController() {
    }

    //This method runs the initial collection of credentials needed in order to connect to the Zendesk server.
    public ConnectionManager runAuthMenu() {
        System.out.println("In order to interact with the Zendesk server, we need some of your information.\n");
        System.out.println("Please provide your subdomain:");
        String subdomain = keyboard.nextLine();
        System.out.println("Please enter your username (hint: your username should be the email address you registered with):");
        String username = keyboard.nextLine();
        System.out.println("Please enter your password:");
        String pass = keyboard.nextLine();
        String authString = (new StringBuilder())
                .append(username)
                .append(":")
                .append(pass)
                .toString();

        ConnectionManager cm = new ConnectionManager(new Credentials(subdomain, authString));
        if (cm.checkCredentials()!= null) {
            System.out.println("Success! You now have access to the Ticket Viewer");
        }
        else {
            System.out.println("Sorry, these are not valid credentials, or we may have trouble communicating with the server ");
            System.out.println("Please verify your credentials and try again");
            System.exit(-1);
        }
        return(cm);
    }

    public int runSingleTicketMenu(ConnectionManager cm) {
        System.out.println("Please provide the ticket id (hint: field name is \"id\"):");
        String id = keyboard.nextLine();
        try {
            int idNumber = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            System.out.println("This does not seem to be a valid ticket ID. Please try again.\n");
            return MenuController.REPEAT;
        }

        JsonNode result = cm.getTicket(id);
        prettyPrint(result);
        return MenuController.REPEAT;
    }

    //This method provides logic for the paging menu that displays lists of tickets
    public int runPagingTicketListMenu(ConnectionManager cm) {
        try {
            JsonNode result = cm.getTickets();
            prettyPrint(result);
            while (true) {
                System.out.println("We have displayed all the tickets on this page for you.");
                System.out.println("Press 1 to view the next page of tickets:");
                System.out.println("Press 2 to view the previous page of tickets:");
                System.out.println("Type 'menu' to return back to the main menu");
                System.out.println("Type 'quit' to exit");
                String choice = keyboard.nextLine();
                JsonNode checkResult;
                switch (choice) {
                    case "1":
                        checkResult = cm.getNextTickets(result);
                        //if we went 'over the edge' in paging, the links will be empty.
                        //we ignore the request in that case and move on
                        if(isValidListJSON(checkResult)) {
                            result=checkResult;
                            prettyPrint(result);
                        }
                        else {
                            System.out.println("There is no more tickets in this direction.");
                        }
                        break;
                    case "2":
                        checkResult = cm.getPrevTickets(result);
                        //if we went 'over the edge' in paging, the links will be empty.
                        //we ignore the request in that case and move on
                        //if we went 'over the edge' in paging, the links will be empty.
                        //we ignore the request in that case and move on
                        if(isValidListJSON(checkResult)) {
                            result=checkResult;
                            prettyPrint(result);
                        }
                        else {
                            System.out.println("There is no more tickets in this direction.");
                        }
                        break;
                    case "menu":
                        return (MenuController.BACK);
                    case "quit":
                        return (MenuController.QUIT);
                    default:
                        System.out.println("Sorry, that does not seem to be a valid command. Could you please try again?");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return MenuController.QUIT;
        }
    }

    private boolean isValidListJSON(JsonNode checkResult) {
        Boolean test = checkResult.path("links").path("prev").isNull();
        return (!test);
    }

    //This method provides logic for the paging menu that displays lists of tickets
    public int runMainMenu(ConnectionManager cm) {
        Integer repeatOrNot = MenuController.REPEAT;
        try {
            System.out.println("Welcome to Zendesk's Coding Challenge, the ticket viewer.");
            while (repeatOrNot != MenuController.QUIT) {
                System.out.println("Please select one of the following options:");
                System.out.println("Press 1 to view all tickets");
                System.out.println("Press 2 to view an individual ticket");
                System.out.println("Type 'quit' to exit");
                String choice = keyboard.nextLine();

                switch (choice) {
                    case "1":
                        repeatOrNot = runPagingTicketListMenu(cm);
                        break;
                    case "2":
                        repeatOrNot = runSingleTicketMenu(cm);
                        break;
                    case "quit":
                        return (MenuController.QUIT);
                    default:
                        System.out.println("Sorry, that does not seem to be a valid command. Could you please try again?");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return MenuController.QUIT;
        }
        return repeatOrNot;
    }



    //This is a utility method, created to make the JSON data easier to read/understand.
    private void prettyPrint(JsonNode node){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            System.out.println(indented);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

