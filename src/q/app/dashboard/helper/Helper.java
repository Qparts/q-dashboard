package q.app.dashboard.helper;

import q.app.dashboard.beans.cart.WireTransfersBean;
import q.app.dashboard.model.cart.Cart;
import q.app.dashboard.model.cart.CartWireTransferRequest;
import q.app.dashboard.model.cart.CustomerWallet;
import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.quotation.Quotation;
import q.app.dashboard.model.sales.Sales;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Helper {

    private static String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxuz1234567890";

    public static String removeNoneAlphaNumeric(String string) {
        return string.replaceAll("[^A-Za-z0-9]", "");
    }

    public static String properTag(String tag){
        return tag.toLowerCase().trim().replaceAll(" ", "_");
    }


    public static void redirect(String path) {
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect(path);
            return;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public static String inputStreamToBase64(InputStream is) throws IOException {
        return Base64.getEncoder().encodeToString(inputStreamToBytesArray(is));
    }

    public static byte[] inputStreamToBytesArray(InputStream is) throws IOException {
        final byte[] bytes;
        try(is){
            bytes = is.readAllBytes();
            return bytes;
        } catch(Exception e){
        }
        return new byte[0];

    }

    public static String getEncodedUrl(String url){
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    public static int[] getCalendarArray(Date date){
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        int[] vals = new int[3];
        vals[0] = c.get(Calendar.YEAR);
        vals[1]= c.get(Calendar.MONTH) + 1;
        vals[2] = c.get(Calendar.DAY_OF_MONTH);
        return vals;
    }

    public static String getRandomSaltString(){
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }


    public static long[] getCustomerIds(List objects){
        long[] ids = new long[objects.size()];
        for(int i = 0; i < objects.size(); i++) {
            if(objects.get(i) instanceof Quotation){
                ids[i] = ((Quotation) objects.get(i)).getCustomerId();
            }else if(objects.get(i) instanceof Cart){
                ids[i] = ((Cart) objects.get(i)).getCustomerId();
            }else if(objects.get(i) instanceof CartWireTransferRequest){
                ids[i] = ((CartWireTransferRequest) objects.get(i)).getCustomerId();
            }else if(objects.get(i) instanceof CustomerWallet){
                ids[i] = ((CustomerWallet) objects.get(i)).getCustomerId();
            }
            else if(objects.get(i) instanceof Sales){
                ids[i] = ((Sales) objects.get(i)).getCustomerId();
            }

        }
        return ids;
    }

    public static List<Integer> getYearsRange(int from, int to){
        List<Integer> ints = new ArrayList<Integer>();
        for(int i = from; i <= to; i++) {
            ints.add(i);
        }
        return ints;
    }


    public static String getParam(String qkey){
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestParameterMap().get(qkey);
    }

    public static void addWarMessage(String text) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, text, null));
    }

    public static void addInfoMessage(String text) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, text, null));
    }

    public static void addErrorMessage(String text) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, text, null));
    }

    public static void addErrorMessage(String text, String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, text, null));
    }


    private static Thread appendCustomer(List<Customer> customers, Object object){
        Thread thread = new Thread(() -> {
            try {
                for (Customer c : customers) {
                    if(object instanceof Quotation){
                        Quotation quotation = (Quotation) object;
                        if (c.getId() == quotation.getCustomerId()) {
                            quotation.setCustomer(c);
                            break;
                        }
                    }

                    else if(object instanceof Cart){
                        Cart cart = (Cart) object;
                        if (c.getId() == cart.getCustomerId()) {
                            cart.setCustomer(c);
                            break;
                        }
                    }

                    else if(object instanceof CartWireTransferRequest){
                        CartWireTransferRequest wire = (CartWireTransferRequest) object;
                        if (c.getId() == wire.getCustomerId()) {
                            wire.getCart().setCustomer(c);
                            break;
                        }
                    }

                    else if(object instanceof CustomerWallet){
                        CustomerWallet wallet = (CustomerWallet) object;
                        if (c.getId() == wallet.getCustomerId()) {
                            wallet.setCustomer(c);
                            break;
                        }
                    }
                    else if(object instanceof Sales){
                        Sales sales = (Sales) object;
                        if (c.getId() == sales.getCustomerId()) {
                            sales.setCustomer(c);
                            break;
                        }
                    }

                }

            } catch (Exception ignore) {

            }
        });
        return thread;
    }

    public static void appendCustomers(List<Customer> customers, List vars) throws InterruptedException  {
        Thread[] mainThreads = new Thread[vars.size()];
        int index = 0;
        for (Object object : vars) {
            Thread t = new Thread(() -> {
                Thread[] threads = new Thread[1];
                threads[0] = appendCustomer(customers, object);
                for (int i = 0; i < threads.length; i++)
                    try {
                        threads[i].start();
                        threads[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            });
            mainThreads[index] = t;
            index++;
        }
        for (int i = 0; i < mainThreads.length; i++) {
            mainThreads[i].start();
            mainThreads[i].join();
        }
    }


}
