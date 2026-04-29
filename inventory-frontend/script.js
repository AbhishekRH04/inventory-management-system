const API = "http://localhost:8080/products";
let editId = null;

// ADD / UPDATE PRODUCT
function addProduct() {
    const nameVal = document.getElementById("name").value;
    const catVal = document.getElementById("category").value;
    const priceVal = document.getElementById("price").value;
    const qtyVal = document.getElementById("quantity").value;

    if (!nameVal || !catVal || !priceVal || !qtyVal) {
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
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(product)
    })
    .then(async res => {
        const data = await res.json();

        if (!res.ok) {
            throw new Error(data.message); // backend exception message
        }

        return data;
    })
    .then(() => {
        alert(editId ? "Product Updated Successfully!" : "Product Added Successfully!");
        clearForm();
        load();
    })
    .catch(err => {
        alert(err.message);
        console.error("Error saving product:", err);
    });
}


// LOAD ALL PRODUCTS
function load() {
    fetch(API)
        .then(async res => {
            if (!res.ok) {
                const data = await res.json();
                throw new Error(data.message);
            }
            return res.json();
        })
        .then(data => show(data))
        .catch(err => {
            alert(err.message);
            console.error("Could not load products:", err);
        });
}


// SHOW DATA
function show(data) {
    const tableBody = document.getElementById("table");
    tableBody.innerHTML = "";

    let lowCount = 0;
    let categoryCount = {};

    data.forEach(p => {
        if (p.status === "LOW_STOCK") lowCount++;

        categoryCount[p.category] = (categoryCount[p.category] || 0) + 1;

        const isLow = p.status === "LOW_STOCK";

        tableBody.innerHTML += `
        <tr class="${isLow ? 'low-row' : ''}">
            <td>${p.name}</td>
            <td>${p.category}</td>
            <td>${p.price}</td>
            <td>${p.quantity}</td>
            <td class="${isLow ? 'low-text' : ''}">${p.status}</td>
            <td>
                <button onclick="editProduct(${p.id}, '${p.name}', '${p.category}', ${p.price}, ${p.quantity})">
                    Edit
                </button>

                <button 
                    style="background:red; color:white;" 
                    onclick="deleteProduct(${p.id})">
                    Delete
                </button>
            </td>
        </tr>
        `;
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


// EDIT PRODUCT
function editProduct(id, name, category, price, quantity) {
    document.getElementById("name").value = name;
    document.getElementById("category").value = category;
    document.getElementById("price").value = price;
    document.getElementById("quantity").value = quantity;

    editId = id;

    document.getElementById("save-btn").innerText = "Update Product";
}


// DELETE PRODUCT
function deleteProduct(id) {
    if (confirm("Delete this product?")) {
        fetch(API + "/" + id, {
            method: "DELETE"
        })
        .then(async res => {
            if (!res.ok) {
                const data = await res.json();
                throw new Error(data.message);
            }

            alert("Product deleted successfully!");
            load();
        })
        .catch(err => {
            alert(err.message);
            console.error("Delete error:", err);
        });
    }
}


// SEARCH PRODUCT
function searchProduct() {
    const keyword = document.getElementById("search").value;

    fetch(API + "/search?keyword=" + keyword)
        .then(async res => {
            if (!res.ok) {
                const data = await res.json();
                throw new Error(data.message);
            }

            return res.json();
        })
        .then(data => show(data))
        .catch(err => {
            alert(err.message);
            console.error("Search error:", err);
        });
}


// SORT ASC
function sortAsc() {
    fetch(API + "/sort/asc")
        .then(res => res.json())
        .then(data => show(data))
        .catch(err => console.error(err));
}


// SORT DESC
function sortDesc() {
    fetch(API + "/sort/desc")
        .then(res => res.json())
        .then(data => show(data))
        .catch(err => console.error(err));
}


// CLEAR FORM
function clearForm() {
    document.getElementById("name").value = "";
    document.getElementById("category").value = "";
    document.getElementById("price").value = "";
    document.getElementById("quantity").value = "";

    editId = null;

    document.getElementById("save-btn").innerText = "Add Product";
}


// LOGOUT
function logout() {
    localStorage.removeItem("user");
    window.location.href = "login.html";
}


// RUN ON START
load();