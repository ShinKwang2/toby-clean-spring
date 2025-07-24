package tobyspring.splearn.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
@Entity
public class Member extends AbstractEntity {

    @NaturalId
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private MemberDetail detail;

    public static Member register(MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(Objects.requireNonNull(registerRequest.email()));
        member.nickname = Objects.requireNonNull(registerRequest.nickname());
        member.passwordHash = Objects.requireNonNull(passwordEncoder.encode(registerRequest.password()));

        member.status = MemberStatus.PENDING;

        return member;
    }

    public void activate() {
        // if (status != MemberStatus.PENDING) throw new IllegalStateException("PENDING 상태가 아닙니다");
        Assert.state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        Assert.state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 압니다.");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(Objects.requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
