package com.ssafy.bartter.community.service;

import com.ssafy.bartter.domain.community.dto.CommunityPostCommentDto;
import com.ssafy.bartter.domain.community.entity.CommunityPost;
import com.ssafy.bartter.domain.community.entity.CommunityPostComment;
import com.ssafy.bartter.domain.community.repository.CommunityPostCommentRepository;
import com.ssafy.bartter.domain.community.repository.CommunityPostRepository;
import com.ssafy.bartter.domain.community.service.CommunityPostCommentService;
import com.ssafy.bartter.domain.user.entity.User;
import com.ssafy.bartter.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @Author 김가람
 *
 * CommunityPostCommentServiceTest 테스트
 * */
@ExtendWith(MockitoExtension.class)
class CommunityPostCommentServiceTest {

    @Mock
    CommunityPostRepository communityPostRepository;

    @Mock
    UserRepository userRepository;

    @Spy
    CommunityPostCommentRepository communityPostCommentRepository;

    @InjectMocks
    CommunityPostCommentService communityPostCommentService;

    @DisplayName("동네모임 게시글에 댓글을 작성한다.")
    @Test
    void 동네모임_댓글_작성() {
        // given
        User mockUser = mock(User.class);
        CommunityPost mockPost = mock(CommunityPost.class);
        given(userRepository.findById(1)).willReturn(Optional.of(mockUser));
        given(communityPostRepository.findById(1)).willReturn(Optional.of(mockPost));

        CommunityPostComment mockComment = new CommunityPostComment("content");
        mockComment.addUser(mockUser);
        mockComment.addCommunityPost(mockPost);
        given(communityPostCommentRepository.save(any(CommunityPostComment.class))).willReturn(mockComment);

        // when
        CommunityPostComment comment = communityPostCommentService.createComment(1, new CommunityPostCommentDto.Create("content"), 1);

        // then
        assertThat(comment).isNotNull();
        assertThat(comment.getUser()).isEqualTo(mockUser);
        assertThat(comment.getCommunityPost()).isEqualTo(mockPost);
        assertThat(comment.getContent()).isEqualTo("content");
    }

    @DisplayName("동네모임 게시글에 댓글을 삭제한다.")
    @Test
    void 동네모임_댓글_삭제() {
        // given
        CommunityPostComment comment = mock(CommunityPostComment.class);
        User mockUser = mock(User.class);

        given(mockUser.getId()).willReturn(1);
        given(comment.getUser()).willReturn(mockUser);
        given(communityPostCommentRepository.findById(1)).willReturn(Optional.of(comment));

        // when
        communityPostCommentService.deleteComment(1, 1);

        // then
        verify(communityPostCommentRepository, times(1)).delete(comment);
    }
}