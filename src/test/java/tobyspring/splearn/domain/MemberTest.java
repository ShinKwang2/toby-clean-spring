package tobyspring.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void createMember() {
        var member = new Member("shin@splearn.app", "Shin", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }
}