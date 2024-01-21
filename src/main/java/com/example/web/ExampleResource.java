package com.example.web;

import com.example.pojo.User;
import com.example.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class ExampleResource {
    @Inject
    UserRepository userRepository;

    @GET
    @Path("/employee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployee(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/employee")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createEmployee(
            @QueryParam("id") Long id,
            @QueryParam("name") String name,
            @QueryParam("surname") String surname) {

        User user = new User();
        user.setId(Math.toIntExact(id));
        user.setName(name);
        user.setSurname(surname);

        userRepository.persist(user);

        return Response.ok(user).build();
    }

    @PUT
    @Path("/employee/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEmployee(@PathParam("id") Long id, User updatedUser) {
        User existingUser = userRepository.findById(id);
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setSurname(updatedUser.getSurname());
            userRepository.persist(existingUser);
            return Response.ok(existingUser).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/employee/{id}")
    @Transactional
    public Response deleteEmployee(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            userRepository.delete(user);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
