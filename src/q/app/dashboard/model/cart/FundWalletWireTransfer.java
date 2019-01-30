package q.app.dashboard.model.cart;

public class FundWalletWireTransfer {
    private CartWireTransferRequest wireTransfer;
    private CustomerWallet wallet;

    public CartWireTransferRequest getWireTransfer() {
        return wireTransfer;
    }

    public void setWireTransfer(CartWireTransferRequest wireTransfer) {
        this.wireTransfer = wireTransfer;
    }

    public CustomerWallet getWallet() {
        return wallet;
    }

    public void setWallet(CustomerWallet wallet) {
        this.wallet = wallet;
    }
}
