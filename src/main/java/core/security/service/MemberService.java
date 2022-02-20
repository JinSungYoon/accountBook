package core.security.service;


import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.security.domain.entity.MemberEntity;
import core.security.domain.repository.MemberRepository;
import core.security.dto.MemberDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
	private MemberRepository memberRepository;

	 @Transactional
    public Long joinUser(MemberDto memberDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        memberDto.setCreatedDate(LocalDateTime.now());
        memberDto.setUpdatedDate(LocalDateTime.now());
        
        return memberRepository.save(memberDto.toEntity()).getId();
    }
	
	@Override
	public MemberEntity loadUserByUsername(String userEmail) throws UsernameNotFoundException {
	    Optional<MemberEntity> userEntityWrapper = memberRepository.findByEmail(userEmail);
	    MemberEntity userEntity = userEntityWrapper.get();
	
	    return userEntity;
	}  
    
}
