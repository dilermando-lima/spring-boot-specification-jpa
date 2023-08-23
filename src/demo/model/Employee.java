package demo.model;

import demo.base.EntityBaseWithPermission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee extends EntityBaseWithPermission  {

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "id_company", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Company company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
