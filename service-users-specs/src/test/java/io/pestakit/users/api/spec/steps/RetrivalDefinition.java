package api;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RetrivalDefinition
{
    //private Environment environment;
    //private DefaultApi api;

    private static final String APPLICATION_JSON = "application/json";

    private final WireMockServer wireMockServer = new WireMockServer();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();


   // User user;

    private int id;
    private HttpResponse response;

   /* private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;
    */

    @Given("^There is an API server$")
    public void thereIsAFruitsServer() throws Throwable
    {
        assertNotNull(wireMockServer);
    }

    @Given("^I have an user with id=(\\d+)$")
    public void iHaveAnUserWithId(int id) throws Throwable
    {
        this.id = id;
    }

    @When("^I POST it to the /user endpoint$")
    public void iPOSTItToTheUserEndpoint() throws Throwable
    {
        wireMockServer.start();

        configureFor("localhost", 8080);

        if(id == 999)
        {
            stubFor(post(urlEqualTo("/user")).willReturn(aResponse().withStatus(404)));
        }
        else
        {
            stubFor(post(urlEqualTo("/user")).willReturn(aResponse().withStatus(200)));
        }

        HttpPost request = new HttpPost("http://localhost:8080/user");

        request.addHeader("Content-Type", APPLICATION_JSON);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("id", "" + id));
        nvps.add(new BasicNameValuePair("password", "secret"));

        request.setEntity(new UrlEncodedFormEntity(nvps));

        response = httpClient.execute(request);

        wireMockServer.stop();
    }

    @Then("^I receive a (\\d+) status code$")
    public void iReceiveAStatusCode(int code) throws Throwable
    {
        assertEquals(code, response.getStatusLine().getStatusCode());
    }
}