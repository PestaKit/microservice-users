package io.pestakit.users.api.spec.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.pestakit.users.ApiException;
import io.pestakit.users.ApiResponse;
import io.pestakit.users.api.DefaultApi;
import io.pestakit.users.api.dto.Credentials;
import io.pestakit.users.api.dto.User;
import io.pestakit.users.api.spec.helpers.Environment;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UsersSteps {

    protected Environment environment;
    protected DefaultApi api;

    User user;
    Credentials cred;
    long uid;
    List<User> users;

    protected ApiResponse lastApiResponse;
    protected ApiException lastApiException;
    protected boolean lastApiCallThrewException;
    protected int lastStatusCode;

    public UsersSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("^there is a User and Team Server$")
    public void thereIsAUserAndTeamServer() throws Throwable {
        assertNotNull(api);
    }

    @When("^I GET the /users endpoint$")
    public void iGETTheUsersEndpoint() throws Throwable {
        try {
            lastApiResponse = api.getUsersWithHttpInfo();
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int code) throws Throwable {
        assertEquals(code, lastStatusCode);
    }

    @Given("^I have a user payload$")
    public void iHaveAUserPayload() throws Throwable {
        iHaveAUserNamedPayload("john");
    }

    @When("^I POST it to the /users endpoint$")
    public void iPOSTItToTheUsersEndpoint() throws Throwable {
        try {
            lastApiResponse = api.createUserWithHttpInfo(user);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Given("^I have a user named (.+) payload$")
    public void iHaveAUserNamedPayload(String name) throws Throwable {
        uid = new Date().getTime();
        createUSerPayload(name);
    }
    @Given("^I have a user named (.+) payload with same uid$")
    public void iHaveAUserNamedPayloadWithSameUid(String name) throws Throwable {
        createUSerPayload(name);
    }

    private void createUSerPayload(String name){
        name = uid + name;
        user = new User();
        user.username("user"+name);
        user.password("pass"+name);
        user.setEmail(name+"@"+name+".com");
        user.setFirstName("first"+name);
        user.setLastName("last"+name);
        user.setDisplayName("display"+name);

    }

    /* User Retrival  */

    @And ("^the response is a list containing at least (\\d+) users$")
    public void theResponseIsAListContainingUsers(int nb) throws Throwable
    {
        users = (List<User>)lastApiResponse.getData();

        assertNotNull(users);
        assert(nb <= users.size());
    }

    @When("^I GET the user with the username (\\w+)$")
    public void iGETTheUserUsername(String username) throws Throwable
    {
        try
        {
            lastApiResponse = api.getUserWithHttpInfo("user" + uid + username);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        }
        catch (ApiException e)
        {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @And("^the response contains a user$")
    public void theResponseContainsAUser() throws Throwable
    {
        user = (User) lastApiResponse.getData();

        assertNotNull(user);
    }


    @Then("^I receive an endpoint to my user payload$")
    public void iReceiveAnEndpointToMyUserPayload() throws Throwable {
        Map<String,List<String>> headers = lastApiResponse.getHeaders();

        List<String> locationList = headers.get("Location");
        if(locationList != null){
            String location = locationList.get(0);
            assertNotNull(location);
            Pattern p = Pattern.compile(".*/api/users/(\\d+)$");
            Matcher m = p.matcher(location);
            assert (m.find());

            long getId = Long.parseLong(m.group(1));
            User getUser = api.getUser_0(getId);

            Argon2 argon = Argon2Factory.create();
            assert(argon.verify(getUser.getPassword(),user.getPassword()));
            user.setPassword(getUser.getPassword());
            assertEquals(user,getUser);
        }
    }


    @When("^I POST it credential to the /login endpoint$")
    public void iPOSTItCredentialToTheLoginEndpoint() throws Throwable {
        // Write code here that turns the phrase above into concrete actions

        try {
            lastApiResponse = api.authWithHttpInfo(cred);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }


    @Given("^I have it credential payload using (.+)$")
    public void iHaveItCredentialPayloadUsing(String identifier) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        cred = new Credentials();
        if(identifier.equals("username")){
            cred.setIdentifier(user.getUsername());
        }else if(identifier.equals("email")){
            cred.setIdentifier(user.getEmail());
        }
        if(identifier.equals("wrongpassword")){
            cred.setIdentifier(user.getUsername());
            cred.setPassword("wrong");
        }else
            cred.setPassword(user.getPassword());

    }
    
        /* LOGIN */

    @Given("^a username (\\w+) with password (\\w+)$")
    public void aUsernameTestWithPasswordTest(String username, String password) throws Throwable {
        cred = new Credentials();
        cred.setIdentifier(username);
        cred.setPassword(password);
    }

    @When("^I POST the credentials to the /auth endpoint$")
    public void iPOSTTheCredentialsToTheAuthEndpoint() throws Throwable {
        try
        {
            lastApiResponse = api.authWithHttpInfo(credentials);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        }
        catch (ApiException e)
        {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @And("^the response contains a token$")
    public void theResponseContainsAToken() throws Throwable {
        token = (Token) lastApiResponse.getData();

        assertNotNull(token);
    }
}
