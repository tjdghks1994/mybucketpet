# 마이버킷 펫
🐶 프로젝트 소개

뱐려동물을 키우는 사람들이 반려동물과 함께하는 버킷리스트를 달성하고 정보를 공유하며 소통하는 웹 사이트
***
👯 프로젝트 참여 멤버

양현아 : 기획, 디자인

박성환 : 설계, 개발
***
⚙️ 개발 환경
> IDE : IntelliJ

> Front : thymeleaf, css, js

> Back : spring boot, mybatis, Amazon RDS(MySQL)


***
🗂️ 프로젝트 구조 ( 개발 진행하면서 변동 생길 수 있음 )

java/com/mybucketpet
  
  - config : Service, Repository와 관련된 빈들과 그외의 스프링 빈으로 별도로 등록하기 위한 설정 클래스 모음
    
  - controller : 컨트롤러 클래스 모음
    
  - domain : entity 클래스 모음
    
  - interceptor : spring interceptor를 구현할 클래스 모음
    
  - repository : DB와 연결되는 클래스 모음
    
  - service : 비즈니스 로직을 수행할 클래스 모음
  
resources
  - com/mybucketpet/repository : Mapper인터페이스와 매핑되는 xml 파일
  
  - static : 정적 리소스 (css, js, img)
  
  - template : 동적 뷰 템플릿 (thymeleaf)
  
  - application.properties : 애플리케이션에서 사용할 속성 정보
  
  - messages.properties : MessageSource 기본 설정 파일
  
  - errors.properties : MessageSource를 통한 오류 메시지를 관리하기 위한 파일

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
    + 버킷 관리 (개발 진행 중)
    + 카테고리 관리
  - 인증관리
  - 일상관리
  - 기본설정


***
📌 [프로젝트 개발 일지 작성 진행 (Notion)](https://polite-handball-c55.notion.site/37b7cd789a0f4a6a8427fd2bd7144fab?pvs=4)
