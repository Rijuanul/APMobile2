package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 5/11/2017.
 */

public class CycleCountPickingLocationEntity {

    private Integer id;
    private String  name;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
