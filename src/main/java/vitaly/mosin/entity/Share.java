package vitaly.mosin.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Share {
    @Id
    private int edrpou;
    private int quantity;
    private double price;
    private double cost;
    private LocalDate date;
    private String comment;
    private boolean status;

    public Share() {
    }

    public int getEdrpou() {
        return edrpou;
    }

    public void setEdrpou(int edrpou) {
        this.edrpou = edrpou;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Share share = (Share) o;
        return edrpou == share.edrpou &&
                quantity == share.quantity &&
                price == share.price &&
                cost == share.cost &&
                Objects.equals(date, share.date) &&
                Objects.equals(comment, share.comment) &&
                Objects.equals(status, share.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edrpou, quantity, price, cost, date, comment, status);
    }

    @Override
    public String toString() {
        return "Share{" +
                "edrpou=" + edrpou +
                ", quantity=" + quantity +
                ", price=" + price +
                ", cost=" + cost +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                ", status='" + (status ? "Active" : "Deleted") + '\'' +
                '}';
    }
}

