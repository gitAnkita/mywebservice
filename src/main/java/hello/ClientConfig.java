package hello;

import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

@ComponentScan("hello.common")
@Configuration
public class ClientConfig {
    @Bean
    Jaxb2Marshaller jaxb2Marshaller() throws Exception{
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("io.spring.guides.gs_producing_web_service");
        jaxb2Marshaller.afterPropertiesSet();
        //jaxb2Marshaller.setPackagesToScan(ClassUtils.getPackageName(GetCountryRequest.class));
        return jaxb2Marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() throws Exception{
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(jaxb2Marshaller());
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
        webServiceTemplate.setDefaultUri("http://localhost:8080/ws");
        return webServiceTemplate;
    }
}
