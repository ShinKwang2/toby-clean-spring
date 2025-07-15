package tobyspring.splearn.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
@ToString
public class Member {
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private Member() {}

    public static Member create(MemberCreateRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        String email = createRequest.email();

//        Pattern EMAIL_PATTERN =
//                Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
//        if (!EMAIL_PATTERN.matcher(email).matches()) {
//            throw new IllegalArgumentException("이메일 형식이 바르지 않습니다: " + email);
//        }

        member.email = Objects.requireNonNull(createRequest.email());
        member.nickname = Objects.requireNonNull(createRequest.nickname());
        member.passwordHash = Objects.requireNonNull(passwordEncoder.encode(createRequest.password()));

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
