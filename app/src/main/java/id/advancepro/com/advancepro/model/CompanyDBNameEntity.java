package id.advancepro.com.advancepro.model;

import java.io.Serializable;

/**
 * Created by TASIV on 3/15/2017.
 */

// Company DB Entity
public class CompanyDBNameEntity implements Serializable{

    private String companyDBName;
    private String companyName;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;
    public String getCompanyDBName() {
        return companyDBName;
    }

    public void setCompanyDBName(String companyDBName) {
        this.companyDBName = companyDBName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return companyName;
    }


}
