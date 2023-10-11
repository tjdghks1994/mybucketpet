# 마이버킷 펫 

🐶 프로젝트 소개

뱐려동물을 키우는 사람들이 반려동물과 함께하는 버킷리스트를 달성하고 정보를 공유하며 소통하는 웹 사이트

***

👯 프로젝트 참여 멤버

양현아 : 기획, 디자인

박성환 : 설계, 개발

***

⚙️ 개발 환경

> Front : thymeleaf, css, js

> Back : spring boot, mybatis, Amazon RDS (MySQL8.0)

***

🗂️ 프로젝트 구조 ( 개발 진행하면서 변동 생길 수 있음 )

~~~
main
  ├─ java
  │  └─ com
  │     └─ mybucketpet
  │        ├─ config
  │        ├─ controller
  │        ├─ domain
  │        ├─ exception
  │        ├─ interceptor
  │        ├─ repository
  │        ├─ service
  │        └─ util
  │
  └─ resources
     ├─ application.properties
     ├─ com
     │  └─ mybucketpet
     │     └─ repository
     ├─ errors.properties
     ├─ messages.properties
     ├─ static
     │  ├─ css
     │  ├─ img
     │  ├─ js
     │  └─ summernote
     └─ templates

~~~

***
🗄 테이블 구조 (ERD)

<img width="1202" alt="스크린샷 2023-10-11 오후 11 26 36" src="https://github.com/tjdghks1994/wanted-pre-onboarding-backend/assets/57320084/b8402380-8c98-4f99-89cb-3cf0b948a4c5">

***
📝 주요 기능 ( 개발 진행 중 )

* 로그인화면
  - 회원가입
    + 이메일 회원가입 (1차 완료)
    + SNS 회원가입
  - 비밀번호 재설정 (1차 완료)
    + 이메일 인증 (완료)
  - 로그인 (완료)
* 메인화면
  - 검색 기능
  - 버킷리스트 게시판
  - 인증 게시판
  - 일상 게시판
* 마이페이지
  - 프로필
  - 회원정보
  - 마이 버킷리스트
  - 스크랩
  - 인증 후기
* 고객센터
  - 자주묻는질문
  - 1대1 문의
  - 공지사항
* 관리자페이지
  - 대시보드
  - 회원관리
  - 버킷관리
    + 메인배너 관리
    + 버킷 관리 (1차 완료)
    + 카테고리 관리
  - 인증관리
  - 일상관리
  - 기본설정

***
📌 [프로젝트 개발 일지 작성 (Notion)](https://polite-handball-c55.notion.site/37b7cd789a0f4a6a8427fd2bd7144fab?pvs=4)
***