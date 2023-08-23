package demo.model;

import demo.base.EntityBaseWithAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User  extends EntityBaseWithAccount {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profile", length = 2, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserProfile userProfile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
