![realworld spring boot](docs/images/realworld-springboot.png)

---

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=spring-org_realworld-springboot-monolith&metric=coverage)](https://sonarcloud.io/dashboard?id=spring-org_realworld-springboot-monolith)


> ### realworld-spring-boot-example-sr

- 해당 프로젝트는 SpringBoot를 기반으로 CRUD 작업 및 인증, 라우팅, 페이징 기능을 구현하기 위한 프로젝트이다.
- 또 다른 방식으로 구현한 프론트 및 백엔드에 대한 자세한 내용은 [RealWorld Repository](https://github.com/gothinkster/realworld)를 참고해주세요.

# How it works

> Describe the general architecture of your app here

## Code Coverage

> **gradle test**

- 프로젝트의 `단위 테스트`를 수행하는 방법

```shell
$ ./gradlew clean test
```

> **gradle jacoco**

- Java Code의 Coverage를 체크
    - 바이너리 커버리지 결과를 리포트

```shell
$ ./gradlew clean
$ ./gradlew --console verbose test jacocoTestReport
```

- Java Code의 Coverage를 체크 및 Coverage의 기준을 만족하는지 확인하는 task
    - Coverage 기준에 만족되지 않는 경우 빌드가 실패하게 된다.

```shell
$ ./gradlew clean
$ ./gradlew --console verbose test jacocoTestReport jacocoTestCoverageVerification
```

- 위 shell을 하나의 명령어로 실행
    - 해당 작업을 위한 사전 작업 필요 [build.gradle](https://github.com/realworld-club/realworld-spring-boot-example-sr/blob/29a3b2b1c180fc918f3b77cbd68acdb3fbd6ab66/build.gradle#L39)

```shell
./gradlew --console verbose testCoverage
```

> **Code Coverage**는 프로젝트에 대한 견고함의 **지표**가 될 수 있다.

- 코드 커버리지에 만족할 때만 배포를 가능하게함으로써 프로젝트에 대한 안전성을 유지할 수 있다.
- 프로덕션 코드에 대한 이해를 높이고, 대규모 리펙토링에 대한 영향을 줄일 수 있다.

> **주의사항**

- 테스트 커버리지의 높은 지표는 테스트의 시간을 늦춘다.
    - 이는 생산성에 대한 저하 문제를 발생시킨다.
- 위 문제를 해결하기 위해서는 스프링 애플리케이션 컨텍스트 로딩을 발생시키는 테스트를 모두 제거
- 컨텍스트 로딩 없이 테스트 할 수 있는 standalone 기능인 WebTestClient를 사용한다.
- 제거가 어려운 경우 Mocking을 활용한다.
- 느려지인 테스트 코드에 대한 프로파일링이 필요

# Getting started

> npm install, npm start, etc.
