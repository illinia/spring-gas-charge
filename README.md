

### 0.0 기본 기능 구현
___
#### 0.0.7 버그 픽스 (12 Oct 2022)
1. 컨트롤러에서 @AuthenticationPrincipal 에서 객체 못 받아오는 버그 수정
   1. 스웨거 설정 위해 SwaggerConfig 에서 WebMvcConfigurationSupport 를 상속 받아 설정 했는데
   2. 스프링 부트에서 기본적으로 설정한 것들이 적용이 안 되었음.
   3. 해결 방법
      1. WebMvcConfigurationSupport 상속을 안 받는다.
      2. WebMvcConfigurationSupport 상속이 필요한 경우 
      addArgumentResolvers(List<handlerMethodArgumentResolver> argumentResolvers)
      메서드를 오버라이딩해서 AuthenticationPrincipalArgumentResolver 클래스를 추가해준다.
___
#### 0.0.6 내역 (11 Oct 2022)
1. 충전소 정보 properties 만들고 pojo 로 사용하게 수정
2. 컨트롤러 전역 에러처리 추가
3. 충전소 id 값 대리키로 변경
4. 충전소 create 기능 추가
   1. POST "/charge"
   2. PostChargeRequestDto 로 값 받기
   3. 인증된 사용자만 호출할수 있게 메서드사전호출 추가
5. 스웨거 기능 추가
   1. oauth2 jwt 필요한 경우 헤더에 추가할 수 있게 기능 추가
   2. 스프링 데이터 Pageable 필드값들 스웨거에서 수정할 수 있게 추가
#### 0.0.7 업데이트 예정
1. 예약 기능은 나중에
2. 충전소 crud 마무리
___
#### 0.0.5 내역 (09 Oct 2022)
1. 배치 스케줄링해서 충전소 데이터 받아오는 기능 완성
2. 충전소 검색 기능 추가
   1. 전체 검색 Page<Charge>
   2. 이름 검색
      1. GET “/charge” name={name}
   3. 가맹, 비가맹 구분 파라미터
      1. GET “/charge”
      2. is-membership=membership or not-membership
   4. 정렬
      1. GET “/charge”
      2. sort=value,desc
   5. 페이징
      1. GET “/charge”
      2. size=1 2 3 4 page=10 20 30
   6. 충전소 id 값으로 검색 Charge
      1. GET “/charge/{id}”
#### 0.0.6 업데이트 예정
1. 검색 결과 dto 로 변환해서 응답
2. 예약 기능 추가
3. 충전소 정보 properties 만들고 pojo 로 사용하게 수정
___
#### 0.0.4 내역 (07 Oct 2022)
1. 스프링 시큐리티에 대한 테스트는 잠시 뒤로 미루기로 함(Jwt 와 Oauth2 로그인을 같이 테스트하기에는 공부가 부족하다 느낌)
2. 스프링 배치 세팅
3. 충전소 엔티티 제작
#### 0.0.5 업데이트 예정
1. 충전소 데이터 쿼츠로 스케줄링 하고 스프링 배치 이용해서 저장하기
___
#### 0.0.3 내역 (20 Sep 2022)
1. 참고 https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-1/

#### 0.0.4 업데이트 예정
1. 로그인 정보 저장하기 위한 JWT 어떻게 구현할지
2. 로그인 관련 테스트는 어떻게 짤지
___
#### 0.0.2 내역 (15 Sep 2022)
1. 변경내역
   1. 회원가입, 로그인 방식을 **소셜 로그인**으로 바꾸기로 변경
   2. 로그인 정보 저장 방식을 세션에서 **JWT**로 바꾸기로 변경
2. Member 에 소셜 로그인 기능 추가하기 위해 필드값 추가
   1. password
   2. email
   3. picture
   4. role
3. 스프링 시큐리티 로그아웃, 소셜 로그인 기능 추가
   1. 구글
   2. 네이버
   3. 소셜 로그인시 Member 객체 만들어서 DB에 저장
   4. 로그아웃 추가

#### 0.0.3 업데이트 예정
1. 로그인 정보 저장하기 위한 JWT 어떻게 구현할지
2. 로그인 관련 테스트는 어떻게 짤지
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
1. ~~Member 서비스 유효성 로직 추가 필요~~
2. ~~Member 컨트롤러에서 요청값이 잘못 들어왔을때 어떻게, 어디서 에러처리 하는지 고민 필요~~
3. ~~서비스, 컨트롤러 테스트코드 작성 고민 필요~~
___
