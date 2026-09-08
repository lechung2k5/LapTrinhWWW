package vn.edu.iuh.cartapi.api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.cartapi.service.AuthenticationService;
import vn.edu.iuh.cartapi.session.UserSession;

import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuthResource {

    @Inject
    AuthenticationService authenticationService;

    @Inject
    UserSession userSession;

    @POST
    @Path("/login")
    public Response login(LoginRequest body) {
        if (body == null || !authenticationService.authenticate(body.username(), body.password())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("message", "Sai tài khoản hoặc mật khẩu"))
                    .build();
        }

        userSession.login(body.username());

        return Response.ok(Map.of(
                "loggedIn", true,
                "username", userSession.getUsername(),
                "instanceId", userSession.getInstanceId()
        )).build();
    }

    @GET
    @Path("/session")
    public Map<String, Object> session() {
        return Map.of(
                "loggedIn", userSession.isLoggedIn(),
                "username", userSession.getUsername() == null ? "" : userSession.getUsername(),
                "instanceId", userSession.getInstanceId()
        );
    }

    @DELETE
    @Path("/session")
    public Response logout(@Context HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        return Response.noContent().build();
    }

    public record LoginRequest(String username, String password) {}
}
