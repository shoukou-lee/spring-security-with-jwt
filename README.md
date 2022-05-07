### Spring Security & JWT 강의를 듣고, 처음부터 스스로 만들어봅니다.
- 강의를 참고한 구현은 lecture 브랜치에 위치합니다. [Java-JWT](https://github.com/auth0/java-jwt) 라이브러리를 사용합니다.
- 다양한 자료를 참고해 다시 구현해본 버전은 master 브랜치에 위치합니다. [jjwt](https://github.com/jwtk/jjwt) 라이브러리를 사용합니다. 
  - Private claims이 추가됩니다.
  - SecurityContext에 저장된 인증 정보를 가져와 API Requester를 식별할 수 있습니다.
  - 토큰 재발행, 로그아웃 기능이 추가됩니다. 토큰 관리를 위해 [Redis](https://redis.io/)를 활용합니다.
  
### reffered to : 
- [스프링부트 시큐리티 & JWT 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0) 
- [Lecturer's GitHub Repository](https://github.com/codingspecialist/Springboot-Security-JWT-Easy)