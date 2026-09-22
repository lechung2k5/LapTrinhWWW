<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration Form</title>


    <link href="https://jsdelivr.net" rel="stylesheet">

    <style>
        body {
            background-color: #f8f9fa;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            font-family: Arial, sans-serif;
        }
        .registration-card {
            width: 100%;
            max-width: 500px;
            background: #fff;
            border: 1px solid #dcdcdc;
            border-radius: 8px;
            padding: 30px;
        }
        .form-control, .form-select {
            border: 1px solid #e0e0e0;
            border-radius: 6px;
            padding: 10px 12px;
            color: #495057;
        }
        .form-control::placeholder {
            color: #a0a0a0;
        }
        .form-label-custom {
            font-weight: 500;
            color: #333;
            margin-bottom: 8px;
        }
        .btn-signup {
            background-color: #0d6efd;
            border: none;
            color: white;
            padding: 12px;
            font-size: 18px;
            border-radius: 8px;
            width: 100%;
            transition: background 0.2s;
            font-weight: bold;
        }
        .btn-signup:hover {
            background-color: #0b5ed7;
        }
    </style>
</head>
<body>

<div class="registration-card shadow-sm">
    <h2 class="mb-4 fw-normal" style="color: #111;">User Registration Form</h2>

    <form action="${pageContext.request.contextPath}/register" method="POST">

        <div class="mb-3">
            <input type="text" class="form-control" name="firstName" placeholder="First Name" required>
        </div>
        <div class="mb-3">
            <input type="text" class="form-control" name="lastName" placeholder="Last Name" required>
        </div>

        <div class="mb-3">
            <input type="email" class="form-control" name="email" placeholder="Your Email" required>
        </div>

        <div class="mb-3">
            <input type="password" class="form-control" name="password" placeholder="Password" required>
        </div>
        <div class="mb-3">
            <label class="form-label-custom d-block">Birthday</label>
            <div class="row g-2">

                <div class="col-4">
                    <select class="form-select" name="birthMonth" required>
                        <option value="" disabled selected>Month</option>
                        <option value="01">Tháng 1</option>
                        <option value="02">Tháng 2</option>
                        <option value="03">Tháng 3</option>
                        <option value="04">Tháng 4</option>
                        <option value="05">Tháng 5</option>
                        <option value="06">Tháng 6</option>
                        <option value="07">Tháng 7</option>
                        <option value="08">Tháng 8</option>
                        <option value="09">Tháng 9</option>
                        <option value="10">Tháng 10</option>
                        <option value="11">Tháng 11</option>
                        <option value="12">Tháng 12</option>
                    </select>
                </div>

                <div class="col-4">
                    <select class="form-select" name="birthDay" required>
                        <option value="" disabled selected>Day</option>
                        <% for (int i = 1; i <= 31; i++) {
                            String dayStr = (i < 10) ? "0" + i : String.valueOf(i);
                        %>
                        <option value="<%= dayStr %>"><%= i %></option>
                        <% } %>
                    </select>
                </div>

                <div class="col-4">
                    <select class="form-select" name="birthYear" required>
                        <option value="" disabled selected>Year</option>
                        <%
                            int currentYear = java.time.Year.now().getValue();
                            for (int i = currentYear; i >= currentYear - 100; i--) {
                        %>
                        <option value="<%= i %>"><%= i %></option>
                        <% } %>
                    </select>
                </div>
            </div>
        </div>


        <div class="mb-4">
            <label class="form-label-custom d-block">Gender</label>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="gender" id="genderFemale" value="Female">
                <label class="form-check-label text-muted" for="genderFemale">Female</label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="gender" id="genderMale" value="Male" checked>
                <label class="form-check-label text-muted" for="genderMale">Male</label>
            </div>
        </div>


        <button type="submit" class="btn-signup">Sign Up</button>
    </form>
</div>

</body>
</html>
