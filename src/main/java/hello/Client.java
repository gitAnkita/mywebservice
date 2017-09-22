package hello;

import io.spring.guides.gs_producing_web_service.Country;
import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import io.spring.guides.gs_producing_web_service.GetCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class Client {

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public Country callService(String name){

        GetCountryRequest countryRequest = new GetCountryRequest();
        countryRequest.setName(name);
        GetCountryResponse response = (GetCountryResponse) webServiceTemplate.marshalSendAndReceive(countryRequest);
        return response.getCountry();
    }
}
