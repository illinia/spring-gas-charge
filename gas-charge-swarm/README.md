### 도커 스웜, 스택

``docker swarm init``

최초 시작, 이미지 업데이트시

``docker stack deploy -c docker-compose.yml gas-charge``

### 도커 이미지

각 디렉토리 별로 이미지 빌드, 로컬 레지스트리 푸시하는 스크립트 사용

### private registry 등록

``docker run -d -p 5000:5000 --restart always reistry``

``echo $'\n127.0.0.1 registry.local' | sudo tee -a /etc/hosts``

``docker image tag 이미지이름:태그 registry.local:5000/그룹이름/이미지이름:태그``

``docker image push registry.local:5000/그룹이름/이미지이름:태그``

### h2 db 로컬에서 실행할때

``vim h2.sh``

명령어 추가

``-web -webAllowOthers -webPort 8082 -tcp -tcpAllowOthers -tcpPort 1521`` 

접속시

``jdbc:h2:tcp://localhost:1521/~/gas-charge;MODE=MYSQL``