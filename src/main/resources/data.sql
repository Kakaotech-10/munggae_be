CREATE TABLE IF NOT EXISTS member (
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    role VARCHAR(50) NOT NULL,
    course VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    member_name VARCHAR(50) NOT NULL,
    member_name_english VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id)
);

CREATE TABLE IF NOT EXISTS post (
    post_id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    post_title VARCHAR(255) NOT NULL,
    post_content TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    parent_comment_id BIGINT,
    comment_content TEXT NOT NULL,
    depth INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comment(comment_id) ON DELETE CASCADE
);

-- Member 데이터 삽입
INSERT IGNORE INTO member (member_id, role, course, email, member_name, member_name_english, created_at, updated_at) VALUES
(1, 'STUDENT', 'Course 1', 'STUDENT1@example.com', '홍길동', 'Hong Gil-dong', NOW(), NOW()),
(2, 'STUDENT', 'Course 2', 'STUDENT2@example.com', '김철수', 'Kim Cheol-su', NOW(), NOW()),
(3, 'STUDENT', 'Course 3', 'STUDENT3@example.com', '이영희', 'Lee Young-hee', NOW(), NOW()),
(4, 'MANAGER', 'Course 1', 'MANAGER1@example.com', '박영수', 'Park Young-su', NOW(), NOW()),
(5, 'STUDENT', 'Course 2', 'STUDENT5@example.com', '최지우', 'Choi Ji-woo', NOW(), NOW()),
(6, 'STUDENT', 'Course 1', 'STUDENT6@example.com', '정수민', 'Jung Su-min', NOW(), NOW()),
(7, 'STUDENT', 'Course 3', 'STUDENT7@example.com', '신동엽', 'Shin Dong-yeop', NOW(), NOW()),
(8, 'STUDENT', 'Course 1', 'STUDENT8@example.com', '유재석', 'Yoo Jae-suk', NOW(), NOW()),
(9, 'STUDENT', 'Course 2', 'STUDENT9@example.com', '하하', 'Haha', NOW(), NOW()),
(10, 'STUDENT', 'Course 3', 'STUDENT10@example.com', '김종국', 'Kim Jong-kook', NOW(), NOW()),
(11, 'STUDENT', 'Course 1', 'STUDENT11@example.com', '강호동', 'Kang Ho-dong', NOW(), NOW()),
(12, 'STUDENT', 'Course 2', 'STUDENT12@example.com', '이수근', 'Lee Soo-geun', NOW(), NOW()),
(13, 'STUDENT', 'Course 3', 'STUDENT13@example.com', '조세호', 'Jo Se-ho', NOW(), NOW()),
(14, 'MANAGER', 'Course 1', 'MANAGER2@example.com', '유희열', 'Yoo Hee-yeol', NOW(), NOW()),
(15, 'STUDENT', 'Course 2', 'STUDENT15@example.com', '안영미', 'Ahn Young-mi', NOW(), NOW()),
(16, 'STUDENT', 'Course 1', 'STUDENT16@example.com', '김태호', 'Kim Tae-ho', NOW(), NOW()),
(17, 'STUDENT', 'Course 3', 'STUDENT17@example.com', '나영석', 'Na Young-seok', NOW(), NOW()),
(18, 'STUDENT', 'Course 1', 'STUDENT18@example.com', '이승기', 'Lee Seung-gi', NOW(), NOW()),
(19, 'STUDENT', 'Course 2', 'STUDENT19@example.com', '차은우', 'Cha Eun-woo', NOW(), NOW()),
(20, 'STUDENT', 'Course 3', 'STUDENT20@example.com', '송혜교', 'Song Hye-kyo', NOW(), NOW());

