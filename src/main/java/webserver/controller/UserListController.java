package webserver.controller;

import core.db.DataBase;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import core.model.User;
import core.route.WebAppPath;

public class UserListController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {

    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        boolean isLoginSuccess = request.getCookies("login").map(Boolean::parseBoolean).orElse(false);

        // 로그인 상태면 사용자목록 출력
        if(isLoginSuccess) {
            response.forwardBody(getUserListHtml());
        }

        // 로그인 상태가 아니면 로그인 페이지로 이동
        response.redirect(WebAppPath.LOGIN_PAGE);
    }

    private String getUserListHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table><tr><th>userId</th><th>userName</th><th>email</th></tr>");
        DataBase.findAll().forEach(u -> sb.append(getUserHtml(u)));
        sb.append("</table>");
        return sb.toString();
    }

    private String getUserHtml(User user) {
        return String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", user.getUserId(), user.getName(), user.getEmail());
    }
}
