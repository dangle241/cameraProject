const { useState } = React;
const root = ReactDOM.createRoot(document.getElementById("root"));

function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");
    const [isError, setIsError] = useState(false);
    const [token, setToken] = useState("");

    async function handleSubmit(event) {
        event.preventDefault();

        const cleanUsername = username.trim();
        if (!cleanUsername || !password) {
            setMessage("Vui long nhap day du ten dang nhap va mat khau.");
            setIsError(true);
            return;
        }

        setLoading(true);
        setMessage("");
        setToken("");

        try {
            const response = await fetch("/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: cleanUsername,
                    password
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                setMessage(errorText && errorText.trim() ? errorText : "Dang nhap that bai.");
                setIsError(true);
                return;
            }

            const data = await response.json();
            const userToken = data.token || "";
            localStorage.setItem("auth_token", userToken);
            sessionStorage.setItem("login_status", "success");

            setMessage("Dang nhap thanh cong.");
            setIsError(false);
            setToken(userToken);
            setTimeout(() => {
                window.location.href = "/home.html";
            }, 500);
        } catch (error) {
            setMessage("Khong the ket noi toi may chu.");
            setIsError(true);
        } finally {
            setLoading(false);
        }
    }

    return React.createElement(
        "main",
        { className: "page" },
        React.createElement(
            "section",
            { className: "card" },
            React.createElement("h1", null, "Dang nhap"),
            React.createElement("p", { className: "subtitle" }, "Vui long nhap tai khoan de tiep tuc."),
            React.createElement(
                "form",
                { onSubmit: handleSubmit, noValidate: true },
                React.createElement("label", { htmlFor: "username" }, "Ten dang nhap"),
                React.createElement("input", {
                    id: "username",
                    name: "username",
                    type: "text",
                    value: username,
                    autoComplete: "username",
                    required: true,
                    onChange: (event) => setUsername(event.target.value)
                }),
                React.createElement("label", { htmlFor: "password" }, "Mat khau"),
                React.createElement(
                    "div",
                    { className: "password-field" },
                    React.createElement("input", {
                        id: "password",
                        name: "password",
                        type: showPassword ? "text" : "password",
                        value: password,
                        autoComplete: "current-password",
                        required: true,
                        onChange: (event) => setPassword(event.target.value)
                    }),
                    React.createElement(
                        "button",
                        {
                            type: "button",
                            className: "password-toggle",
                            onClick: () => setShowPassword((prev) => !prev),
                            "aria-label": showPassword ? "An mat khau" : "Hien mat khau"
                        },
                        React.createElement(
                            "svg",
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
                    { type: "submit", className: "submit-btn", disabled: loading },
                    loading ? "Dang dang nhap..." : "Dang nhap"
                )
            ),
            React.createElement(
                "a",
                { href: "/oauth2/authorization/google", className: "google-login-btn" },
                "Dang nhap bang Google"
            ),
            React.createElement(
                "p",
                {
                    className: `message${message ? isError ? " error" : " success" : ""}`,
                    "aria-live": "polite"
                },
                message
            ),
            React.createElement(
                "p",
                { className: `token-box${token ? "" : " hidden"}` },
                token ? `Token: ${token}` : ""
            )
        )
    );
}

root.render(React.createElement(LoginPage));
