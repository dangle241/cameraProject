const { useState, useEffect } = React;
// Khoi tao root de React render vao phan tu co id "root".
const root = ReactDOM.createRoot(document.getElementById("root"));

// Component giao dien dang nhap.
function LoginPage() {
    // Luu ten dang nhap nguoi dung nhap vao.
    const [username, setUsername] = useState("");
    // Luu mat khau nguoi dung nhap vao.
    const [password, setPassword] = useState("");
    // Dieu khien viec hien/an mat khau tren o input.
    const [showPassword, setShowPassword] = useState(false);
    // Trang thai loading khi dang gui request.
    const [loading, setLoading] = useState(false);
    // Noi dung thong bao thanh cong/that bai.
    const [message, setMessage] = useState("");
    // Danh dau thong bao hien tai la loi hay khong.
    const [isError, setIsError] = useState(false);
    // Luu token sau khi dang nhap thanh cong de hien thi.
    const [token, setToken] = useState("");
    // CSRF header name + value from server (Spring Security cookie-based CSRF).
    const [csrf, setCsrf] = useState({ headerName: "", token: "" });

    useEffect(() => {
        let cancelled = false;
        fetch("/auth/csrf", { credentials: "same-origin" })
            .then((r) => (r.ok ? r.json() : null))
            .then((d) => {
                if (!cancelled && d && d.token && d.headerName) {
                    setCsrf({ headerName: d.headerName, token: d.token });
                }
            })
            .catch(() => {});
        return () => {
            cancelled = true;
        };
    }, []);

    // Ham xu ly khi submit form dang nhap.
    async function handleSubmit(event) {
        // Chan trinh duyet reload trang mac dinh khi submit.
        event.preventDefault();

        // Loai bo khoang trang thua o dau/cuoi username.
        const cleanUsername = username.trim();
        // Neu thieu username hoac password thi thong bao loi.
        if (!cleanUsername || !password) {
            setMessage("Vui long nhap day du ten dang nhap va mat khau.");
            setIsError(true);
            return;
        }

        // Bat dau trang thai loading.
        setLoading(true);
        // Xoa thong bao cu.
        setMessage("");
        // Xoa token cu truoc khi dang nhap lai.
        setToken("");

        try {
            // Goi API dang nhap voi method POST.
            const headers = { "Content-Type": "application/json" };
            if (csrf.token && csrf.headerName) {
                headers[csrf.headerName] = csrf.token;
            }
            const response = await fetch("/auth/login", {
                method: "POST",
                credentials: "same-origin",
                headers,
                body: JSON.stringify({
                    username: cleanUsername,
                    password
                })
            });

            // Neu server tra ve loi (khong phai 2xx).
            if (!response.ok) {
                // Thu doc noi dung loi tra ve tu server.
                const errorText = await response.text();
                // Hien loi tu server neu co, neu khong dung loi mac dinh.
                setMessage(errorText && errorText.trim() ? errorText : "Dang nhap that bai.");
                setIsError(true);
                return;
            }

            // Parse JSON ket qua khi dang nhap thanh cong.
            const data = await response.json();
            // Lay token tu response, neu khong co thi gan chuoi rong.
            const userToken = data.token || "";
            // Luu token vao localStorage de dung cho lan sau.
            localStorage.setItem("auth_token", userToken);
            // Luu trang thai dang nhap thanh cong vao session.
            sessionStorage.setItem("login_status", "success");

            // Cap nhat giao dien thong bao thanh cong.
            setMessage("Dang nhap thanh cong.");
            setIsError(false);
            setToken(userToken);
            // Delay nho de nguoi dung thay thong bao roi moi chuyen trang.
            setTimeout(() => {
                window.location.href = "/home.html";
            }, 500);
        } catch (error) {
            // Xu ly truong hop khong ket noi duoc den server.
            setMessage("Khong the ket noi toi may chu.");
            setIsError(true);
        } finally {
            // Tat loading du thanh cong hay that bai.
            setLoading(false);
        }
    }

    // Tra ve cay giao dien duoc tao bang React.createElement.
    return React.createElement(
        "main",
        // Lop CSS cho khung trang.
        { className: "page" },
        React.createElement(
            "section",
            // Lop CSS cho the card trung tam.
            { className: "card" },
            React.createElement("h1", null, "Dang nhap"),
            React.createElement("p", { className: "subtitle" }, "Vui long nhap tai khoan de tiep tuc."),
            React.createElement(
                "form",
                // Gan su kien submit vao ham handleSubmit.
                { onSubmit: handleSubmit, noValidate: true },
                React.createElement("label", { htmlFor: "username" }, "Ten dang nhap"),
                React.createElement("input", {
                    id: "username",
                    name: "username",
                    type: "text",
                    value: username,
                    autoComplete: "username",
                    required: true,
                    // Moi lan go phim se cap nhat state username.
                    onChange: (event) => setUsername(event.target.value)
                }),
                React.createElement("label", { htmlFor: "password" }, "Mat khau"),
                React.createElement(
                    "div",
                    // Wrapper gom input mat khau va nut hien/an.
                    { className: "password-field" },
                    React.createElement("input", {
                        id: "password",
                        name: "password",
                        // Doi type dua tren trang thai showPassword.
                        type: showPassword ? "text" : "password",
                        value: password,
                        autoComplete: "current-password",
                        required: true,
                        // Moi lan go phim se cap nhat state password.
                        onChange: (event) => setPassword(event.target.value)
                    }),
                    React.createElement(
                        "button",
                        {
                            type: "button",
                            className: "password-toggle",
                            // Dao nguoc trang thai hien/an mat khau.
                            onClick: () => setShowPassword((prev) => !prev),
                            // Nhac cho screen reader hanh dong cua nut.
                            "aria-label": showPassword ? "An mat khau" : "Hien mat khau"
                        },
                        React.createElement(
                            "svg",
                            // Icon con mat.
                            {
                                width: "20",
                                height: "20",
                                viewBox: "0 0 24 24",
                                fill: "none",
                                xmlns: "http://www.w3.org/2000/svg"
                            },
                            React.createElement("path", {
                                d: "M2 12C3.8 8.5 7.4 6 12 6C16.6 6 20.2 8.5 22 12C20.2 15.5 16.6 18 12 18C7.4 18 3.8 15.5 2 12Z",
                                stroke: "currentColor",
                                strokeWidth: "1.8"
                            }),
                            React.createElement("circle", {
                                cx: "12",
                                cy: "12",
                                r: "3",
                                stroke: "currentColor",
                                strokeWidth: "1.8"
                            }),
                            // Neu dang an mat khau thi ve them duong gach che icon.
                            showPassword
                                ? null
                                : React.createElement("path", {
                                    d: "M4 20L20 4",
                                    stroke: "currentColor",
                                    strokeWidth: "1.8"
                                })
                        )
                    )
                ),
                React.createElement(
                    "button",
                    // Nut submit bi vo hieu khi dang loading.
                    { type: "submit", className: "submit-btn", disabled: loading },
                    // Text tren nut thay doi theo trang thai loading.
                    loading ? "Dang dang nhap..." : "Dang nhap"
                )
            ),
            React.createElement(
                "a",
                // Nut dang nhap OAuth2 voi Google.
                { href: "/oauth2/authorization/google", className: "google-login-btn" },
                "Dang nhap bang Google"
            ),
            React.createElement(
                "p",
                {
                    // Them class "error" hoac "success" khi co message.
                    className: `message${message ? isError ? " error" : " success" : ""}`,
                    // Cho phep screen reader doc thay doi thong bao.
                    "aria-live": "polite"
                },
                message
            ),
            React.createElement(
                "p",
                // Chi hien token-box khi co token.
                { className: `token-box${token ? "" : " hidden"}` },
                token ? `Token: ${token}` : ""
            )
        )
    );
}

// Render component LoginPage len man hinh.
root.render(React.createElement(LoginPage));
