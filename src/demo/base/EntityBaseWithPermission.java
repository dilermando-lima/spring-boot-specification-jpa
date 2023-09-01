package demo.base;

import demo.model.GroupPermission;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;

@MappedSuperclass
public abstract class EntityBaseWithPermission extends EntityBaseWithAccount {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group_permission", nullable = false)
    protected GroupPermission groupPermission;

	public GroupPermission getGroupPermission() {
		return groupPermission;
	}

	public void setGroupPermission(GroupPermission groupPermission) {
		this.groupPermission = groupPermission;
	}

}
