package tests;

import main.ConnectionManager;
import main.Credentials;
import org.codehaus.jackson.JsonNode;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

// This class encapsulates all the tests focused on ticket operations.
public class RequestTicketTest {

    // This test verifies that the ID of a single ticket will match up with the expected subject of that same ticket.
    @Test
    public void testSingleTicketRequest() {
        File fileContainingTestResources = new File("./src/tests/TestResources.txt");
        try {
            // Here we read some testing parameters such as input values and expected values from a simple .txt file.
            Scanner inputTestResources = new Scanner(fileContainingTestResources);
            String testSubdomain = inputTestResources.nextLine();
            String testAuthCredentials = inputTestResources.nextLine();
            String testID = inputTestResources.nextLine();
            String testSubject = inputTestResources.nextLine();
            ConnectionManager cm = new ConnectionManager(new Credentials(testSubdomain, testAuthCredentials));
            //If ConnectionManager getTicket is successful, it will return a valid JsonNode.
            //If it is NOT successful, it will return null and assert.
            JsonNode returnTicket = cm.getTicket(testID);
            assertNotNull(returnTicket);
            //To determine a successful retrieval of the ticket, we just compare if the subject of the
            // ticket in the result is what we expected.
            String resultSubject = returnTicket.path("ticket").path("subject").toString();

            assertEquals( 0, testSubject.compareTo(resultSubject));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //This test verifies that when we request to view all tickets, we will have the correct amount of tickets per page
    //by checking if the last ticket ID matches the expected amount of tickets.
    @Test
    public void testTicketsRequest() {
        File fileContainingTestResources = new File("./src/tests/TestResources.txt");
        try {
            // Here we read some testing parameters such as input values and expected values from a simple .txt file.
            Scanner inputTestResources = new Scanner(fileContainingTestResources);
            String testSubdomain = inputTestResources.nextLine();
            String testAuthCredentials = inputTestResources.nextLine();
            //This test does not need the data from the next two lines.
            inputTestResources.nextLine();
            inputTestResources.nextLine();
            String testAmountOfTickets = inputTestResources.nextLine();
            ConnectionManager cm = new ConnectionManager(new Credentials(testSubdomain, testAuthCredentials));
            //If ConnectionManager getTicket is successful, it will return a valid JsonNode.
            //If it is NOT successful, it will return null and assert.
            JsonNode returnTickets = cm.getTickets();
            assertNotNull(returnTickets);
            //To determine a successful retrieval of the amount of tickets, we just compare if the amount of the
            // tickets retrieved matches the amount we expected.
            JsonNode ticketsArray = returnTickets.path("tickets");
            JsonNode lastTicket = ticketsArray.get(Integer.parseInt(testAmountOfTickets) - 1);
            String lastTicketID = lastTicket.path("id").toString();
            //a bit of a hack. this test assumes that the first 25 tickets will have ids from 1-25.
            // So, the last ticket should have id 25
            assertEquals(lastTicketID,testAmountOfTickets);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //This test verifies that the paging mechanism works, test is limited, and only tests the first and the second set
    // of 25 tickets.
    @Test
    public void testPagination() {
        File fileContainingTestResources = new File("./src/tests/TestResources.txt");
        try {
            // Here we read some testing parameters such as input values and expected values from a simple .txt file.
            Scanner inputTestResources = new Scanner(fileContainingTestResources);
            String testSubdomain = inputTestResources.nextLine();
            String testAuthCredentials = inputTestResources.nextLine();
            //This test does not need the data from the next two lines.
            inputTestResources.nextLine();
            inputTestResources.nextLine();
            String testAmountOfTickets = inputTestResources.nextLine();
            ConnectionManager cm = new ConnectionManager(new Credentials(testSubdomain, testAuthCredentials));
            //If ConnectionManager getTicket is successful, it will return a valid JsonNode.
            //If it is NOT successful, it will return null and assert.
            JsonNode returnTickets = cm.getTickets();
            assertNotNull(returnTickets);
            // after the first page of tickets, we ask for the next one.
            // we ignore the first 25 tickets in the result.
            returnTickets = cm.getNextTickets(returnTickets);
            assertNotNull(returnTickets);
            //To determine a successful retrieval of the amount of tickets, we just compare if the amount of the
            // tickets retrieved matches the amount we expected.
            JsonNode ticketsArray = returnTickets.path("tickets");
            JsonNode lastTicket = ticketsArray.get(Integer.parseInt(testAmountOfTickets) - 1);
            String lastTicketID = lastTicket.path("id").toString();
            //a bit of a hack. this test assumes that the second page of 25 tickets will have ids from 26-50.
            // So, the last ticket should have id 50
            assertEquals(lastTicketID,String.valueOf(2*Integer.parseInt(testAmountOfTickets)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}


