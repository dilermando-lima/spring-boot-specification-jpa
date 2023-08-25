package demo.model;

import demo.base.EntityBaseWithId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission  extends EntityBaseWithId {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group_permission", nullable = false)
    private GroupPermission groupPermission;

    @Column(name = "read", nullable = false)
    private Boolean read;

    @Column(name = "update", nullable = false)
    private Boolean update;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public GroupPermission getGroupPermission() {
		return groupPermission;
	}

	public void setGroupPermission(GroupPermission groupPermission) {
		this.groupPermission = groupPermission;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Boolean getUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}
    
}
