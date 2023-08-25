package demo.base;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class EntityBaseWithId {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "date_insert", nullable = false, updatable = false)
    private ZonedDateTime dateInsert;

    @Column(name = "date_last_update", nullable = false)
    private ZonedDateTime dateLastUpdate;

    @PrePersist
    public void prePersist(){
        dateInsert = ZonedDateTime.now();
        dateLastUpdate = ZonedDateTime.now();
    }

	public static <T> T newWithId(String id, Class<T> type){
		var entityBaseWithId = new EntityBaseWithId() {};
		entityBaseWithId.setId(id);
		return type.cast(entityBaseWithId);
	}

    @PreUpdate
    public void preUpdate(){
        dateLastUpdate = ZonedDateTime.now();
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ZonedDateTime getDateInsert() {
		return dateInsert;
	}

	public void setDateInsert(ZonedDateTime dateInsert) {
		this.dateInsert = dateInsert;
	}

	public ZonedDateTime getDateLastUpdate() {
		return dateLastUpdate;
	}

	public void setDateLastUpdate(ZonedDateTime dateLastUpdate) {
		this.dateLastUpdate = dateLastUpdate;
	}


}
