package io.pestakit.users.api.spec.helpers;

import io.pestakit.users.api.DefaultApi;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Olivier Liechti on 24/06/17.
 */
public class Environment {

    private DefaultApi api = new DefaultApi();

    public Environment() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("environment.properties"));
        String url = properties.getProperty("io.pestakit.users.server.url");
        //api.getApiClient().setBasePath(url);

    }

    public DefaultApi getApi() {
        return api;
    }


}
