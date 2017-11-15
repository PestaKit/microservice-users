/*
package io.pestakit.users.api.spec.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.pestakit.users.ApiException;
import io.pestakit.users.ApiResponse;
import io.pestakit.users.api.DefaultApi;
import io.pestakit.users.api.dto.User;
import io.pestakit.users.api.spec.helpers.Environment;

import static org.junit.Assert.assertNotNull;

public class UserCreation {

    private Environment environment;
    private DefaultApi api;

    User user;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;


    UserCreation(Environment environment){
        this.environment = environment;
        api = environment.getApi();
    }

    @Given("^I have a user payload$")
    public void iHaveAUserPayload() throws Throwable {
        user = new User();
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
}
*/
