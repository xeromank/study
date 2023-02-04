package com.example.study.basic.domain;


import com.example.study.basic.mapper.UsersMapper;
import com.example.study.common.convertor.UserRolesConverter;
import com.example.study.common.dto.users.SignWithRequest;
import com.example.study.common.dto.users.SignWithResponse;
import com.example.study.common.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode
public class Users implements Serializable {

    @Serial
    private static final long serialVersionUID = 2749103380280749779L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;

    @Column(length = 40)
    private String userName;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 40, unique = true, updatable = false)
    private String signInId;

    @Column(length = 200)
    private String signInPw;

    @Column
    private Long signInCount;

    @Column
    private Integer errorCount;

    @Convert(converter = UserRolesConverter.class)
    private List<UserRole> userRoles;

    @CreatedBy
    @Column(updatable = false)
    protected Long createdBy;

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    @Column
    protected Long modifiedBy;

    @LastModifiedDate
    @Column
    protected LocalDateTime modifiedDate;

    public static Users signWith(SignWithRequest signWithRequest){

        return UsersMapper.INSTANCE.signWithToUsers(signWithRequest);

//        return Users.builder()
//            .signInId(signWithRequest.getSignInId())
//            .signInPw(signWithRequest.getSignInPw())
//            .userName(signWithRequest.getUserName())
//            .userRoles(Arrays.asList(UserRole.ROLE_USERS))
//            .build();
    }

    public SignWithResponse signWithResponse() {
        return UsersMapper.INSTANCE.toSignWithResponse(this);
    }
}
