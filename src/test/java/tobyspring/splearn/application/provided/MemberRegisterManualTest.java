package tobyspring.splearn.application.provided;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splearn.application.MemberService;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.MemberStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class MemberRegisterManualTest {

    @Test
    void registerTestStub() {
        MemberService register = new MemberService(
                new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestMock() {
        EmailSenderMock emailSenderMock = new EmailSenderMock();
        MemberService register = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSenderMock.getTos()).hasSize(1);
        assertThat(emailSenderMock.tos.getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    void registerTestMockito() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
        MemberService register = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSenderMock).send(ArgumentMatchers.eq(member.getEmail()), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }

        @Override
        public Optional<Member> findByEmail(Email email) {
            return Optional.empty();
        }
    }

    static class EmailSenderMock implements EmailSender {
        List<Email> tos = new ArrayList<>();

        public List<Email> getTos() {
            return tos;
        }

        @Override
        public void send(Email email, String subject, String body) {
            tos.add(email);
        }
    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {
        }
    }

}