package qu.scholar.service;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import qu.scholar.entity.Publication;
import qu.scholar.entity.RelatedTitles;

@Path("/publications")
@Stateless
public class ScholarService {

    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublicationsList(SearchParameters searchParameters) {
        
        long startTime = System.currentTimeMillis();
        //String [] test= searchParameters.getTitles();
        System.out.println("server count: "+searchParameters.getCount());
//        for (String element: test)
//        {
//          element = element.substring(1, element.length() - 1);
//
//           System.out.println("printing:"+element);
//        }
        
        System.out.println("inside getPublicationList method!");

        List<RelatedTitles> data;
        try {
            data = Publication.processRelatedTitlesRESTService(searchParameters.getTitles());
            String json = (new Gson()).toJson(data);
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Total time:"+totalTime);
            return Response.ok(json).build();
        } catch (Exception ex) {
            String msg = String.format("NO Publications found, Try different titles");
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }
       
    }
    @GET
    @Path("/{keyword}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("keyword") String keyword) {

        System.out.println("inside getPublicationsByKeyword method");
        System.out.println("keyword: " + keyword);
        //keyword="Aid";
        List<String> keywordTitles = Publication.getTitlesByKeyword(keyword.trim());
        if (keywordTitles != null) {
            String json = (new Gson()).toJson(keywordTitles);
            return Response.ok(json).build();
        } else {
            String msg = String.format("NO Publications found, Try different titles"+keyword);
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }
    }
    
    
    @GET
    @Path("/keywords")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKeywords() {

        System.out.println("inside getKeywords REST service");
        List<String> keywords;
        try {
            keywords = Publication.getLibraryKeywords();
             String json = (new Gson()).toJson(keywords);
            return Response.ok(json).build();
        } catch (IOException ex) {
            ex.printStackTrace();
            String msg = String.format("NO Keywords returned from the Library, Type them manualy");
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }
       
    }

}
