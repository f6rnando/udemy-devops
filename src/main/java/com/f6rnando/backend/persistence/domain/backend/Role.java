package com.f6rnando.backend.persistence.domain.backend;

import com.f6rnando.enums.RolesEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/************************************
 Created by f6rnando@gmail.com
 2018-02-07
 *************************************/

@Entity
public class Role implements Serializable {

    /*      FIELDS      */

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();


    /*      METHODS     */

    public Role() {
    }

    /**
     * Full constructor
     * @param rolesEnum
     */
    public Role(RolesEnum rolesEnum) {
        this.id = rolesEnum.getId();
        this.name = rolesEnum.getRoleName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != role.id) return false;
        return name != null ? name.equals(role.name) : role.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
