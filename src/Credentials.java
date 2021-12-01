public class Credentials {
private String subdomain;
private String authCredentials;

public String getSubdomain() {
   return subdomain;
}

public String getAuthCredentials() {
    return authCredentials;
}
//I don't want Credentials to be created without any parameters, so this is private.
private Credentials() {
}

public Credentials(String mySubdomain, String myCredentials) {
    subdomain = mySubdomain;
    authCredentials = myCredentials;
}
}

