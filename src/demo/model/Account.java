package demo.model;

import demo.base.EntityBaseWithId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account extends EntityBaseWithId {

    @Column(name = "name", nullable = false)
    private String name;

    public Account(){}

    public Account(String name) {
		this.name = name;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
