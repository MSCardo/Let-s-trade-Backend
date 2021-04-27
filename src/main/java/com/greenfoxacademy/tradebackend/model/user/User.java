package com.greenfoxacademy.tradebackend.model.user;

import com.greenfoxacademy.tradebackend.model.stock.Stock;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class User implements UserDetails {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String username;
  private String email;
  private String password;
  @Enumerated(EnumType.STRING)
  private UserRole userRole;
  private Boolean locked = false;
  private Boolean enabled = false;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Stock> stock;
  private Double balance;

  public User(String username,
              String email,
              String password,
              UserRole userRole) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.userRole = userRole;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    SimpleGrantedAuthority authority =
        new SimpleGrantedAuthority(userRole.name());
    return Collections.singletonList(authority);
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
