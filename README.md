

### 0.0 기본 기능 구현
___
#### 0.1.0 내역 (16 Oct 2022)
1. AWS EC2 배포
2. 맥에서 ec2 콘솔 접속시 간편 설정
   1. cp pem 파일 위치 ~/.ssh/
   2. cd .ssh/
   3. chmod 600 pem 파일 위치
   4. vim ~/.ssh/config
   5. Host 에 별칭 저장, HostName 에 탄력적 주소 입력 IdentityFile 에 pem 파일 주소 입력
   6. chmod 700 ~/.ssh/config 
   7. ssh 별
3. 리눅스 자바 버젼 체크, 설치
   1. java -version
   2. Java11
   3. sudo yum install -y java-11-amazon-corretto.x86_64
   4. sudo /usr/sbin/alternatives --config java
   5. sudo yum remove java-1.7.0-openjdk
   6. java -version
4. 타임존 변경
   1. sudo rm /etc/localtime
   2. sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
   3. date
5. 시스템 호스트 이름 변경
   1. sudo vi /etc/cloud/cloud.cfg
   2. preserve_hostname 설정이 없으면 파일 끝에 텍스트 추가 preserve_hostname: true
   3. sudo hostnamectl set-hostname 필요한 도메인 이름
6. sudo reboot
7. ec2 에서 rds 접근 확인
   1. sudo yum install mysql
   2. mysql -u 유저이름 -p -h ec2주소
8. ec2 에 깃 설치, 프로젝트 빌드
   1. sudo yum install git
   2. git --version
   3. mkdir ~/app && mkdir ~/app/step1
   4. cd ~/app/step1
   5. git clone 주소
   6. ./gradlew clean
   7. ./gradlew compileQuerydsl (자바 버젼이 안맞으면 에러남. 버젼 11)
   8. ./gradlew build
9. 배포 스크립트 제작
   1. vim ~/app/step1/deploy.sh
   2. 내용은 프로젝트 배포_스크립트.sh
   3. chmod +x ./deploy.sh
   4. ./deploy.sh
   5. vim nohup.out 에러 체크
   6. vim /home/ec2-user/app/application-oauth.properties
   7. vim /home/ec2-user/app/application-charge.properties
___
#### 0.0.10 내역 (15 Oct 2022)
1. 예약 상태 변경 어드민용 기능 추가
   1. PATCH “/reservationValidationId” 권한 ADMIN
      1. PathVariable String reservationValidationId
      2. ForceUpdateReservationRequestDto
         1. ReservationStatus status
         2. LocalDateTime time
      3. ForceUpdateReservationResponseDto
         1. String reservationId
         2. String userEmail
         3. String chargePlaceId
         4. LocalDatetime reserveTime
         5. ReservationStatus status
2. 유저 전체 검색
   1. GET “” 권한 ADMIN
      1. RequestParam
         1. String email
         2. String email-verified, verified, unverified
         3. String name
         4. String provider google, kakao, naver
         5. String user-authority user, admin
      2. ResponseBody SearchUserListResponseDto
         1. Page<SearchUserResponseDto>
            1. String imageUrl
            2. String name
            3. String email
            4. UserAuthority authority;
            5. GetReservationResponseDto reservations
3. 유저 전체 검색시 N + 1 문제 해결
   1. 유저 전체 검색시 유저 안의 예약, 예약 안의 충전소 정보가 n + 1 * n + 1 로 검색됨
   2. querydsl 의 leftJoin 메서드와 fetchJoin 메서드로 하나의 쿼리로 불러올 수 있게 수정
#### 0.1.0 업데이트 예정
1. 테스트 코드 작성
2. AWS 배포
___
#### 0.0.9 내역 (14 Oct 2022)
1. 예약 조회
   1. GET 단건조회 “/{reservationValidationId}” 권한 USER
      1. 예약 식별 아이디
      2. 해당 유저 이메일(사전 메서드 권한 검증)
      3. Request
         1. PathVariable String reservationValidationId
   2. GET 전체조회, 다건조회 “/reservation”
      1. 전체 조회일 시 권한 인증 유저, ADMIN
         1. email 선택, email 넣든 안 넣든 검색 됨 -> 모든 유저의 예약을 볼 수 있다.
      2. 다건 조회일 시 권한 인증 유저, email == 인증 객체
         1. 다건 조회는 email 이 필수
         2. 자동으로 검색시 email 비교해서 같은 예약만 응답 -> 다른 유저의 예약은 볼 수 없다.
      3. Request
         1. 해당 유저 이메일 RequestParam String email
         2. 충전소 아이디 RequestParam String chargePlaceId
         3. 예약 상태 RequestParam ReservationStatus status
         4. Pageable
      4. Response Page<GetReservationResponseDto>
      5. GetReservationResponseDto
         1. String reservationId
         2. String userEmail
         3. String chargePlaceId
         4. LocalDatetime reserveTime
         5. ReservationStatus status
