package twitter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Logout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalidating the session
        request.getSession().invalidate();
        
        // Redirecting back to the login page or any other landing page after logout
        response.sendRedirect("Login");
    }

    @Override
    public String getServletInfo() {
        return "Logout Servlet";
    }
}