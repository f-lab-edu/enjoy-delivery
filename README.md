## enjoy-delivery 서버 구조도
![enjoy-delivery](https://user-images.githubusercontent.com/29730565/147025438-31c0bad7-c466-4b6d-b836-3b371b36dcdc.png)
Spring boot, Java, Spring data JPA, Mysql, Redis, Jenkins, Github, naver cloud flatform, firebase


## 프로젝트 목표
* 배달의 민족같은 배달 앱 서비스를 구현해 내는 것이 목표입니다.
* 단순 기능 구현뿐 아니라 확장성 있는 서비스를 구현하는 것이 목표입니다.
* 객체지향 원리와 Ioc/DI, Fetch join을 적용함으로써 더 나은 코드를 작성하는 것이 목표입니다.
* 스터디와 코드 리뷰, 페어프로그래밍을 통해 함께 자라기가 목표입니다.

## 기술적 issue 해결 과정
* [ EnjoyDelivery  이슈 1. 가게 데이터 조회 시 N+1문제 해결하기 - Fetch join, @EntityGraph](https://velog.io/@meme2367/EnjoyDelivery-%EC%9D%B4%EC%8A%88-1.-%EA%B0%80%EA%B2%8C-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A1%B0%ED%9A%8C-%EC%8B%9C-N1%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0-Fetch-join-EntityGraph)

* [ EnjoyDelivery  이슈 2. 장바구니 메뉴 조회 시 redis의 O(N) 명령어를 O(1) 명령어로 변경](https://velog.io/@meme2367/EnjoyDelivery-%EC%9D%B4%EC%8A%88-3.-%EC%9E%A5%EB%B0%94%EA%B5%AC%EB%8B%88-%EA%B8%B0%EB%8A%A5%EC%97%90-Redis-ON-%EB%AA%85%EB%A0%B9%EC%96%B4%EB%A5%BC-O1-%EB%AA%85%EB%A0%B9%EC%96%B4%EB%A1%9C-%EB%B3%80%EA%B2%BD)


* [ EnjoyDelivery  이슈 3. 다중 서버에서 인증 정보는 어디에 저장할까 : Session Storage](https://velog.io/@meme2367/EnjoyDelivery-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%9D%98-%EC%9D%B4%EC%8A%88-3.-%EB%8B%A4%EC%A4%91-%EC%84%9C%EB%B2%84%EC%97%90%EC%84%9C-%EC%9D%B8%EC%A6%9D-%EC%A0%95%EB%B3%B4%EB%8A%94-%EC%96%B4%EB%94%94%EC%97%90-%EC%A0%80%EC%9E%A5%ED%95%A0%EA%B9%8C-Session-Storage)

* [ EnjoyDelivery  이슈 4. 다중 서버에서의 CI / CD : Jenkins Pipeline](https://velog.io/@meme2367/EnjoyDelivery-%EC%9D%B4%EC%8A%88-4.-%EB%8B%A4%EC%A4%91-%EC%84%9C%EB%B2%84%EC%97%90%EC%84%9C%EC%9D%98-CI-CD)


## 프로젝트 중점사항
* 서버의 확장성
* 다중 서버 환경에서 Session Storage 활용한 인증 구현
* 새로운 스레드풀을 만들고 @Async를 이용하여 비동기 푸쉬 알람 서비스 구현
* 가게 데이터 조회 시 N+1문제 해결하기
* 의존적이지 않은 단위테스트 작성
* Redis Cache를 이용해 장바구니 기능 구현
* 스프링의 @Transactional을 이용하여 작업의 완전성 보장하기
* Jenkins pipeline을 사용하여 CI/CD 환경 구축
* Redis scan을 활용하여 장바구니 조회 성능 최적화
* naver Cloud Platform을 이용하여 로드밸런싱
* Mysql Replication - Master/Slave 데이터 이중화 적용(미완성)
* Ngrinder를 이용하여 connection pool 테스트(미완성)


## DB ERD

## Class Diagram

## Front

