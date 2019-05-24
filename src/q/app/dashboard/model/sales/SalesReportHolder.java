package q.app.dashboard.model.sales;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesReportHolder implements Serializable {
    private List<Sales> completeSales;
    private List<Sales> incompleteSales;
    private List<SalesReturn> completeSalesReturn;
    private List<SalesReturn> incompleteSalesReturn;
    private double completeSalesTotal;
    private double completeSalesCostTotal;
    private double incompleteSalesTotal;

    private double completeSalesReturnTotal;
    private double completeSalesReturnCostTotal;
    private double incompleteSalesReturnTotal;

    public SalesReportHolder() {
        completeSales = new ArrayList<>();
        incompleteSales = new ArrayList<>();
        completeSalesReturn = new ArrayList<>();
        incompleteSalesReturn = new ArrayList<>();
    }


    public void initCalculations(){
        for(var sales : completeSales){
            completeSalesTotal += sales.getGrandTotal();
            completeSalesCostTotal += sales.getGrandTotalCost();
        }

        for(var sales :incompleteSales){
            incompleteSalesTotal += sales.getGrandTotal();
        }

        for(var salesReturn : completeSalesReturn){
            completeSalesReturnCostTotal += salesReturn.getGrandTotalCost();
            completeSalesReturnTotal += salesReturn.getGrandTotal();
        }

        for(var salesReturn : incompleteSalesReturn){
            incompleteSalesReturnTotal += salesReturn.getGrandTotal();
        }

    }


    public double getCompleteSalesTotal() {
        return completeSalesTotal;
    }


    public double getIncompleteSalesTotal() {
        return incompleteSalesTotal;
    }


    public List<Sales> getCompleteSales() {
        return completeSales;
    }

    public void setCompleteSales(List<Sales> completeSales) {
        this.completeSales = completeSales;
    }

    public List<Sales> getIncompleteSales() {
        return incompleteSales;
    }

    public void setIncompleteSales(List<Sales> incompleteSales) {
        this.incompleteSales = incompleteSales;
    }

    public double getCompleteSalesCostTotal() {
        return completeSalesCostTotal;
    }

    public List<SalesReturn> getCompleteSalesReturn() {
        return completeSalesReturn;
    }

    public List<SalesReturn> getIncompleteSalesReturn() {
        return incompleteSalesReturn;
    }

    public void setCompleteSalesReturn(List<SalesReturn> completeSalesReturn) {
        this.completeSalesReturn = completeSalesReturn;
    }

    public void setIncompleteSalesReturn(List<SalesReturn> incompleteSalesReturn) {
        this.incompleteSalesReturn = incompleteSalesReturn;
    }

    public double getCompleteSalesReturnTotal() {
        return completeSalesReturnTotal;
    }

    public double getCompleteSalesReturnCostTotal() {
        return completeSalesReturnCostTotal;
    }

    public double getIncompleteSalesReturnTotal() {
        return incompleteSalesReturnTotal;
    }
}
