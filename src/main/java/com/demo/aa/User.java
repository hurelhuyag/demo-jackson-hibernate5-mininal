package com.demo.aa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class User {

    @Id
    private Long id;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<SomeData> data;

    public User() {

    }

    public User(Long id, Role role, List<SomeData> data) {
        this.id = id;
        this.role = role;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<SomeData> getData() {
        return data;
    }

    public void setData(List<SomeData> data) {
        this.data = data;
    }
}
