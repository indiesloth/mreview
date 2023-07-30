package org.zerock.mreview.repository;

import java.util.stream.IntStream;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.entity.Member;

@SpringBootTest
class MemberRepositoryTests {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  @Test
  public void insertMembers() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Member member = Member.builder()
          .email("r" + i + "@zerock.org")
          .pw("1111")
          .nickname("reviewer" + i)
          .build();

      memberRepository.save(member);
    });
  }

  @Commit
  @Transactional
  @Test
  public void testDeleteMember() {
    Long mid = 1L;

    Member member = Member.builder().mid(mid).build();

    //FK를 먼저 삭제하고 PK를 삭제해야함 -> review 삭제 후 member 삭제
    reviewRepository.deleteByMember(member);
    memberRepository.deleteById(mid);
  }
}