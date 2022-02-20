package core.security.dto;

import lombok.*;

import java.time.LocalDateTime;

import core.security.domain.entity.MemberEntity;


@Data
@ToString
@NoArgsConstructor
public class MemberDto {
	private Long id;
    private String email;
    private String password;
    private String userName;
    private String gender;
    private Integer age;
    private String auth;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .id(id)
                .email(email)
                .password(password)
                .userName(userName)
                .gender(gender)
                .age(age)
                .auth(auth)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    @Builder
    public MemberDto(Long id, String email, String password,String userName,String gender,Integer age,String auth,LocalDateTime createdDate,LocalDateTime updatedDate) {
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
}