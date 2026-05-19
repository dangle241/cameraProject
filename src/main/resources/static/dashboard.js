(function () {
    const userNameElement = document.getElementById("user-name");
    const sidebarUserName = document.getElementById("sidebar-user-name");
    const userAvatar = document.getElementById("user-avatar");
    const successMessage = document.getElementById("success-message");
    const logoutButton = document.getElementById("logout-btn");
    const themeToggle = document.getElementById("theme-toggle");
    const sidebarToggle = document.getElementById("sidebar-toggle");
    const chartElement = document.getElementById("overview-chart");
    const recentSalesElement = document.getElementById("recent-sales");
    const storedUser = localStorage.getItem("auth_user");
    const token = localStorage.getItem("auth_token");
    const loginStatus = sessionStorage.getItem("login_status");
    const preferredTheme = localStorage.getItem("dashboard_theme") || "light";
    const chartData = [
        { name: "Jan", total: 4200 },
        { name: "Feb", total: 3100 },
        { name: "Mar", total: 5200 },
        { name: "Apr", total: 4600 },
        { name: "May", total: 5800 },
        { name: "Jun", total: 3900 },
        { name: "Jul", total: 5000 },
        { name: "Aug", total: 6100 },
        { name: "Sep", total: 4300 },
        { name: "Oct", total: 5600 },
        { name: "Nov", total: 4700 },
        { name: "Dec", total: 6900 }
    ];
    const recentSales = [
        ["OM", "Olivia Martin", "olivia.martin@email.com", "+$1,999.00"],
        ["JL", "Jackson Lee", "jackson.lee@email.com", "+$39.00"],
        ["IN", "Isabella Nguyen", "isabella.nguyen@email.com", "+$299.00"],
        ["WK", "William Kim", "will@email.com", "+$99.00"],
        ["SD", "Sofia Davis", "sofia.davis@email.com", "+$39.00"]
    ];

    if (storedUser) {
        userNameElement.textContent = storedUser;
        sidebarUserName.textContent = storedUser;
        userAvatar.textContent = storedUser.slice(0, 1).toUpperCase();
    }

    if (loginStatus === "success") {
        successMessage.classList.remove("hidden");
        sessionStorage.removeItem("login_status");
    }

    if (!token && !storedUser) {
        successMessage.textContent = "Bạn đang xem trang mẫu sau đăng nhập";
        successMessage.classList.remove("hidden");
    }

    if (preferredTheme === "dark") {
        document.body.classList.add("dark");
    }

    chartData.forEach((item) => {
        const bar = document.createElement("div");
        const height = Math.round(Math.max(12, (item.total / 7000) * 100));
        bar.className = `bar bar-h-${height}`;
        bar.title = `${item.name}: $${item.total}`;
        bar.innerHTML = `<span>${item.name}</span>`;
        chartElement.appendChild(bar);
    });

    recentSales.forEach(([initials, name, email, amount]) => {
        const item = document.createElement("div");
        item.className = "sale-item";
        item.innerHTML = `
            <div class="avatar">${initials}</div>
            <div class="sale-main">
                <strong>${name}</strong>
                <span>${email}</span>
            </div>
            <div class="sale-amount">${amount}</div>
        `;
        recentSalesElement.appendChild(item);
    });

    themeToggle.addEventListener("click", function () {
        document.body.classList.toggle("dark");
        localStorage.setItem("dashboard_theme", document.body.classList.contains("dark") ? "dark" : "light");
    });

    sidebarToggle.addEventListener("click", function () {
        if (window.matchMedia("(max-width: 820px)").matches) {
            document.body.classList.toggle("sidebar-open");
            return;
        }
        document.body.classList.toggle("sidebar-collapsed");
    });

    logoutButton.addEventListener("click", function () {
        localStorage.removeItem("auth_token");
        localStorage.removeItem("auth_user");
        sessionStorage.removeItem("login_status");
        window.location.href = "/home.html";
    });
})();
