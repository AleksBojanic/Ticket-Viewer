package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Base64;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import javax.security.sasl.AuthenticationException;

public class ConnectionManager {
    // These are the starting points for URLs to get a ticket list, and individual tickets.
    private String baseTicketListURL = ".zendesk.com/api/v2/tickets.json?page[size]=";
    private String baseTicketURL = ".zendesk.com/api/v2/tickets/";
    private String baseTestURL = ".zendesk.com/api/v2/users/me.json";
    private int pageSize = 25;

    // These are the starting points for URLs to get a ticket list, and individual tickets.
    private String ticketListURL;
    private String ticketURL;
    private String testURL;
    // We need to remember Base64Encoded password credential to add to header later.
    private String passwordCredentials;

    //I don't want main.ConnectionManager to be created without any parameters, so this is private.
    private ConnectionManager() {
    }
    //To create a functional main.ConnectionManager, you need to pass valid credentials.
    public ConnectionManager(Credentials myCredentials) {
        //This builds the URLs for calls to get the list and to get an individual ticket
        ticketListURL = (new StringBuilder())
            .append("https://")
            .append(myCredentials.getSubdomain())
            .append(baseTicketListURL)
            .append(pageSize)
            .toString();
        ticketURL = (new StringBuilder())
                .append("https://")
                .append(myCredentials.getSubdomain())
                .append(baseTicketURL)
                .toString();
        byte[] authEncBytes = Base64.getEncoder().encode(myCredentials.getAuthCredentials().getBytes());
        passwordCredentials = new String(authEncBytes);
        testURL = (new StringBuilder())
                .append("https://")
                .append(myCredentials.getSubdomain())
                .append(baseTestURL)
                .toString();
    }

    //This method makes a REST call, adds the credentials and returns the JsonNode.
    // It is not intended to be used outside of this class, hence it is private.
    private JsonNode makeRESTCall(String urlString) {
        int status = 0;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + passwordCredentials);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] responseCharArray = new char[1024];
            StringBuffer responseBuffer = new StringBuffer();
            while ((numCharsRead = isr.read(responseCharArray)) > 0) {
                responseBuffer.append(responseCharArray, 0, numCharsRead);
            }

            String result = responseBuffer.toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnknownHostException | FileNotFoundException e) {
            if (hasInternetConnectivity()) {
                System.out.println("We cannot find any information associated with the data you entered.");
                System.out.println("Could you please verify the information you entered, and then try again?\n");
            } else {
                System.out.println("It seems that you do not have valid internet connectivity.");
                System.out.println("Could you please verify this, then try again?");
            }
        } catch (IOException e) {
            if(e.getMessage().contains("401")) {
                System.out.println("The server could not authorize you. Typically this happens when your username or password are incorrect");
                System.out.println("Could you please verify your credentials and try again?");
            } else {
                System.out.println("It seems an unexpected error has occurred while communicating with the server.");
                System.out.println("Please review the error code below for possible cause, and try again.");
                e.printStackTrace();
            }
        }
            return null;
    }
    //simple call (we are using get jobs to verify credentials
    public JsonNode checkCredentials() {
        return makeRESTCall(ticketListURL);
    }

    //calls that are handling ticket list requests
    public JsonNode getTickets() {
        return makeRESTCall(ticketListURL);
    }
    //paging requests for next ticket
    public JsonNode getNextTickets(JsonNode node) {

        return makeRESTCall(node.path("links").path("next").getTextValue());
    }
    //paging requests for previous ticket
    public JsonNode getPrevTickets(JsonNode node) {

        return makeRESTCall(node.path("links").path("prev").getTextValue());
    }
    public JsonNode getTicket(String ticketID) {
        String singleTicketURL = (new StringBuilder())
                .append(ticketURL)
                .append(ticketID)
                .append(".json")
                .toString();
        return makeRESTCall(singleTicketURL);
    }
    private boolean hasInternetConnectivity() {
        boolean result = false;
        try {
            Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
            int x = process.waitFor();
            result = (x == 0);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return (result);
    }
}


