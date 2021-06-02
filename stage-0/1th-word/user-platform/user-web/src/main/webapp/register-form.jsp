<head>
    <jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf"/>
    <title>Create your account</title>
    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <form action="/register/submit">
        <h1 class="h3 mb-3 font-weight-normal">注册</h1>

        <label class="col-form-label" for="inputName">名称:</label>
        <input id="inputName" name="name" type="text" class="form-control" placeholder="请输入名称" required autofocus>

        <label for="inputEmail" class="col-form-label">电子邮件:</label>
        <input id="inputEmail" name="email" type="email" class="form-control" placeholder="请输入电子邮件" required autofocus>

        <label for="inputPassword" class="col-form-label">Password:</label>
        <input id="inputPassword" name="password" type="password" class="form-control" placeholder="请输入密码" required>

        <label for="inputPhoneNumber" class="col-form-label">电话号码:</label>
        <input id="inputPhoneNumber" name="phoneNumber" type="tel" class="form-control" placeholder="请输入电话号码" required>

<%--        <div class="checkbox mb-3">
            <label> <input type="checkbox" value="remember-me">
                Remember me
            </label>
        </div>--%>

        <button class="btn btn-lg btn-primary btn-block" style="margin-top: 40px" type="submit">Sign up</button>
        <p class="mt-5 mb-3 text-muted">&copy; 2017-2021</p>
    </form>
</div>
</body>