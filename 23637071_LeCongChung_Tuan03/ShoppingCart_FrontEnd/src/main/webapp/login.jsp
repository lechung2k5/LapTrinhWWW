<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đăng nhập Shopping Cart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<main class="card login-card">
<p class="eyebrow">JAKARTA EE · JSP · REST</p>
<h1>Đăng nhập</h1>
<p class="muted">Tài khoản demo: <strong>student</strong> / <strong>123456</strong></p>
<div id="error" class="message error" hidden></div>
<form id="loginForm">
<label for="username">Tên đăng nhập</label>
<input id="username" name="username" value="student" autocomplete="username" required autofocus>
<label for="password">Mật khẩu</label>
<input id="password" name="password" type="password" value="123456" autocomplete="current-password" required>
<button id="loginButton" type="submit">Đăng nhập</button>
</form>
</main>
<script>
const API = '${initParam.apiBaseUrl}';
const contextPath = '${pageContext.request.contextPath}';
const form = document.querySelector('#loginForm');
const errorBox = document.querySelector('#error');
const button = document.querySelector('#loginButton');

// Nếu backend vẫn còn session đăng nhập, đi thẳng đến trang sản phẩm.
fetch(API + '/auth/session', {credentials: 'include'})
.then(response => response.ok ? response.json() : null)
.then(session => {
if (session?.loggedIn) location.href = contextPath + '/products.jsp';
})
.catch(() => {});

form.addEventListener('submit', async event => {
event.preventDefault();
errorBox.hidden = true;
button.disabled = true;
button.textContent = 'Đang đăng nhập…';
try {
const response = await fetch(API + '/auth/login', {
method: 'POST',
credentials: 'include',
headers: {'Content-Type': 'application/json'},
body: JSON.stringify({
username: document.querySelector('#username').value,
password: document.querySelector('#password').value
})
});
const data = await response.json();
if (!response.ok) throw new Error(data.message || 'Đăng nhập thất bại');
location.href = contextPath + '/products.jsp';
} catch (error) {
errorBox.textContent = error.message === 'Failed to fetch' ? 'Không kết nối được backend REST API.' : error.message;
errorBox.hidden = false;
} finally {
button.disabled = false;
button.textContent = 'Đăng nhập';
}
});
</script>
</body>
</html>
