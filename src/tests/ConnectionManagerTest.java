package tests;

import main.ConnectionManager;
import main.Credentials;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
// This class encapsulates all the tests focused on initialization.
public class ConnectionManagerTest {

    //This test verifies that we can get a proper ConnectionManager instance, which means we have successfully authenticated
    //the user, and we did not get any exceptions.
    @Test
    public void testBasicConnectivity() {
        File fileContainingTestResources = new File("./src/tests/TestResources.txt");
        try {
            // Here we read some testing parameters such as input values and expected values from a simple .txt file.
            Scanner inputTestResources = new Scanner(fileContainingTestResources);
            String testSubdomain = inputTestResources.nextLine();
            String testAuthCredentials = inputTestResources.nextLine();
            ConnectionManager cm = new ConnectionManager(new Credentials(testSubdomain, testAuthCredentials));
            //If ConnectionManager checkCredentials is successful, it will return a valid JsonNode.
            //If it is NOT successful, it will return null and assert.
            assertNotNull(cm.checkCredentials());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
