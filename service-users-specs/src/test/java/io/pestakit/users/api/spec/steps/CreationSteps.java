package io.pestakit.users.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.pestakit.users.ApiException;
import io.pestakit.users.ApiResponse;
import io.pestakit.users.api.DefaultApi;
import io.pestakit.users.api.dto.Fruit;
import io.pestakit.users.api.dto.User;
import io.pestakit.users.api.spec.helpers.Environment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Olivier Liechti on 27/07/17.
 */
public class CreationSteps {

    protected Environment environment;
    protected DefaultApi api;

    Fruit fruit;
    User user;

    protected ApiResponse lastApiResponse;
    protected ApiException lastApiException;
    protected boolean lastApiCallThrewException;
    protected int lastStatusCode;

    public CreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("^there is a Fruits server$")
    public void there_is_a_Fruits_server() throws Throwable {
        assertNotNull(api);
    }

    @Given("^I have a fruit payload$")
    public void i_have_a_fruit_payload() throws Throwable {
        fruit = new io.pestakit.users.api.dto.Fruit();
    }

    @When("^I POST it to the /fruits endpoint$")
    public void i_POST_it_to_the_fruits_endpoint() throws Throwable {
        try {
            lastApiResponse = api.createFruitWithHttpInfo(fruit);
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
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(arg1, lastStatusCode);
    }

    @Given("^I have a user payload$")
    public void iHaveAUserPayload() throws Throwable {
        iHaveAUserNamedPayload("john");
    }

    @Given("^there is a User and Team Server$")
    public void thereIsAUserAndTeamServer() throws Throwable {
        assertNotNull(api);
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

    @Given("^I have a user named (\\w+) payload$")
    public void iHaveAUserNamedPayload(String name) throws Throwable {
        user = new User();
        user.username("user"+name);
        user.password("pass"+name);
        user.setEmail(name+"@"+name+".com");
        user.setFirstName("first"+name);
        user.setLastName("last"+name);
        user.setDisplayName("display"+name);
    }

    @Given("^the DB is reseted$")
    public void theDBIsReseted() throws Throwable {

    }
}
