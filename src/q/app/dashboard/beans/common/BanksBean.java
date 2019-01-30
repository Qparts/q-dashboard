package q.app.dashboard.beans.common;

import q.app.dashboard.helper.AppConstants;
import q.app.dashboard.helper.Helper;
import q.app.dashboard.model.cart.Bank;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class BanksBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Bank bank;
    private List<Bank> banks;
    private List<Bank> activeBanks;

    @Inject
    private Requester reqs;


    @PostConstruct
    private void init() {
        bank = new Bank();
        initBanks();
        initActiveBanks();
    }

    private void initBanks() {
        banks = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.GET_BANKS);
        if(r.getStatus() == 200) {
            this.banks= r.readEntity(new GenericType<List<Bank>>() {});
        }
    }

    public Bank getBankFromId(int bankId) {
        for(Bank b : banks) {
            if(b.getId() == bankId)
                return b;
        }
        return null;
    }

    private void initActiveBanks() {
        this.activeBanks = new ArrayList<>();
        for(Bank bank : banks){
            if(bank.getCustomerStatus() == 'A'){
                activeBanks.add(bank);
            }
        }
    }


    public void createBank() {
        Response r = reqs.postSecuredRequest(AppConstants.POST_PUT_DELETE_BANK, bank);
        if(r.getStatus() == 201) {
            init();
            Helper.addInfoMessage("Bank created");
        }
        else {
            Helper.addErrorMessage("Error code " + r.getStatus());
        }
    }


    public Bank getBank() {
        return bank;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public List<Bank> getActiveBanks() {
        return this.activeBanks;
    }
}
