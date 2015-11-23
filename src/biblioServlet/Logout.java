package biblioServlet;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** Classe pour logout systeme de gestion de bibliotheque
 */

public class Logout extends HttpServlet
{
public void doGet(HttpServletRequest request,
                  HttpServletResponse response)
    throws ServletException, IOException
{
// invalider la session pour liberer les ressources associees e la session
request.getSession().invalidate();
RequestDispatcher dispatcher =
  request.getRequestDispatcher("/login.jsp");
dispatcher.forward(request, response);
}

public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException
{
doGet(request, response);
}
}
