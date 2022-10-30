
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ming.chen.SkierServlet", value = "/ming.chen.SkierServlet")
public class SkierServlet extends HttpServlet {

    private ObjectPool<Channel> pool;
    private final static String QUEUE_NAME = "SkierQueue";

    @Override
    public void init() throws ServletException {
        this.pool = new GenericObjectPool<Channel>(new RabbitMQChannelFactory());
    }

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
            res.getWriter().write("It works!!!");
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
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        String urlPath = req.getPathInfo();

        // check we have a URL
        if (urlPath == null || urlPath.isEmpty()) {
            res.getWriter().write("missing parameter");
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String[] urlParts = urlPath.split("/");


        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            try {
                JsonObject skier = getJson(urlParts);
                Channel channel = null;
                try {
                    channel = pool.borrowObject();
                    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    channel.basicPublish("", QUEUE_NAME, null, skier.toString().getBytes());
                } catch (Exception e) {
                    System.out.println("Errors occurs when get channel from pool:" + e);
                } finally {
                    try {
                        if (channel != null) {
                            pool.returnObject(channel);
                        }
                    } catch (Exception e) {
                        System.out.println("Error occurs when returning channel");
                    }
                }
                res.setStatus(HttpServletResponse.SC_CREATED);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            System.out.println("baby");
            res.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    private JsonObject getJson(String[] url) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("resortID", Integer.valueOf(url[1]));
        jsonObject.addProperty("seasonID", Integer.valueOf(url[3]));
        jsonObject.addProperty("dayID", Integer.valueOf(url[5]));
        jsonObject.addProperty("skierID", Integer.valueOf(url[7]));
        return jsonObject;
    }
}
