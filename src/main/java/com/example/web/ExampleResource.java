package com.example.web;

import com.example.pojo.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class ExampleResource {

    @GET
    @Path("/employee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployee(@PathParam("id") Long id) {
//        return User.findById(id);
        User user=User.findById(id);
        return Response.ok(user).build();
    }
//    @Transactional
//    @PostConstruct
//    public void init() {
//        var user=new User();
//        user.setId(1);
//        user.setName("2");
//        user.setSurname("3");
//        userRepository.persist(user);
//    }
}
