package com.gamingroom.gameauth.resource;

import com.gamingroom.gameauth.auth.GamePrincipal;
import com.gamingroom.gameauth.model.CreateGameUserRequest;
import com.gamingroom.gameauth.model.ErrorResponse;
import com.gamingroom.gameauth.model.GameUserRecord;
import com.gamingroom.gameauth.model.UpdateGameUserRequest;
import com.gamingroom.gameauth.repository.DuplicateEmailException;
import com.gamingroom.gameauth.repository.GameUserRepository;
import io.dropwizard.auth.Auth;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameUserResource {
  private final GameUserRepository repository;

  public GameUserResource(GameUserRepository repository) {
    this.repository = repository;
  }

  @GET
  @RolesAllowed("ADMIN")
  public List<GameUserRecord> list(@Auth GamePrincipal principal) {
    return repository.findAll();
  }

  @GET
  @Path("/{id}")
  @RolesAllowed({"USER", "ADMIN"})
  public GameUserRecord get(@Auth GamePrincipal principal, @PathParam("id") long id) {
    return repository.findById(id).orElseThrow(() -> notFound(id));
  }

  @POST
  @RolesAllowed("ADMIN")
  public Response create(
      @Auth GamePrincipal principal,
      @Valid CreateGameUserRequest request,
      @jakarta.ws.rs.core.Context UriInfo uriInfo) {
    try {
      GameUserRecord created = repository.create(request);
      return Response.created(
              uriInfo.getAbsolutePathBuilder().path(Long.toString(created.id())).build())
          .entity(created)
          .build();
    } catch (DuplicateEmailException exception) {
      throw conflict(exception);
    }
  }

  @PUT
  @Path("/{id}")
  @RolesAllowed("ADMIN")
  public GameUserRecord update(
      @Auth GamePrincipal principal,
      @PathParam("id") long id,
      @Valid UpdateGameUserRequest request) {
    try {
      return repository.update(id, request).orElseThrow(() -> notFound(id));
    } catch (DuplicateEmailException exception) {
      throw conflict(exception);
    }
  }

  @DELETE
  @Path("/{id}")
  @RolesAllowed("ADMIN")
  public Response delete(@Auth GamePrincipal principal, @PathParam("id") long id) {
    if (!repository.delete(id)) throw notFound(id);
    return Response.noContent().build();
  }

  private static WebApplicationException notFound(long id) {
    return new WebApplicationException(
        Response.status(Response.Status.NOT_FOUND)
            .entity(new ErrorResponse("USER_NOT_FOUND", "No user exists with id " + id))
            .build());
  }

  private static WebApplicationException conflict(DuplicateEmailException exception) {
    return new WebApplicationException(
        Response.status(Response.Status.CONFLICT)
            .entity(new ErrorResponse("EMAIL_CONFLICT", exception.getMessage()))
            .build());
  }
}
