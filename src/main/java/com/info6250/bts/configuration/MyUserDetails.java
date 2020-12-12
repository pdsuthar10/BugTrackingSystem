package com.info6250.bts.configuration;

import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.Role;
import com.info6250.bts.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MyUserDetails implements UserDetails {

    private User user;
    @Autowired
    private UserDAO userDAO;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        boolean isAdmin = user.isAdmin();
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        if(isAdmin)
            authorities.add(new SimpleGrantedAuthority("admin"));
        User user = userDAO.findUserByUsername("test");
        Set<Role> roles = userDAO.findRoles(user);
        for(Role role : roles){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return null;
    }

    public MyUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
