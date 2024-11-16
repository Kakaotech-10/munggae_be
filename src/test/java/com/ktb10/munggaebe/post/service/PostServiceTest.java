package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.domain.PostImage;
import com.ktb10.munggaebe.image.service.ImageService;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.exception.PostNotFoundException;
import com.ktb10.munggaebe.post.repository.PostRepository;
import com.ktb10.munggaebe.post.service.dto.PostServiceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FilteringService filteringService;

    @Mock
    private ImageService imageService;

    @BeforeEach
    void setup() {
        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        setupSecurityContextWithRole(member, "STUDENT");
    }

    private void setupSecurityContextWithRole(Member member, String role) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                member, null, Collections.singleton(() -> "ROLE_" + role)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("게시물 목록을 성공적으로 가져온다")
    void getPosts_ShouldReturnPosts() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Post> postPage = new PageImpl<>(List.of(Post.builder().build()));
        given(postRepository.findAll(pageable)).willReturn(postPage);

        // when
        Page<Post> result = postService.getPosts(0, 10);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(1);
        verify(postRepository).findAll(pageable);
    }

    @Test
    @DisplayName("게시물을 ID로 성공적으로 가져온다")
    void getPost_ShouldReturnPost_WhenPostExists() {
        // given
        long postId = 1L;
        Post post = Post.builder().id(postId).build();
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        Post result = postService.getPost(postId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(postId);
        verify(postRepository).findById(postId);
    }

    @Test
    @DisplayName("게시물을 찾을 수 없을 때 예외를 발생시킨다")
    void getPost_ShouldThrowException_WhenPostNotFound() {
        // given
        long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시물을 성공적으로 생성한다")
    void createPost_ShouldCreatePost_WhenValidInput() {
        // given
        long memberId = 1L;
        Member member = Member.builder().id(memberId).build();
        Post post = Post.builder().title("Title").content("Content").build();

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(postRepository.save(any(Post.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Post result = postService.createPost(post);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMember()).isEqualTo(member);
        assertThat(result.getTitle()).isEqualTo("Title");
        verify(memberRepository).findById(memberId);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물을 생성할 때 회원을 찾을 수 없으면 예외를 발생시킨다")
    void createPost_ShouldThrowException_WhenMemberNotFound() {
        // given
        Post post = Post.builder().title("Title").content("Content").build();

        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.createPost(post))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("게시물을 성공적으로 수정한다")
    void updatePost_ShouldUpdatePost_WhenAuthorized() {
        // given
        long postId = 1L;
        Post post = Post.builder().id(postId).member(Member.builder().id(1L).role(MemberRole.STUDENT).build()).build();
        PostServiceDto.UpdateReq updateReq = new PostServiceDto.UpdateReq(postId, "Updated Title", "Updated Content");

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        Post result = postService.updatePost(updateReq);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        verify(postRepository).findById(postId);
    }

    @Test
    @DisplayName("게시물을 수정할 때 게시물을 찾을 수 없으면 예외를 발생시킨다")
    void updatePost_ShouldThrowException_WhenPostNotFound() {
        // given
        long postId = 1L;
        PostServiceDto.UpdateReq updateReq = new PostServiceDto.UpdateReq(postId, "Updated Title", "Updated Content");

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.updatePost(updateReq))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시물을 수정할 때 권한이 없으면 예외를 발생시킨다")
    void updatePost_ShouldThrowException_WhenUnauthorized() {
        // given
        long postId = 1L;
        Post post = Post.builder().id(postId).member(Member.builder().id(2L).role(MemberRole.STUDENT).build()).build();
        PostServiceDto.UpdateReq updateReq = new PostServiceDto.UpdateReq(postId, "Updated Title", "Updated Content");

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when & then
        assertThatThrownBy(() -> postService.updatePost(updateReq))
                .isInstanceOf(MemberPermissionDeniedException.class);
    }

    @Test
    @DisplayName("게시물을 수정할 때 매니저이면 성공적으로 수정한다")
    void updatePost_ShouldUpdatePost_WhenRoleManager() {
        // given
        long postId = 1L;
        long managerId = 2L;
        Member manager = Member.builder().id(managerId).role(MemberRole.MANAGER).build();
        Post post = Post.builder().id(postId).member(Member.builder().id(1L).build()).build();
        PostServiceDto.UpdateReq updateReq = new PostServiceDto.UpdateReq(postId, "Updated Title", "Updated Content");

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        setupSecurityContextWithRole(manager, "MANAGER");

        // when
        Post result = postService.updatePost(updateReq);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        verify(postRepository).findById(postId);
    }

    @Test
    @DisplayName("게시물을 성공적으로 삭제한다")
    void deletePost_ShouldDeletePost_WhenAuthorized() {
        // given
        long postId = 1L;

        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        Post post = Post.builder().id(postId).member(member).build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        setupSecurityContextWithRole(member, "STUDENT");

        // when
        postService.deletePost(postId);

        // then
        verify(postRepository).deleteById(postId);
    }

    @Test
    @DisplayName("게시물을 삭제할 때 게시물을 찾을 수 없으면 예외를 발생시킨다")
    void deletePost_ShouldThrowException_WhenPostNotFound() {
        // given
        long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.deletePost(postId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시물을 삭제할 때 권한이 없으면 예외를 발생시킨다")
    void deletePost_ShouldThrowException_WhenUnauthorized() {
        // given
        long postId = 1L;
        Member student = Member.builder().id(2L).role(MemberRole.STUDENT).build();
        Post post = Post.builder().id(postId).member(Member.builder().id(1L).build()).build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        setupSecurityContextWithRole(student, "STUDENT");

        // when & then
        assertThatThrownBy(() -> postService.deletePost(postId))
                .isInstanceOf(MemberPermissionDeniedException.class);
    }

    @Test
    @DisplayName("게시물을 삭제할 때 매니저이면 성공적으로 삭제한다")
    void deletePost_ShouldDeletePost_WhenRoleManager() {
        // given
        long postId = 1L;
        Member manager = Member.builder().id(2L).role(MemberRole.MANAGER).build();
        Post post = Post.builder().id(postId).member(Member.builder().id(1L).build()).build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        setupSecurityContextWithRole(manager, "MANAGER");

        // when
        postService.deletePost(postId);

        // then
        verify(postRepository).deleteById(postId);
    }

    @Test
    @DisplayName("파일 이름 리스트로 Presigned URL을 성공적으로 생성한다.")
    void generatePresignedUrls_success() {
        // Given
        long postId = 1L;
        List<String> fileNames = List.of("file1.jpg", "file2.jpg");

        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        Post post = Post.builder().id(postId).member(member).build();
        setupSecurityContextWithRole(member, "STUDENT");

        given(postRepository.findById(postId)).willReturn(Optional.ofNullable(post));
        given(imageService.getPresignedUrl(anyString(), eq(postId), eq(ImageType.POST)))
                .willReturn("url");

        // When
        List<PostServiceDto.PresignedUrlRes> result = postService.getPresignedUrl(postId, fileNames);

        // Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst().getUrl()).isEqualTo("url");

        then(imageService).should(times(2))
                .getPresignedUrl(anyString(), eq(postId), eq(ImageType.POST));
    }

    @Test
    @DisplayName("게시물을 찾을 수 없는 경우 PostNotFoundException을 발생시킨다.")
    void getPresignedUrl_postNotFound() {
        // Given
        long postId = 1L;
        List<String> fileNames = List.of("file1.jpg", "file2.jpg");

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.getPresignedUrl(postId, fileNames))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("권한이 없는 사용자가 접근할 경우 MemberPermissionDeniedException을 발생시킨다.")
    void getPresignedUrl_permissionDenied() {
        // Given
        long postId = 1L;
        List<String> fileNames = List.of("file1.jpg", "file2.jpg");

        Member member = Member.builder().id(2L).role(MemberRole.STUDENT).build();
        Post post = Post.builder().id(postId).member(member).build();

        given(postRepository.findById(postId)).willReturn(Optional.ofNullable(post));

        // When & Then
        assertThatThrownBy(() -> postService.getPresignedUrl(postId, fileNames))
                .isInstanceOf(MemberPermissionDeniedException.class);
    }

    @Test
    @DisplayName("이미지를 성공적으로 저장한다.")
    void saveImages_success() {
        // Given
        long postId = 1L;
        List<UrlDto> urls = List.of(new UrlDto("file1.png", "url1"), new UrlDto("file2.jpeg", "url2"));
        PostImage postImage1 = PostImage.builder().s3ImagePath("s3ImagePath1").build();
        PostImage postImage2 = PostImage.builder().s3ImagePath("s3ImagePath2").build();
        List<PostImage> savedImages = List.of(postImage1, postImage2);

        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        Post post = Post.builder().id(postId).member(member).build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(imageService.savePostImages(post, urls)).willReturn(savedImages);

        // When
        List<PostImage> result = postService.saveImages(postId, urls);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(PostImage::getS3ImagePath).containsExactly("s3ImagePath1", "s3ImagePath2");
    }

    @Test
    @DisplayName("존재하지 않는 게시물 ID로 저장 시 PostNotFoundException을 발생시킨다.")
    void saveImages_postNotFound() {
        // Given
        long postId = 1L;
        List<UrlDto> urls = List.of(new UrlDto("file1.png", "url1"), new UrlDto("file2.jpeg", "url2"));

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.saveImages(postId, urls))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("권한이 없는 사용자가 이미지를 저장하려 하면 MemberPermissionDeniedException을 발생시킨다.")
    void saveImages_permissionDenied() {
        // Given
        long postId = 1L;
        List<UrlDto> urls = List.of(new UrlDto("file1.png", "url1"), new UrlDto("file2.jpeg", "url2"));

        Member member = Member.builder().id(2L).role(MemberRole.STUDENT).build();
        Post post = Post.builder().id(postId).member(member).build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // When & Then
        assertThatThrownBy(() -> postService.saveImages(postId, urls))
                .isInstanceOf(MemberPermissionDeniedException.class);
    }
}