2. 예약 취소
   1. POST 예약 상태를 취소로 변경 “/cancel” 권한 USER, ADMIN
      1. 파라미터 String email, String reservationValidationId
      2. USER 인 경우 예약 이메일과 유저 이메일 일치 확인
      3. ADMIN 은 확인 필요 없음
      4. Request
         1. String email
         2. String reservationValidationId
      5. Response CancelReservationResponseDto
         1. String reservationId
         2. String userEmail
         3. String chargePlaceId
         4. LocalDatetime reserveTime
         5. ReservationStatus status
#### 0.0.10 업데이트 예정
1. 충전소, 유저 상세 검색 기능 추가 후 버전 업
2. 충전소, 유저 검색시 N + 1 확인 및 쿼리 수정
___
#### 0.0.8 내역 (13 Oct 2022)
1. 예약 엔티티 제작, 유저, 충전소 엔티티와 매핑
2. 예약 등록, 수정 기능 추가
   1. 예약 등록
      1. POST “/reservation” 권한 USER
         1. ReserveRequestDto
         2. ReserveResponseDto
   2. 예약 수정
      1. PATCH “/reservation” 권한 USER
         1. UpdateReservationRequestDto
         2. UpdateReservationResponseDto
3. 사전권한부여 메서드에 SpEL 사용하여 인증되고 이메일 일치하는 유저 본인만 예약 수정하게 수정
4. 스웨거 설명, 기본값 설정
5. 스프링 부트 validation 기능 RequestDto 에 추가
#### 0.0.9 업데이트 예정
1. 예약 crud 계속 추가
___
#### 0.0.7 내역 (12 Oct 2022)
1. 스웨거 테스트용 계정 생성, 부트 실행시 jwt 토큰값 콘솔창에서 출력
2. 권한 없을때 던져지는 AccessDeniedException 도 전역 예외 핸들러에서 처리
   1. Exception 핸들러에서 잡아서 500 에러로 던져지는 이슈 있었음
   2. 현재는 403 재대로 던져짐
3. 충전소 수정 기능 추가
   1. PATCH “/charge” 권한 ADMIN
   2. PatchChargeRequestDto requestBody
   3. SearchChargeResponseDto responseBody
4. 충전소 삭제 기능 추가
   1. DELETE “/charge/chargePlaceId” 권한 ADMIN
   2. String chargePlaceId request
   3. String 삭제 완료 문구 반환 response
5. 예외 처리 기준 변경
   1. 서비스에서 예외 던지게 변경
   2. 예를 들면 검색시 결과가 없을때 NoResultException 던지게 변경
#### 0.0.8 업데이트 예정
1. 예약 엔티티 제작, 매핑 설계
2. 예약 crud
3. 일정 시간마다 예약 시간 지나서 충전 완료가 안 되었을 시 예약 노쇼상태로 변경하는 배치 기능 추가
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
   1. POST "/charge" 권한 ADMIN
   2. PostChargeRequestDto requestBody
   3. 인증된 사용자만 호출할수 있게 메서드사전호출 추가
5. 스웨거 기능 추가
   1. oauth2 jwt 필요한 경우 헤더에 추가할 수 있게 기능 추가
   2. 스프링 데이터 Pageable 필드값들 스웨거에서 수정할 수 있게 추가
#### 0.0.7 업데이트 예정
1. 예약 기능은 나중에
2. ~~충전소 crud 마무리~~
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
1. ~~검색 결과 dto 로 변환해서 응답~~
2. 예약 기능 추가
3. ~~충전소 정보 properties 만들고 pojo 로 사용하게 수정~~
___
#### 0.0.4 내역 (07 Oct 2022)
1. 스프링 시큐리티에 대한 테스트는 잠시 뒤로 미루기로 함(Jwt 와 Oauth2 로그인을 같이 테스트하기에는 공부가 부족하다 느낌)
2. 스프링 배치 세팅
3. 충전소 엔티티 제작
#### 0.0.5 업데이트 예정
1. ~~충전소 데이터 쿼츠로 스케줄링 하고 스프링 배치 이용해서 저장하기~~
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
1. ~~로그인 정보 저장하기 위한 JWT 어떻게 구현할지~~
2. ~~로그인 관련 테스트는 어떻게 짤지~~
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
