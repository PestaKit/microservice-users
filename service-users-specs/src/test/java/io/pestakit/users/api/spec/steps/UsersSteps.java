package io.pestakit.users.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.pestakit.users.ApiException;
import io.pestakit.users.ApiResponse;
import io.pestakit.users.api.DefaultApi;
import io.pestakit.users.api.dto.User;
import io.pestakit.users.api.spec.helpers.Environment;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UsersSteps {

    protected Environment environment;
    protected DefaultApi api;

    User user;
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

    @Then("^I receive an endpoint to the user$")
    public void iReceiveAnEndpointToTheUser() throws Throwable {
        Map<String,List<String>> headers = lastApiResponse.getHeaders();

        List<String> locationList = headers.get("Location");
        if(locationList != null){
            String location = locationList.get(0);
            assertNotNull(location);
            assert(location.matches(".*/api/users/\\d+$"));
        }
    }

    /* User Retrival  */

    @And ("^the response is a list containing (\\d+) users$")
    public void theResponseIsAListContainingUsers(int nb) throws Throwable
    {
        users = (List<User>)lastApiResponse.getData();

        assertNotNull(users);
        assertEquals(nb, users.size());
    }

    @And("^one user has the following attributes:$")
    public void oneUserHasTheFollowingAttributes(List<User> users) throws Throwable
    {
        assertNotNull(users);

        for (User user: users)
        {
            assertEquals(true, this.users.contains(user));
        }
    }

    @When("^I GET the user with the username (\\w+)$")
    public void iGETTheUserUsername(String username) throws Throwable
    {
        try
        {
            lastApiResponse = api.getUserWithHttpInfo(username);
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

    @And("^the user has the following atttributes:$")
    public void theUserHasTheFollowingAtttributes(List<User> user) throws Throwable
    {
        assertEquals(1, user.size());
        assertEquals(true, user.contains(this.user));
        assertEquals( this.user, user.get(0));
    }

}