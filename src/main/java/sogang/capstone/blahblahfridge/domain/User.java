package sogang.capstone.blahblahfridge.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@Table(name = "User")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "reviews")
public class User extends AbstractTimestamp implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String username;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "authentication_code", nullable = false, unique = true)
    private String authenticationCode;

    @Column(nullable = false, length = 10)
    private String provider;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @Builder
    public User(
        Long id,
        String username,
        String image,
        String authenticationCode,
        String provider
    ) {
        if (username == null) {
            throw new RuntimeException("이름은 필수값입니다.");
        }
        if (authenticationCode == null) {
            throw new RuntimeException("인증 코드는 필수값입니다.");
        }
        if (provider == null) {
            throw new RuntimeException("provider는 필수값입니다.");
        }

        this.id = id;
        this.username = username;
        this.image = image;
        this.authenticationCode = authenticationCode;
        this.provider = provider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}