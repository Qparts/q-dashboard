package q.app.dashboard.helper;

import q.app.dashboard.model.customer.Customer;
import q.app.dashboard.model.quotation.Quotation;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
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

    public static long[] getCustomerIds(List<Quotation> quotations) {
        long[] ids = new long[quotations.size()];
        for(int i = 0; i < quotations.size(); i++) {
            ids[i] = quotations.get(i).getCustomerId();
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


    private static Thread appendCustomer(List<Customer> customers, Quotation quotation){
        Thread thread = new Thread(() -> {
            try {
                for (Customer c : customers) {
                    if (c.getId() == quotation.getCustomerId()) {
                        quotation.setCustomer(c);
                        break;
                    }
                }

            } catch (Exception ignore) {

            }
        });
        return thread;
    }

    public static void appendCustomersToQuotations(List<Customer> customers, List<Quotation> quotations) throws InterruptedException  {
        Thread[] mainThreads = new Thread[quotations.size()];
        int index = 0;
        for (Quotation quotation : quotations) {
            Thread t = new Thread(() -> {
                Thread[] threads = new Thread[1];
                threads[0] = appendCustomer(customers, quotation);
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