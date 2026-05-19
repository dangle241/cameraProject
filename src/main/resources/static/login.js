const { useEffect, useState } = React;
const root = ReactDOM.createRoot(document.getElementById("root"));
const e = React.createElement;

function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");
    const [isError, setIsError] = useState(false);
    const [csrf, setCsrf] = useState({ headerName: "", token: "" });

    useEffect(() => {
        let cancelled = false;
        fetch("/auth/csrf", { credentials: "same-origin" })
            .then((response) => (response.ok ? response.json() : null))
            .then((data) => {
                if (!cancelled && data && data.token && data.headerName) {
                    setCsrf({ headerName: data.headerName, token: data.token });
                }
            })
            .catch(() => {});
        return () => {
            cancelled = true;
        };
    }, []);

    async function handleSubmit(event) {
        event.preventDefault();

        const cleanUsername = username.trim();
        if (!cleanUsername || !password) {
            setMessage("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            setIsError(true);
            return;
        }

        setLoading(true);
        setMessage("");

        try {
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

            if (!response.ok) {
                const errorText = await response.text();
                setMessage(errorText && errorText.trim() ? errorText : "Đăng nhập thất bại.");
                setIsError(true);
                return;
            }

            const data = await response.json();
            const userToken = data.token || "";
            localStorage.setItem("auth_token", userToken);
            localStorage.setItem("auth_user", cleanUsername);
            sessionStorage.setItem("login_status", "success");

            setMessage("Đăng nhập thành công.");
            setIsError(false);
            setTimeout(() => {
                window.location.href = "/dashboard.html";
            }, 400);
        } catch (error) {
            setMessage("Không thể kết nối tới máy chủ.");
            setIsError(true);
        } finally {
            setLoading(false);
        }
    }

    return e(
        "main",
        { className: "auth-page" },
        e(
            "section",
            { className: "auth-panel" },
            e(
                "a",
                { className: "brand", href: "/home.html" },
                e("span", { className: "brand-mark" }, "SA"),
                e("span", null, "Shadcn Admin")
            ),
            e(
                "div",
                { className: "form-wrap" },
                e("h1", null, "Sign in"),
                e(
                    "p",
                    { className: "subtitle" },
                    "Enter your username and password below to log into your account."
                ),
                e(
                    "form",
                    { onSubmit: handleSubmit, noValidate: true },
                    e(
                        "div",
                        { className: "field" },
                        e("label", { htmlFor: "username" }, "Username"),
                        e("input", {
                            id: "username",
                            name: "username",
                            type: "text",
                            value: username,
                            autoComplete: "username",
                            required: true,
                            placeholder: "admin",
                            onChange: (event) => setUsername(event.target.value)
                        })
                    ),
                    e(
                        "div",
                        { className: "field" },
                        e(
                            "div",
                            { className: "label-row" },
                            e("label", { htmlFor: "password" }, "Password"),
                            e("a", { href: "#forgot-password" }, "Forgot password?")
                        ),
                        e(
                            "div",
                            { className: "password-field" },
                            e("input", {
                                id: "password",
                                name: "password",
                                type: showPassword ? "text" : "password",
                                value: password,
                                autoComplete: "current-password",
                                required: true,
                                placeholder: "••••••••",
                                onChange: (event) => setPassword(event.target.value)
                            }),
                            e(
                                "button",
                                {
                                    type: "button",
                                    className: "password-toggle",
                                    onClick: () => setShowPassword((prev) => !prev),
                                    "aria-label": showPassword ? "Ẩn mật khẩu" : "Hiện mật khẩu"
                                },
                                showPassword ? "Hide" : "Show"
                            )
                        )
                    ),
                    e(
                        "button",
                        { type: "submit", className: "submit-btn", disabled: loading },
                        loading ? "Signing in..." : "Sign in"
                    )
                ),
                e(
                    "div",
                    { className: "divider" },
                    e("span", null, "Or continue with")
                ),
                e(
                    "a",
                    { href: "/oauth2/authorization/google", className: "oauth-btn" },
                    "Google"
                ),
                e(
                    "p",
                    {
                        className: `message${message ? (isError ? " error" : " success") : ""}`,
                        "aria-live": "polite"
                    },
                    message
                ),
                e(
                    "p",
                    { className: "terms" },
                    "By clicking sign in, you agree to our Terms of Service and Privacy Policy."
                )
            )
        ),
        e(
            "section",
            { className: "preview-panel", "aria-label": "Dashboard preview" },
            e(
                "div",
                { className: "preview-window" },
                e(
                    "div",
                    { className: "preview-header" },
                    e("span", null),
                    e("span", null),
                    e("span", null)
                ),
                e(
                    "div",
                    { className: "preview-body" },
                    e("aside", { className: "preview-sidebar" }),
                    e(
                        "div",
                        { className: "preview-content" },
                        e("div", { className: "preview-line wide" }),
                        e(
                            "div",
                            { className: "preview-cards" },
                            e("span", null),
                            e("span", null),
                            e("span", null)
                        ),
                        e("div", { className: "preview-chart" })
                    )
                )
            )
        )
    );
}

root.render(e(LoginPage));
