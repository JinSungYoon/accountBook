package core.security.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name = "member")
public class MemberEntity implements UserDetails {
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;
    
    @Column(length = 100, nullable = false)
    private String userName;
    
    @Column(length = 1, nullable = true)
    private String gender;
    
    @Column(length = 3, nullable = true)
    private Integer age;
    
    @Column(length = 100, nullable = false)
    private String auth;
    
    @CreatedDate
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    private LocalDateTime updatedDate;
    
    @Builder
    public MemberEntity(Long id, String email, String password, String userName,String gender,Integer age,String auth,LocalDateTime createdDate,LocalDateTime updatedDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.auth = auth;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> roles = new HashSet<>();
	    for (String role : auth.split(",")) {
	      roles.add(new SimpleGrantedAuthority(role));
	    }
	    return roles;
	}


	@Override
	public String getUsername() {
		return userName;
	}

	 // 계정 만료 여부 반환
	  @Override
	  public boolean isAccountNonExpired() {
	    // 만료되었는지 확인하는 로직
	    return true; // true -> 만료되지 않았음
	  }

	  // 계정 잠금 여부 반환
	  @Override
	  public boolean isAccountNonLocked() {
	    // 계정 잠금되었는지 확인하는 로직
	    return true; // true -> 잠금되지 않았음
	  }

	  // 패스워드의 만료 여부 반환
	  @Override
	  public boolean isCredentialsNonExpired() {
	    // 패스워드가 만료되었는지 확인하는 로직
	    return true; // true -> 만료되지 않았음
	  }

	  // 계정 사용 가능 여부 반환
	  @Override
	  public boolean isEnabled() {
	    // 계정이 사용 가능한지 확인하는 로직
	    return true; // true -> 사용 가능
	  }
}