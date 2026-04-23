const API = "http://localhost:8080/products";
let editId = null;

// ADD / UPDATE PRODUCT
function addProduct() {
    const nameVal = document.getElementById("name").value;
    const catVal = document.getElementById("category").value;
    const priceVal = document.getElementById("price").value;
    const qtyVal = document.getElementById("quantity").value;

    if(!nameVal || !catVal || !priceVal || !qtyVal) {
        alert("Please fill all fields");
        return;
    }

    const product = {
        name: nameVal,
        category: catVal,
        price: parseFloat(priceVal),
        quantity: parseInt(qtyVal)
    };

    let url = API;
    let method = "POST";

    if (editId !== null) {
        url = API + "/" + editId;
        method = "PUT";
    }

    fetch(url, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(product)
    })
    .then(res => {
        if(!res.ok) throw new Error("Server Error");
        return res.json();
    })
    .then(() => {
        alert(editId ? "Updated!" : "Added!");
        clearForm();
        load();
    })
    .catch(err => console.error("Error saving product:", err));
}

// LOAD ALL PRODUCTS
function load() {
    fetch(API)
        .then(res => res.json())
        .then(data => show(data))
        .catch(err => console.error("Could not load data. Is the Java backend running?", err));
}

// SHOW DATA (Table Logic)
function show(data) {
    const tableBody = document.getElementById("table");
    tableBody.innerHTML = "";

    let lowCount = 0;
    let categoryCount = {};

    data.forEach(p => {
        if (p.status === "LOW_STOCK") lowCount++;
        categoryCount[p.category] = (categoryCount[p.category] || 0) + 1;

        const isLow = p.status === 'LOW_STOCK';

        tableBody.innerHTML += `
        <tr class="${isLow ? 'low-row' : ''}">
            <td>${p.name}</td>
            <td>${p.category}</td>
            <td>$${p.price}</td>
            <td>${p.quantity}</td>
            <td class="${isLow ? 'low-text' : ''}">${p.status}</td>
            <td>
                <button onclick="editProduct(${p.id}, '${p.name}', '${p.category}', ${p.price}, ${p.quantity})">Edit</button>
                <button style="background:red; color:white;" onclick="deleteProduct(${p.id})">Delete</button>
            </td>
        </tr>`;
    });

    document.getElementById("total").innerText = data.length;
    document.getElementById("low").innerText = lowCount;

    let top = "-";
    let max = 0;
    for (let cat in categoryCount) {
        if (categoryCount[cat] > max) {
            max = categoryCount[cat];
            top = cat;
        }
    }
    document.getElementById("topCategory").innerText = top;
}

// EDIT, DELETE, SEARCH (Rest unchanged but verified)
function editProduct(id, name, category, price, quantity) {
    document.getElementById("name").value = name;
    document.getElementById("category").value = category;
    document.getElementById("price").value = price;
    document.getElementById("quantity").value = quantity;
    editId = id;
    document.getElementById("save-btn").innerText = "Update Product";
}

function deleteProduct(id) {
    if(confirm("Delete this product?")) {
        fetch(API + "/" + id, { method: "DELETE" }).then(() => load());
    }
}

function searchProduct() {
    const keyword = document.getElementById("search").value;
    fetch(API + "/search?keyword=" + keyword)
        .then(res => res.json())
        .then(data => show(data));
}

function sortAsc() { fetch(API + "/sort/asc").then(res => res.json()).then(data => show(data)); }
function sortDesc() { fetch(API + "/sort/desc").then(res => res.json()).then(data => show(data)); }

function clearForm() {
    document.getElementById("name").value = "";
    document.getElementById("category").value = "";
    document.getElementById("price").value = "";
    document.getElementById("quantity").value = "";
    editId = null;
    document.getElementById("save-btn").innerText = "Add Product";
}

function logout() {
    localStorage.removeItem("user");
    window.location.href = "login.html";
}

// RUN ON START
load();