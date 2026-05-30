async function addToCart(productId) {
    const response = await fetch('/api/cart/add/' + productId, {
        method: 'POST', headers: getCsrfHeaders()});
    if (response.status === 401 || response.status === 403) {
        window.location.href = '/auth/login';
        return;
    }
    if (!response.ok) {
        const data = await response.json();
        alert(data.error || 'Ошибка добавления в корзину');
        return;
    }

    const data = await response.json();
    alert('Товар добавлен в корзину. В корзине товаров: ' + data.totalQuantity);
}

async function increaseCartItem(cartItemId) {
    const response = await fetch('/api/cart/increase/' + cartItemId, {
        method: 'POST', headers: getCsrfHeaders()});
    const data = await response.json();
    renderCart(data);
}

async function decreaseCartItem(cartItemId) {
    const response = await fetch('/api/cart/decrease/' + cartItemId, {
        method: 'POST', headers: getCsrfHeaders()});
    const data = await response.json();
    renderCart(data);
}


async function removeCartItem(cartItemId) {
    const response = await fetch('/api/cart/remove/' + cartItemId, {
        method: 'POST', headers: getCsrfHeaders()});

    const data = await response.json();
    renderCart(data);
}

function renderCart(cart) {
    const container = document.getElementById('cart-items');
    const total = document.getElementById('cart-total');
    if (!container || !total) {
        return;
    }
    container.innerHTML = '';
    if (cart.items.length === 0) {
        container.innerHTML = '<p>Корзина пустая</p>';
        total.innerText = '0';
        return;
    }
    cart.items.forEach(item => {
        const div = document.createElement('div');
        div.className = 'card cart-item';
        div.innerHTML = `
        <h3>${item.title}</h3>
        <p>Цена: ${item.priceRub} ₽</p>
        <p>Количество: ${item.quantity}</p>
        <p>Сумма: ${item.subtotalRub} ₽</p>
        <div class="small-actions">
            <button type="button" onclick="decreaseCartItem(${item.cartItemId})">-</button>
            <button type="button" onclick="increaseCartItem(${item.cartItemId})">+</button>
            <button type="button" onclick="removeCartItem(${item.cartItemId})">Удалить</button>
        </div>
    `;
        container.appendChild(div);
    });

    total.innerText = cart.totalRub;
    const cartCurrency = document.getElementById('cart-currency');
    if (cartCurrency) {
        cartCurrency.innerText = 'RUB';
    }
    const convertedTotal = document.getElementById('converted-total');
    const checkoutLink = document.getElementById('checkout-link');
    if (convertedTotal) {
        convertedTotal.innerText = '—';
    }
    if (checkoutLink) {
        checkoutLink.href = '/checkout?currency=RUB';
    }
}
function getCsrfHeaders() {
    const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    if (token && header) {
        return {
            [header]: token
        };


    }
    return {};
}
async function convertCurrency() {
    const currency = document.getElementById('currency-select').value;
    const response = await fetch('/api/currency/convert?currency=' + currency, {
        method: 'GET', headers: getCsrfHeaders()});
    if (!response.ok) {
        const data = await response.json();
        alert(data.error || 'Ошибка добавления в корзину');
        return;
    }
    const data = await response.json();
    const converted = Number(data.convertedTotal).toFixed(2);
    document.getElementById('converted-total').innerText =
        converted + ' ' + data.currency;
    document.getElementById('cart-total').innerText = converted;
    document.getElementById('cart-currency').innerText = data.currency;
    const checkoutLink = document.getElementById('checkout-link');
    if (checkoutLink) {
        checkoutLink.href = '/checkout?currency=' + data.currency + '&amount=' + converted;
    }

}


