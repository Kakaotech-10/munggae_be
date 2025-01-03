package com.ktb10.munggaebe.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.channel.domain.Channel;
import com.ktb10.munggaebe.channel.repository.ChannelRepository;
import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.domain.MemberImage;
import com.ktb10.munggaebe.image.domain.PostImage;
import com.ktb10.munggaebe.image.service.ImageService;
import com.ktb10.munggaebe.image.service.dto.ImageCdnPathDto;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
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

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ChannelRepository channelRepository;

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
    @DisplayName("채널 ID로 게시글 목록을 성공적으로 조회한다")
    void getPostsByChannel_ShouldReturnPosts() {
        // given
        Long channelId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        Channel testChannel = Channel.builder().name("Test Channel").build();
        ReflectionTestUtils.setField(testChannel, "id", channelId); // ID 필드 강제 설정

        Page<Post> postPage = new PageImpl<>(List.of(
                Post.builder().id(1L).channel(testChannel).createdAt(LocalDateTime.now()).build()
        ));

        given(postRepository.findByChannelId(eq(channelId), eq(pageable))).willReturn(postPage);

        // when
        Page<Post> result = postService.getPosts(channelId, 0, 10);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        verify(postRepository).findByChannelId(eq(channelId), eq(pageable));
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
        long channelId = 1L;

        Member member = Member.builder().id(memberId).build();
        Channel testChannel = Channel.builder().name("Test Channel").build();

        Post post = Post.builder().title("Title").content("Content").build();

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(channelRepository.findById(channelId)).willReturn(Optional.of(testChannel));
        given(postRepository.save(any(Post.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Post result = postService.createPost(channelId, post);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMember()).isEqualTo(member);
        assertThat(result.getChannel()).isEqualTo(testChannel);
        assertThat(result.getTitle()).isEqualTo("Title");

        verify(memberRepository).findById(memberId);
        verify(channelRepository).findById(channelId);
        verify(postRepository).save(any(Post.class));
    }


    @Test
    @DisplayName("게시물을 생성할 때 회원을 찾을 수 없으면 예외를 발생시킨다")
    void createPost_ShouldThrowException_WhenMemberNotFound() {
        // Given
        Long channelId = 1L;
        Post post = Post.builder().title("Test Post").content("Test Content").build();
        Channel testChannel = Channel.builder().name("Test Channel").build();

        given(memberRepository.findById(1L)).willReturn(Optional.empty());
        given(channelRepository.findById(channelId)).willReturn(Optional.of(testChannel));

        // When
        Throwable thrown = catchThrowable(() -> postService.createPost(channelId, post));

        // Then
        assertThat(thrown)
                .isInstanceOf(MemberNotFoundException.class) // 기대 예외를 MemberNotFoundException으로 변경
                .hasMessageContaining("id 1 에 해당하는 Member가 존재하지 않습니다.");
    }

    //추가한 테스트(채널)
    @Test
    @DisplayName("채널 ID 없이 모든 게시글을 성공적으로 가져온다")
    void getAllPosts_ShouldReturnAllPosts() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        LocalDateTime now = LocalDateTime.now(); // LocalDateTime.now() 결과를 변수에 저장

        Page<Post> postPage = new PageImpl<>(List.of(
                Post.builder().id(1L).title("Post 1").build(),
                Post.builder().id(2L).title("Post 2").build(),
                Post.builder().id(3L).title("Post 3").build()
        ));

        given(postRepository.findByCreatedAtBefore(any(), eq(pageable))).willReturn(postPage);

        // when
        Page<Post> result = postService.getPosts(null, 0, 10);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Post 1");
        assertThat(result.getContent().get(1).getTitle()).isEqualTo("Post 2");
        assertThat(result.getContent().get(2).getTitle()).isEqualTo("Post 3");

        verify(postRepository).findByCreatedAtBefore(any(), eq(pageable));
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

    @Test
    @DisplayName("해당하는 게시물의 이미지들의 정보를 반환한다.")
    void getPostImageCdnPaths_success() {
        //Given
        long postId = 1L;
        ImageCdnPathDto dto1 = ImageCdnPathDto.builder().imageId(1L).fileName("file1.jpg").path("cdnPath1").build();
        ImageCdnPathDto dto2 = ImageCdnPathDto.builder().imageId(2L).fileName("file2.jpg").path("cdnPath2").build();

        PostServiceDto.ImageCdnPathRes expected1 = PostServiceDto.ImageCdnPathRes.builder().imageId(1L).fileName("file1.jpg").path("cdnPath1").build();
        PostServiceDto.ImageCdnPathRes expected2 = PostServiceDto.ImageCdnPathRes.builder().imageId(2L).fileName("file2.jpg").path("cdnPath2").build();

        given(postRepository.existsById(postId)).willReturn(true);
        given(imageService.getPostImageUrls(postId)).willReturn(List.of(dto1, dto2));
        given(objectMapper.convertValue(dto1, PostServiceDto.ImageCdnPathRes.class)).willReturn(expected1);
        given(objectMapper.convertValue(dto2, PostServiceDto.ImageCdnPathRes.class)).willReturn(expected2);

        //When
        List<PostServiceDto.ImageCdnPathRes> result = postService.getPostImageCdnPaths(postId);

        //Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst()).usingRecursiveComparison().isEqualTo(expected1);
        assertThat(result.getLast()).usingRecursiveComparison().isEqualTo(expected2);
    }

    @Test
    @DisplayName("존재하지 않는 게시물 ID로 요청 시 PostNotFoundException을 발생시킨다.")
    void getPostImageCdnPaths_postNotFound() {
        // Given
        long postId = 1L;

        given(postRepository.existsById(postId)).willReturn(false);

        // When & Then
        assertThatThrownBy(() -> postService.getPostImageCdnPaths(postId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("이미지를 업데이트하고, 업데이트된 이미지를 반환한다.")
    void updateImage_success() {
        // Given
        long postId = 1L;
        long imageId = 1L;

        Member member = Member.builder().id(1L).build();
        Post post = Post.builder().id(postId).member(member).build();
        UrlDto imageInfo = new UrlDto("file1.jpg", "url1");
        PostImage expected = PostImage.builder()
                .id(imageId)
                .originalName(imageInfo.getFileName())
                .s3ImagePath(imageInfo.getUrl())
                .build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(imageService.updateImage(1L, imageInfo)).willReturn(expected);

        // When
        PostImage result = postService.updateImage(postId, imageId, imageInfo);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 게시물 ID로 요청 시 PostNotFoundException을 발생시킨다.")
    void updateImage_postNotFound() {
        // Given
        long postId = 1L;
        long imageId = 1L;
        UrlDto imageInfo = new UrlDto("file1.jpg", "url1");

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.updateImage(postId, imageId, imageInfo))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("권한이 없는 사용자가 이미지를 수정하려 하면 MemberPermissionDeniedException을 발생시킨다.")
    void updateImage_permissionDenied() {
        // Given
        long postId = 1L;
        long imageId = 1L;

        Member member = Member.builder().id(2L).build();
        Post post = Post.builder().id(postId).member(member).build();
        UrlDto imageInfo = new UrlDto("file1.jpg", "url1");

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // When & Then
        assertThatThrownBy(() -> postService.updateImage(postId, imageId, imageInfo))
                .isInstanceOf(MemberPermissionDeniedException.class);
    }

    @Test
    @DisplayName("imageService.updateImage의 반환값 타입이 PostImage가 아니면 IllegalStateException 발생")
    void updateImage_imageType() {
        // Given
        long postId = 1L;
        long imageId = 1L;

        Member member = Member.builder().id(1L).build();
        Post post = Post.builder().id(postId).member(member).build();
        UrlDto imageInfo = new UrlDto("file1.jpg", "url1");
        MemberImage memberImage = MemberImage.builder()
                .id(imageId)
                .originalName(imageInfo.getFileName())
                .s3ImagePath(imageInfo.getUrl())
                .build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(imageService.updateImage(1L, imageInfo)).willReturn(memberImage);

        // When & Then
        assertThatThrownBy(() -> postService.updateImage(postId, imageId, imageInfo))
                .isInstanceOf(IllegalStateException.class);
    }
}
