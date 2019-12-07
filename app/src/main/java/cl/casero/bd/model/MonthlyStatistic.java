package cl.casero.bd.model;

/**
 * Created by Patricio Pérez Pinto on 06/01/2016.
 */
public class MonthlyStatistic {
    // TODO: Añadir la palabra count a estos contadores
    private int finishedCards;
    private int newCards;
    private int maintenance;
    private int totalItems;
    private int payment;
    private int sale;

    public int getFinishedCards() {
        return finishedCards;
    }

    public void setFinishedCards(int finishedCards) {
        this.finishedCards = finishedCards;
    }

    public int getNewCards() {
        return newCards;
    }

    public void setNewCards(int newCards) {
        this.newCards = newCards;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance = maintenance;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "MonthlyStatistic{" +
                "finishedCards=" + finishedCards +
                ", newCards=" + newCards +
                ", maintenance=" + maintenance +
                ", totalItems=" + totalItems +
                ", payment=" + payment +
                ", sale=" + sale +
                '}';
    }
}
