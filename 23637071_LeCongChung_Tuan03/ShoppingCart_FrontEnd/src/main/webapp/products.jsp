<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Shopping Cart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<main class="page">
<header class="topbar">
<div>
<p class="eyebrow">SHOPPING CART</p>
<h1>Danh sách sản phẩm</h1>
<p id="welcome" class="muted">Đang tải session…</p>
</div>
<button id="logoutButton" class="button secondary" type="button">Đăng xuất</button>
</header>
<div id="message" class="message" hidden></div>
<div class="layout">
<section class="card">
<div class="section-heading">
<div><p class="eyebrow">GET /PRODUCTS</p><h2>Sản phẩm</h2></div>
<button id="reloadButton" class="button subtle" type="button">Tải lại</button>
</div>
<div id="products" class="product-list"><p class="muted">Đang tải sản phẩm…</p></div>
</section>
<aside class="card cart-card">
<p class="eyebrow">GET /CART</p>
<h2>Giỏ hàng</h2>
<div id="cart"><p class="muted">Đang tải giỏ hàng…</p></div>
<p id="instanceId" class="technical"></p>
</aside>
</div>
</main>
<script>
const API = '${initParam.apiBaseUrl}';
const contextPath = '${pageContext.request.contextPath}';
const productBox = document.querySelector('#products');
const cartBox = document.querySelector('#cart');
const messageBox = document.querySelector('#message');

const money = value => new Intl.NumberFormat('vi-VN', {
style: 'currency', currency: 'USD'
}).format(value);

async function callApi(path, options = {}) {
const response = await fetch(API + path, {...options, credentials: 'include'});
if (response.status === 401) {
location.href = contextPath + '/login.jsp';
throw new Error('Session đăng nhập đã hết hạn');
}
const data = response.status === 204 ? null : await response.json();
if (!response.ok) throw new Error(data?.message || 'REST API trả lỗi ' + response.status);
return data;
}

function showMessage(text, error = false) {
messageBox.textContent = text;
messageBox.className = error ? 'message error' : 'message success';
messageBox.hidden = false;
setTimeout(() => messageBox.hidden = true, 2500);
}

function renderProducts(products) {
productBox.replaceChildren();
for (const product of products) {
const row = document.createElement('article');
row.className = 'product';
const description = document.createElement('div');
const name = document.createElement('h3');
name.textContent = product.name;
const price = document.createElement('p');
price.className = 'price';
price.textContent = money(product.price);
description.append(name, price);
const addButton = document.createElement('button');
addButton.className = 'button';
addButton.textContent = 'Thêm vào giỏ';
addButton.addEventListener('click', () => addToCart(product.id, addButton));
row.append(description, addButton);
productBox.append(row);
}
}

function renderCart(cart) {
cartBox.replaceChildren();
if (cart.items.length === 0) {
const empty = document.createElement('p');
empty.className = 'muted';
empty.textContent = 'Giỏ hàng đang trống.';
cartBox.append(empty);
} else {
const list = document.createElement('ul');
list.className = 'cart-list';
for (const item of cart.items) {
const row = document.createElement('li');
const label = document.createElement('span');
label.textContent = item.product.name + ' × ' + item.quantity;
const subtotal = document.createElement('strong');
subtotal.textContent = money(item.subtotal);
row.append(label, subtotal);
list.append(row);
}
cartBox.append(list);
}
const summary = document.createElement('div');
summary.className = 'cart-summary';
summary.innerHTML = '<span>' + cart.itemCount + ' sản phẩm</span><strong>' + money(cart.total) + '</strong>';
cartBox.append(summary);
}

async function addToCart(productId, button) {
button.disabled = true;
try {
const cart = await callApi('/cart/items', {
method: 'POST',
headers: {'Content-Type': 'application/json'},
body: JSON.stringify({productId})
});
renderCart(cart);
showMessage('Đã thêm sản phẩm vào giỏ hàng.');
} catch (error) {
showMessage(error.message, true);
} finally {
button.disabled = false;
}
}

async function loadPage() {
try {
const [session, products, cart] = await Promise.all([
callApi('/auth/session'),
callApi('/products'),
callApi('/cart')
]);
if (!session.loggedIn) {
location.href = contextPath + '/login.jsp';
return;
}
document.querySelector('#welcome').textContent = 'Xin chào, ' + session.username;
document.querySelector('#instanceId').textContent = 'CDI Session: ' + session.instanceId;
renderProducts(products);
renderCart(cart);
} catch (error) {
showMessage(error.message, true);
}
}

document.querySelector('#reloadButton').addEventListener('click', loadPage);
document.querySelector('#logoutButton').addEventListener('click', async () => {
try {
await callApi('/auth/session', {method: 'DELETE'});
location.href = contextPath + '/login.jsp';
} catch (error) {
showMessage(error.message, true);
}
});
loadPage();
</script>
</body>
</html>
