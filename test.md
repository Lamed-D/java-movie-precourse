# 테스트 가이드 (TEST)

이 문서는 설치/사용 문서(`INSTALL.md`, `USAGE.md`)를 바탕으로 Java 21 환경 확인과 테스트 실행 방법을 정리합니다.

## 1) Java 21 버전 확인
터미널에서 다음 명령을 실행해 Java 버전을 확인하세요.

- 공통
```
java -version
```
- 출력 예시 (버전은 환경에 따라 다를 수 있습니다)
```
openjdk version "21.0.x" 2025-xx-xx
```
실 출력:
java version "21.0.8" 2025-07-15 LTS
Java(TM) SE Runtime Environment (build 21.0.8+12-LTS-250)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.8+12-LTS-250, mixed mode, sharing)

IDE(Eclipse/IntelliJ IDEA)에서도 Project SDK/Language Level이 Java 21로 설정되어 있는지 확인하세요.

## 2) 테스트 실행 (Gradle Wrapper 사용)
Gradle를 따로 설치할 필요 없이 Wrapper를 사용합니다.

- macOS / Linux
```
./gradlew clean test
```

- Windows (PowerShell 또는 CMD)
```
./gradlew.bat clean test
```
또는
```
gradlew.bat clean test
```

## 3) 기대 결과
테스트가 통과하면 아래와 유사한 메시지가 표시됩니다.
```
BUILD SUCCESSFUL in 0s
```

실제 출력:
C:\Users\Lamed\Documents\GitHub\java-movie-precourse>gradlew.bat clean test

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.7/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 2s
4 actionable tasks: 4 executed

실제 시간 값은 환경에 따라 달라질 수 있습니다.

## 4) 무엇이 검증되나요?
- 할인 순서: 무비데이(10%) → 시간(2,000원) → 포인트 → 결제수단(카드 5%/현금 2%)
- 시간 겹침 차단: 같은 예매에서 상영 시간이 겹치면 예외
- 좌석 중복 차단: 동일 상영에서 이미 예약된 좌석은 예외
- 경계값: 11:00, 20:00 시작 시 시간 할인 적용 여부

자세한 시나리오는 테스트 코드(`src/test/java`)를 참고하세요.

## 5) 문제 해결 (Troubleshooting)
- Java 버전이 21이 아님: `INSTALL.md`의 JDK 설정 절차를 따라 `JAVA_HOME`을 재설정하세요.
- Gradle 다운로드 지연/실패: 네트워크 프록시/방화벽을 확인하고, 필요 시 미러를 사용하세요. `INSTALL.md` 참고.
- 테스트 실패: 변경한 코드가 할인 순서, 겹침 규칙, 좌석 중복 규칙을 위반했는지 확인하세요. 사용 예시는 `USAGE.md` 참고.

## 6) 추가 참고
- 설치/환경 구성: `INSTALL.md`
- 도메인/규칙/사용 예시: `README.md`, `USAGE.md`
