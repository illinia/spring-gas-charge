

### 0.0 설정 및 엔티티 정의


___
#### 0.0.1 내역 (13 Sep 2022)
1. h2 DB 연결
2. Member 엔티티 정의
3. Member 엔티티 Spring Data JPA 연동, 리포지토리 테스트
4. DB, 테스트 환경 구축
   * 개발시 로컬 h2 DB 연동
   * Spring Security 에서 웹 콘솔 사용가능하게 설정
   * 테스트시 메모리 DB 사용하게 설정
5. Member Service, Controller 추가
   * 회원가입 서비스 추가
   * 회원가입 컨트롤러 회원가입 Post 방식 추가, RequestBody 에 Dto 설정
   
#### 0.0.2 업데이트 예정
1. Member 서비스 유효성 로직 추가 필요
2. Member 컨트롤러에서 요청값이 잘못 들어왔을때 어떻게, 어디서 에러처리 하는지 고민 필요
3. 서비스, 컨트롤러 테스트코드 작성 고민 필요
___
