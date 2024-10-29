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
