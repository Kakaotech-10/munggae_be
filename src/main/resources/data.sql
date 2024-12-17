CREATE TABLE IF NOT EXISTS member (
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    role VARCHAR(50) NOT NULL,
    course VARCHAR(50) NOT NULL,
    member_name VARCHAR(50) NOT NULL,
    member_name_english VARCHAR(50) NOT NULL,
    kakao_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id)
);

-- channel 생성
CREATE TABLE IF NOT EXISTS channel (
    channel_id BIGINT NOT NULL AUTO_INCREMENT,
    channel_name VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (channel_id)
);

-- member_channel 생성
CREATE TABLE IF NOT EXISTS member_channel (
    member_channel_id BIGINT NOT NULL AUTO_INCREMENT,
    channel_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    can_post BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (member_channel_id),
    FOREIGN KEY (channel_id) REFERENCES channel(channel_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS post (
    post_id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    channel_id BIGINT NOT NULL, --channel_id 추가
    post_title VARCHAR(255) NOT NULL,
    post_content TEXT NOT NULL,
    is_clean BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    reservation_time DATETIME,
    dead_line DATETIME,
    PRIMARY KEY (post_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channel(channel_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    parent_comment_id BIGINT,
    comment_content TEXT NOT NULL,
    depth INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    is_clean BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comment(comment_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS image (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    s3_image_path VARCHAR(1024) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    DTYPE VARCHAR(31) NOT NULL,
    UNIQUE (stored_name)
);

CREATE TABLE IF NOT EXISTS post_image (
    image_id BIGINT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    FOREIGN KEY (image_id) REFERENCES image(image_id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS member_image (
    image_id BIGINT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    FOREIGN KEY (image_id) REFERENCES image(image_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notification (
    notification_id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT,
    type VARCHAR(50) NOT NULL,
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (notification_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE SET NULL
);

<<<<<<< HEAD

=======
>>>>>>> 2cd88ab (fix: data.sql 충돌 수정)
-- Member 데이터 삽입
INSERT IGNORE INTO member (member_id, role, course, member_name, member_name_english, kakao_id, created_at, updated_at) VALUES
(1, 'STUDENT', 'FULLSTACK', '홍길동', 'Hong Gil-dong', 1234567891, NOW(), NOW()),
(2, 'STUDENT', 'CLOUD', '김철수', 'Kim Cheol-su', 1234567892, NOW(), NOW()),
(3, 'STUDENT', 'AI', '이영희', 'Lee Young-hee', 1234567893, NOW(), NOW()),
(4, 'MANAGER', 'FULLSTACK', '박영수', 'Park Young-su', 1234567894, NOW(), NOW()),
(5, 'STUDENT', 'CLOUD', '최지우', 'Choi Ji-woo', 1234567895, NOW(), NOW()),
(6, 'STUDENT', 'AI', '정수민', 'Jung Su-min', 1234567896, NOW(), NOW()),
(7, 'STUDENT', 'FULLSTACK', '신동엽', 'Shin Dong-yeop', 1234567897, NOW(), NOW()),
(8, 'STUDENT', 'CLOUD', '유재석', 'Yoo Jae-suk', 1234567898, NOW(), NOW()),
(9, 'STUDENT', 'AI', '하하', 'Haha', 1234567899, NOW(), NOW()),
(10, 'STUDENT', 'FULLSTACK', '김종국', 'Kim Jong-kook', 1234567900, NOW(), NOW()),
(11, 'STUDENT', 'CLOUD', '강호동', 'Kang Ho-dong', 1234567901, NOW(), NOW()),
(12, 'STUDENT', 'AI', '이수근', 'Lee Soo-geun', 1234567902, NOW(), NOW()),
(13, 'STUDENT', 'FULLSTACK', '조세호', 'Jo Se-ho', 1234567903, NOW(), NOW()),
(14, 'MANAGER', 'CLOUD', '유희열', 'Yoo Hee-yeol', 1234567904, NOW(), NOW()),
(15, 'STUDENT', 'AI', '안영미', 'Ahn Young-mi', 1234567905, NOW(), NOW()),
(16, 'STUDENT', 'FULLSTACK', '김태호', 'Kim Tae-ho', 1234567906, NOW(), NOW()),
(17, 'STUDENT', 'CLOUD', '나영석', 'Na Young-seok', 1234567907, NOW(), NOW()),
(18, 'STUDENT', 'AI', '이승기', 'Lee Seung-gi', 1234567908, NOW(), NOW()),
(19, 'STUDENT', 'FULLSTACK', '차은우', 'Cha Eun-woo', 1234567909, NOW(), NOW()),
(20, 'STUDENT', 'AI', '송혜교', 'Song Hye-kyo', 1234567910, NOW(), NOW());


       -- 채널 데이터 삽입
INSERT INTO channel (channel_name) VALUES
('공지'),
('풀스택'),
('클라우드'),
('인공지능'),
('학습게시판');

-- 채널 데이터 삽입
INSERT INTO member_channel (channel_id, member_id, can_post) VALUES
(1, 1, true),
(1, 3, true),
(2, 5, false),
(3, 6, true);

-- Post 데이터 삽입
--INSERT IGNORE INTO post (post_id, member_id, post_title, post_content, created_at, updated_at, is_clean) VALUES
--(1, 1, 'First Post', 'This is the content of the first post', NOW(), NOW(), true),
--(2, 2, 'Second Post', 'This is the content of the second post', NOW(), NOW(), true),
--(3, 3, 'Third Post', 'This is the content of the third post', NOW(), NOW(), true),
--(4, 4, 'Fourth Post', 'This is the content of the fourth post', NOW(), NOW(), true),
--(5, 5, 'Fifth Post', 'This is the content of the fifth post', NOW(), NOW(), true),
--(6, 6, 'Sixth Post', 'This is the content of the sixth post', NOW(), NOW(), true),
--(7, 7, 'Seventh Post', 'This is the content of the seventh post', NOW(), NOW(), true),
--(8, 8, 'Eighth Post', 'This is the content of the eighth post', NOW(), NOW(), true),
--(9, 9, 'Ninth Post', 'This is the content of the ninth post', NOW(), NOW(), true),
--(10, 10, 'Tenth Post', 'This is the content of the tenth post', NOW(), NOW(), true),
--(11, 11, 'Eleventh Post', 'This is the content of the eleventh post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 1 HOUR), true),
--(12, 12, 'Twelfth Post', 'This is the content of the twelfth post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 1 HOUR), true),
--(13, 13, 'Thirteenth Post', 'This is the content of the thirteenth post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), true),
--(14, 14, 'Fourteenth Post', 'This is the content of the fourteenth post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), true),
--(15, 15, 'Fifteenth Post', 'This is the content of the fifteenth post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), true),
--(16, 16, 'Sixteenth Post', 'This is the content of the sixteenth post', DATE_ADD(NOW(), INTERVAL 2 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), true),
--(17, 17, 'Seventeenth Post', 'This is the content of the seventeenth post', DATE_ADD(NOW(), INTERVAL 2 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), true),
--(18, 18, '개자식아', 'This is the content of the eighteenth post', DATE_ADD(NOW(), INTERVAL 3 HOUR), DATE_ADD(NOW(), INTERVAL 3 HOUR), false),
--(19, 19, 'Nineteenth Post', '개자식아', DATE_ADD(NOW(), INTERVAL 4 HOUR), DATE_ADD(NOW(), INTERVAL 4 HOUR), false),
--(20, 20, 'Twentieth Post', 'This is the content of the twentieth post', DATE_ADD(NOW(), INTERVAL 5 HOUR), DATE_ADD(NOW(), INTERVAL 5 HOUR), true);

-- Post 데이터 삽입 (channel_id 추가)
INSERT IGNORE INTO post (post_id, member_id, channel_id, post_title, post_content, created_at, updated_at, reservation_time, dead_line, is_clean) VALUES
(1, 1, 1, 'First Post', 'This is the content of the first post', DATE_ADD(NOW(), INTERVAL -4 DAY), DATE_ADD(NOW(), INTERVAL -4 DAY), null, DATE_ADD(NOW(), INTERVAL 6 DAY), true),
(2, 2, 1, 'Second Post', 'This is the content of the second post', DATE_ADD(NOW(), INTERVAL -4 DAY), DATE_ADD(NOW(), INTERVAL -4 DAY), null, DATE_ADD(NOW(), INTERVAL 5 DAY), true),
(3, 3, 1, 'Third Post', 'This is the content of the third post', DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), true),
(4, 4, 1, 'Fourth Post', 'This is the content of the fourth post', DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), true),
(5, 5, 2, 'Fifth Post', 'This is the content of the fifth post', NOW(), NOW(), null, null, true),
(6, 6, 2, 'Sixth Post', 'This is the content of the sixth post', NOW(), NOW(), null, null, true),
(7, 7, 2, 'Seventh Post', 'This is the content of the seventh post', NOW(), NOW(), null, null, true),
(8, 8, 2, 'Eighth Post', 'This is the content of the eighth post', NOW(), NOW(), null, null, true),
(9, 9, 2, 'Ninth Post', 'This is the content of the ninth post', NOW(), NOW(), null, null, true),
(10, 10, 3, 'Tenth Post', 'This is the content of the tenth post', NOW(), NOW(), null, null, true),
(11, 11, 3, 'Eleventh Post', 'This is the content of the eleventh post', NOW(), NOW(), null, null, true),
(12, 12, 3, 'Twelfth Post', 'This is the content of the twelfth post', NOW(), NOW(), null, null, true),
(13, 13, 3, 'Thirteenth Post', 'This is the content of the thirteenth post', NOW(), NOW(), null, null, true),
(14, 14, 3, 'Fourteenth Post', 'This is the content of the fourteenth post', NOW(), NOW(), null, null, true),
(15, 15, 3, 'Fifteenth Post', 'This is the content of the fifteenth post', NOW(), NOW(), null, null, true),
(16, 16, 4, 'Sixteenth Post', 'This is the content of the sixteenth post', NOW(), NOW(), null, null, true),
(17, 17, 4, 'Seventeenth Post', 'This is the content of the seventeenth post', NOW(), NOW(), null, null, true),
(18, 18, 4, '개자식아', 'This is the content of the eighteenth post', NOW(), NOW(), null, null, false),
(19, 19, 4, 'Nineteenth Post', '개자식아', NOW(), NOW(), null, null, false),
(20, 20, 4, 'Twentieth Post', 'This is the content of the twentieth post', NOW(), NOW(), null, null, true);

-- Comment 데이터 삽입
INSERT IGNORE INTO comment (comment_id, post_id, member_id, parent_comment_id, comment_content, depth, created_at, updated_at, is_clean) VALUES
(1, 1, 2, NULL, 'This is the first comment', 0, NOW(), NOW(), true),
(2, 19, 3, 20, 'This is the second comment', 1, NOW(), NOW(), true),
(3, 19, 4, 20, '개자식아', 1, NOW(), NOW(), false),
(4, 19, 5, 20, 'This is the fourth comment', 1, NOW(), NOW(), true),
(5, 19, 6, NULL, '개자식아', 0, NOW(), NOW(), false),
(6, 5, 7, NULL, 'This is the sixth comment', 0, NOW(), NOW(), true),
(7, 6, 8, NULL, 'This is the seventh comment', 0, NOW(), NOW(), true),
(8, 7, 9, NULL, 'This is the eighth comment', 0, NOW(), NOW(), true),
(9, 8, 10, NULL, 'This is the ninth comment', 0, NOW(), NOW(), true),
(10, 9, 11, NULL, 'This is the tenth comment', 0, NOW(), NOW(), true),
(11, 10, 12, NULL, 'This is the eleventh comment', 0, NOW(), NOW(), true),
(12, 11, 13, NULL, 'This is the twelfth comment', 0, NOW(), NOW(), true),
(13, 12, 14, NULL, 'This is the thirteenth comment', 0, NOW(), NOW(), true),
(14, 13, 15, NULL, 'This is the fourteenth comment', 0, NOW(), NOW(), true),
(15, 14, 16, NULL, 'This is the fifteenth comment', 0, NOW(), NOW(), true),
(16, 15, 17, NULL, 'This is the sixteenth comment', 0, NOW(), NOW(), true),
(17, 16, 18, NULL, 'This is the seventeenth comment', 0, NOW(), NOW(), true),
(18, 17, 19, NULL, 'This is the eighteenth comment', 0, NOW(), NOW(), true),
(19, 18, 20, NULL, 'This is the nineteenth comment', 0, NOW(), NOW(), true),
(20, 19, 1, NULL, 'This is the twentieth comment', 0, NOW(), NOW(), true);

INSERT IGNORE INTO image (original_name, stored_name, s3_image_path, DTYPE) VALUES
('sample1.jpg', 'stored1.jpg', '/post/sample1.jpg', 'post'),
('sample2.jpg', 'stored2.jpg', '/post/sample2.jpg', 'post'),
('sample3.jpg', 'stored3.jpg', '/post/sample3.jpg', 'post'),
('sample4.jpg', 'stored4.jpg', '/member/sample4.jpg', 'member'),
('sample5.jpg', 'stored5.jpg', '/member/sample5.jpg', 'member');

INSERT IGNORE INTO post_image (image_id, post_id) VALUES
(1, 1),
(2, 2),
(3, 3);

INSERT IGNORE INTO member_image (image_id, member_id) VALUES
(4, 1),
(5, 2);

INSERT IGNORE INTO notification (notification_id, member_id, type, message, is_read, created_at, updated_at) VALUES
(1, 1, 'MENTION', 'You have a new comment on your post.', false, NOW(), NOW()),
(2, 2, 'MENTION', 'Your post has been liked by Kim Cheol-su.', false, DATE_ADD(NOW(), INTERVAL -1 DAY), DATE_ADD(NOW(), INTERVAL -1 DAY)),
(3, 6, 'MENTION', 'Your post has been liked by Yoo Jae-suk.', false, DATE_ADD(NOW(), INTERVAL -4 DAY), DATE_ADD(NOW(), INTERVAL -4 DAY)),
(4, 1, 'ANNOUNCEMENT', 'ANNOUNCEMENT1', false, DATE_ADD(NOW(), INTERVAL -2 DAY), DATE_ADD(NOW(), INTERVAL -2 DAY)),
(5, 2, 'ANNOUNCEMENT', 'ANNOUNCEMENT1', false, DATE_ADD(NOW(), INTERVAL -2 DAY), DATE_ADD(NOW(), INTERVAL -2 DAY)),
(6, 3, 'ANNOUNCEMENT', 'ANNOUNCEMENT1', false, DATE_ADD(NOW(), INTERVAL -2 DAY), DATE_ADD(NOW(), INTERVAL -2 DAY)),
(7, 4, 'ANNOUNCEMENT', 'ANNOUNCEMENT1', false, DATE_ADD(NOW(), INTERVAL -2 DAY), DATE_ADD(NOW(), INTERVAL -2 DAY)),
(8, 5, 'ANNOUNCEMENT', 'ANNOUNCEMENT1', false, DATE_ADD(NOW(), INTERVAL -2 DAY), DATE_ADD(NOW(), INTERVAL -2 DAY)),
(9, 6, 'ANNOUNCEMENT', 'ANNOUNCEMENT1', false, DATE_ADD(NOW(), INTERVAL -2 DAY), DATE_ADD(NOW(), INTERVAL -2 DAY)),
(10, 7, 'ANNOUNCEMENT', 'ANNOUNCEMENT1', false, DATE_ADD(NOW(), INTERVAL -2 DAY), DATE_ADD(NOW(), INTERVAL -2 DAY)),
(11, 8, 'ANNOUNCEMENT', 'ANNOUNCEMENT1', false, DATE_ADD(NOW(), INTERVAL -2 DAY), DATE_ADD(NOW(), INTERVAL -2 DAY));
