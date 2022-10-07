import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.getWriter().write("missing parameter");
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("NOT FOUND");
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`
            res.getWriter().write("It works!");
        }
    }

    private boolean isUrlValid(String[] urlPath) {
        // urlPath  = "/1/seasons/2019/day/1/skier/123"
        // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
        // Handle /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
        if (urlPath.length == 8) {
            if (!urlPath[0].equals("")
                    || !urlPath[2].equals("seasons")
                    || !urlPath[4].equals("days")
                    || !urlPath[6].equals("skiers")) {
                return false;
            }
        }

        return urlPath.length == 8;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String urlPath = req.getPathInfo();
        BufferedReader bufferedReader = req.getReader();


        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.getWriter().write("missing parameter");
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String[] urlParts = urlPath.split("/");


        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            // If path is valid, we send 201 code back to client
            res.setStatus(HttpServletResponse.SC_CREATED);
        }

        System.out.println("It works!");
        System.out.println(urlPath);
    }
}
