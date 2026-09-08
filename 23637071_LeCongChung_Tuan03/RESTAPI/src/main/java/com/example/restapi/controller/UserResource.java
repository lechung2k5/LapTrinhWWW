package com.example.restapi.controller;

import com.example.restapi.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/users")
public class UserResource {

    // Khai báo sẵn danh sách user static ở đầu class để dùng chung cho các hàm
    private static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User(1, "Mai Hoàng", "hoang@gmail.com"));
        userList.add(new User(2, "Lâm Tòng", "tong@gmail.com"));
    }

    // 1. GET - Lấy danh sách TẤT CẢ User
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        System.out.println("Lấy danh sách toàn bộ user");
        return Response.ok(userList).build();
    }

    // 2. GET - Lấy thông tin MỘT User dựa vào id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        System.out.println("Tìm kiếm user có ID: " + id);
        Optional<User> user = userList.stream().filter(u -> u.getId() == id).findFirst();

        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Không tìm thấy người dùng với ID: " + id)
                    .build();
        }
    }

    // 3. POST - Nhận JSON từ Client để thêm 1 User mới
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        System.out.println("Tạo user mới: " + user.getName());
        // Tự động gán ID tăng dần dựa vào kích thước list hiện tại
        if (user.getId() == 0) {
            user.setId(userList.size() + 1);
        }
        userList.add(user);
        return Response.status(Response.Status.CREATED)
                .entity(user)
                .build();
    }

    // 4. PUT - Cập nhật User đã có dựa trên ID
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, User updatedUser) {
        System.out.println("Cập nhật user ID " + id);
        Optional<User> existingUser = userList.stream().filter(u -> u.getId() == id).findFirst();

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Không tìm thấy người dùng để cập nhật với ID: " + id)
                    .build();
        }
    }

    // 5. DELETE - Xóa User dựa trên ID
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int id) {
        System.out.println("Đã xóa user ID: " + id);
        boolean removed = userList.removeIf(u -> u.getId() == id);

        if (removed) {
            return Response.ok("Xóa người dùng thành công!").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Không tìm thấy người dùng để xóa với ID: " + id)
                    .build();
        }
    }
}