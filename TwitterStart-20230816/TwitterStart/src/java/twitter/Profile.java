package twitter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "Profile", urlPatterns = {"/Profile"})
public class Profile extends HttpServlet {
    
    private final static Logger LOGGER = Logger.getLogger(Profile.class.getName());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    if (!Login.ensureUserIsLoggedIn(request)) {
        request.setAttribute("message", "You must login.");
        response.sendRedirect("Login");
        return;
    }
    
    try {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        User user = UserModel.getUser(username); // If you've changed this method, adjust accordingly.
        
        request.setAttribute("filename", user.getFilename());
        String url = "/profile.jsp";
        getServletContext().getRequestDispatcher(url).forward(request, response);
    } catch (SQLException | ClassNotFoundException ex) {
        LOGGER.log(Level.SEVERE, "An error occurred while fetching the user profile.", ex);
        response.sendRedirect("error.jsp"); // Redirect to an error page or handle it as per your requirement.
    }
}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
