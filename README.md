# ☁️ 뭉게뭉게 백엔드

부트캠프 내 **커뮤니티 게시판** 프로젝트

![groom](https://github.com/user-attachments/assets/65647302-03f1-4676-9bc6-976f6d6fa0a7)

📅 개발 기간 : 2024.09.23 ~ 2024.12.20 (3달)

<br>

## 👫 팀 구성 및 역할

| **김요한** | **오지영** |
|:------: | :------: |
| <img src="http://raw.githubusercontent.com/Kakaotech-10/.github/main/profile/Yohan.png" height=150 width=150> |  <img src="http://raw.githubusercontent.com/Kakaotech-10/.github/main/profile/Ella.png" height=150 width=150> |  <img src="http://raw.githubusercontent.com/Kakaotech-10/.github/main/profile/Lucy.png" height=150 width=150> |

- 🧩 김요한
  - Git : https://github.com/yohanii
  - 담당
    - 게시판 기능
    - 인증/인가
    - 알림 기능
    - 멘션 기능
    - 키워드 랭킹 기능
    - 텍스트 필터링 연결
    - AI 댓글 기능
- 🍭 오지영
  - Git : https://github.com/rimeir
  - 담당
    - 채널 기능


<br>


## 📚 Stack
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
<img src="https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=Elasticsearch&logoColor=white">
<img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">


<br>


## 🔧 주요 기능
- 사용자 인증 및 권한
  - JWT 토큰 기반 카카오 OAuth2 로그인
  - Spring Security 사용한 인증, 인가
  - Access Token은 header에, Refresh Token은 https, Secure 된 쿠키에 전달받기로 프론트엔드와 협의
  - Redis에 Refresh Token 저장
  - 유저 권한 : STUDENT, MANAGER
- 커뮤니티 게시판 with 채널
  - 채널, 게시물, 댓글, 대댓글 CRUD 기능
  - 예약 게시글 가능
  - 공지사항 마감 시간 지정 가능
  - 게시물, 사용자 이미지
    - S3, CloudFront 사용
    - Presigned URL 방식
- 알림 서비스
  - SSE로 알림 전송
  - Redis pub/sub로 분산 환경에서 동작할 수 있도록, 알림 이벤트를 모든 스프링 애플리케이션에 전달
- 키워드 랭킹 기능
  - AI팀에서 ElasticSearch에 적재한 데이터를 활용해, 핫한 키워드 랭킹 Top 3 전달
- 멘션 기능
  - ElasticSearch & 한글 플러그인 사용해 초성, 중성, 종성 단위의 이름 검색 기능
  - 'ㄱ'만 쳐도, '강하늘', '김요한', '김민우' 검색 됨.
- 텍스트 필터링
  - AI 팀 api 활용
- 학습게시판 AI 댓글 기능
  - AI 팀 api 활용
  - 비동기 처리


<br>


## 🥳 개인 회고
- 김요한
  - 👍 새로운 기술 도입에 대한 두려움이 사라짐. 기술 선택 시, 장단점 비교 후 합리적인 선택 경험을 할 수 있었다.
  - 👍 서버 분산 처리 환경에서 발생한 문제 해결 경험
  - 👎 많은 기능 구현보다 하나의 기능을 기술적으로 딥하게 파는 경험이 더 좋았을 것 같다.
- 오지영
  - 👍 백엔드와 프론트엔드의 협업 방법 익힘
  - 👍 API 명세화로 협업 효율성 증가
  - 👎 권한 관리와 ERD 설계 경험 부족으로 인해 데이터 구조 최적화와 사용자 권한 관리가 미흡했다. 추후, 구체적인 권한 설계로 보완 예정.
