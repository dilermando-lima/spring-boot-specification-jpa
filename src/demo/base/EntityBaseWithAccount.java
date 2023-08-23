package demo.base;

import demo.model.Account;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;

@MappedSuperclass
public abstract class EntityBaseWithAccount extends EntityBaseWithId {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account", nullable = false, updatable = false)
    private Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
    
}
