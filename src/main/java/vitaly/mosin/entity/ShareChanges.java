package vitaly.mosin.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ShareChanges {
    @Id
    private int edrpou;
    private String fName;
    private String oldValue;
    private String newValue;

    public ShareChanges() {
    }

    public int getEdrpou() {
        return edrpou;
    }

    public void setEdrpou(int edrpou) {
        this.edrpou = edrpou;
    }

    @Override
    public String toString() {
        return "ShareChanges{" +
                "edrpou=" + edrpou +
                ", fName='" + fName + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
