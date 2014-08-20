package qu.scholar.service;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.jackson.JacksonFeature;


@javax.ws.rs.ApplicationPath("/rs")
public class ApplicationConfig extends Application { 
    @Override
    public Set<Class<?>> getClasses() {
        System.out.println("In ApplicationConfig: registering REST APIs");
    	Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
    	resources.add(JacksonFeature.class);
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * Add any resources which you need to be accessed as a REST service
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(qu.scholar.service.ScholarService.class);
        
    }
}