-- Post 데이터 삽입
INSERT IGNORE INTO post (post_id, member_id, post_title, post_content, created_at, updated_at) VALUES
(1, 1, 'First Post', 'This is the content of the first post', NOW(), NOW()),
(2, 2, 'Second Post', 'This is the content of the second post', NOW(), NOW()),
(3, 3, 'Third Post', 'This is the content of the third post', NOW(), NOW()),
(4, 4, 'Fourth Post', 'This is the content of the fourth post', NOW(), NOW()),
(5, 5, 'Fifth Post', 'This is the content of the fifth post', NOW(), NOW()),
(6, 6, 'Sixth Post', 'This is the content of the sixth post', NOW(), NOW()),
(7, 7, 'Seventh Post', 'This is the content of the seventh post', NOW(), NOW()),
(8, 8, 'Eighth Post', 'This is the content of the eighth post', NOW(), NOW()),
(9, 9, 'Ninth Post', 'This is the content of the ninth post', NOW(), NOW()),
(10, 10, 'Tenth Post', 'This is the content of the tenth post', NOW(), NOW()),
(11, 11, 'Eleventh Post', 'This is the content of the eleventh post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 1 HOUR)),
(12, 12, 'Twelfth Post', 'This is the content of the twelfth post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 1 HOUR)),
(13, 13, 'Thirteenth Post', 'This is the content of the thirteenth post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR)),
(14, 14, 'Fourteenth Post', 'This is the content of the fourteenth post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR)),
(15, 15, 'Fifteenth Post', 'This is the content of the fifteenth post', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR)),
(16, 16, 'Sixteenth Post', 'This is the content of the sixteenth post', DATE_ADD(NOW(), INTERVAL 2 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR)),
(17, 17, 'Seventeenth Post', 'This is the content of the seventeenth post', DATE_ADD(NOW(), INTERVAL 2 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR)),
(18, 18, 'Eighteenth Post', 'This is the content of the eighteenth post', DATE_ADD(NOW(), INTERVAL 3 HOUR), DATE_ADD(NOW(), INTERVAL 3 HOUR)),
(19, 19, 'Nineteenth Post', 'This is the content of the nineteenth post', DATE_ADD(NOW(), INTERVAL 4 HOUR), DATE_ADD(NOW(), INTERVAL 4 HOUR)),
(20, 20, 'Twentieth Post', 'This is the content of the twentieth post', DATE_ADD(NOW(), INTERVAL 5 HOUR), DATE_ADD(NOW(), INTERVAL 5 HOUR));

-- Comment 데이터 삽입
INSERT IGNORE INTO comment (comment_id, post_id, member_id, parent_comment_id, comment_content, depth, created_at, updated_at) VALUES
(1, 1, 2, NULL, 'This is the first comment', 0, NOW(), NOW()),
(2, 1, 3, NULL, 'This is the second comment', 0, NOW(), NOW()),
(3, 2, 4, NULL, 'This is the third comment', 0, NOW(), NOW()),
(4, 3, 5, NULL, 'This is the fourth comment', 0, NOW(), NOW()),
(5, 4, 6, NULL, 'This is the fifth comment', 0, NOW(), NOW()),
(6, 5, 7, NULL, 'This is the sixth comment', 0, NOW(), NOW()),
(7, 6, 8, NULL, 'This is the seventh comment', 0, NOW(), NOW()),
(8, 7, 9, NULL, 'This is the eighth comment', 0, NOW(), NOW()),
(9, 8, 10, NULL, 'This is the ninth comment', 0, NOW(), NOW()),
(10, 9, 11, NULL, 'This is the tenth comment', 0, NOW(), NOW()),
(11, 10, 12, NULL, 'This is the eleventh comment', 0, NOW(), NOW()),
(12, 11, 13, NULL, 'This is the twelfth comment', 0, NOW(), NOW()),
(13, 12, 14, NULL, 'This is the thirteenth comment', 0, NOW(), NOW()),
(14, 13, 15, NULL, 'This is the fourteenth comment', 0, NOW(), NOW()),
(15, 14, 16, NULL, 'This is the fifteenth comment', 0, NOW(), NOW()),
(16, 15, 17, NULL, 'This is the sixteenth comment', 0, NOW(), NOW()),
(17, 16, 18, NULL, 'This is the seventeenth comment', 0, NOW(), NOW()),
(18, 17, 19, NULL, 'This is the eighteenth comment', 0, NOW(), NOW()),
(19, 18, 20, NULL, 'This is the nineteenth comment', 0, NOW(), NOW()),
(20, 19, 1, NULL, 'This is the twentieth comment', 0, NOW(), NOW());