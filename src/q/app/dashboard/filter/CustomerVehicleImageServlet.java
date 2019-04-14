package q.app.dashboard.filter;


import q.app.dashboard.helper.AWSClient;
import q.app.dashboard.helper.AppConstants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@WebServlet("/app/customer-vehicle/*")
public class CustomerVehicleImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res){
        try{
            String filename = req.getPathInfo().substring(1);
            String mimeType = getServletContext().getMimeType(filename);
            copyFile(filename, mimeType, res);
        }catch(Exception ignore){
        }
    }

    public static void copyFile(String filename, String mimeType, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        Map map = AWSClient.retreiveImage(filename, AppConstants.CUSTOMER_VEHICLE_BUCKET_NAME);
        InputStream is = (InputStream) map.get("inputStream");
        Long size = (Long) map.get("size");
        System.out.println(size);
        response.setHeader("Content-Type", mimeType);
        response.setHeader("Content-Length", String.valueOf(size));
        is.transferTo(response.getOutputStream());
        response.getOutputStream().close();
        is.close();
    }
}
