package vn.edu.store.controller;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.edu.store.model.Account;
import vn.edu.store.service.AccountService;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Inject
    private AccountService accountService;
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/account/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String year = request.getParameter("birthYear");
        String month = request.getParameter("birthMonth");
        String day = request.getParameter("birthDay");
        LocalDate dateOfBirth = null;
        if (year != null && month != null && day != null) {
            try {
                String dobString = year + "-" + month + "-" + day;
                dateOfBirth = LocalDate.parse(dobString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Account account = new Account();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPassword(password);
        account.setDateOfBirth(dateOfBirth);

        boolean isSaved = accountService.saveAccount(account);

        if (isSaved) {
            response.sendRedirect(request.getContextPath() + "/accounts");
        } else {
            request.setAttribute("errorMessage", "Đăng ký tài khoản thất bại!");
            request.getRequestDispatcher("/WEB-INF/views/account/register.jsp")
                    .forward(request, response);
        }
    }
}
