package learn.genericBackend.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Email is required")
    @Email(message = "Must be valid email")
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(int id, String username, String password, String email, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return convertRolesToAuthorities(List.of(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public static List<GrantedAuthority> convertRolesToAuthorities(List<UserRole> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (roles.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority(""));
        } else {
            for (UserRole role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            }
        }

        return authorities;
    }

    public static List<UserRole> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(a -> UserRole.valueOf(a.getAuthority().substring("ROLE_".length())))
                .toList();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User member = (User) o;
        return id == member.id && Objects.equals(username, member.username) && Objects.equals(password, member.password) && Objects.equals(email, member.email) && role == member.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, role);
    }
}
