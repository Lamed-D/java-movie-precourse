# 개발 환경 설치 가이드 (java-movie-precourse)

이 문서는 Windows 기준으로 작성되었으며, macOS/Linux 명령도 함께 제공합니다.

## 1. 사전 준비물
- JDK 21 이상 (Temurin 권장)
- Git
- IDE: IntelliJ IDEA 또는 VS Code(Java 확장팩)

설치 링크 예시
- JDK(Temurin): `https://adoptium.net`
- Git: `https://git-scm.com/downloads`
- IntelliJ: `https://www.jetbrains.com/idea/`
- VS Code: `https://code.visualstudio.com/`

## 2. JDK 설정 확인
Windows (PowerShell)
```
java -version
$env:JAVA_HOME
```
- `java -version` 결과가 21 이상이어야 합니다.
- `JAVA_HOME`이 비어있으면 환경 변수 설정 권장.

Windows에서 JAVA_HOME 설정(예)
```
# 관리자 권한 PowerShell
setx JAVA_HOME "C:\\Program Files\\Eclipse Adoptium\\jdk-21" /M
setx PATH "%PATH%;%JAVA_HOME%\\bin" /M
```
PowerShell 세션을 재시작 후 다시 확인하세요.

macOS/Linux
```
java -version
/usr/libexec/java_home -v 21   # macOS
```
필요 시 `~/.zshrc` 또는 `~/.bashrc`에 JAVA_HOME을 추가하세요.

## 3. 저장소 클론
Windows (PowerShell)
```
cd C:\\Users\\<사용자명>\\Documents\\GitHub
git clone https://github.com/<YOUR_ACCOUNT>/java-movie-precourse.git
cd java-movie-precourse
```
macOS/Linux
```
cd ~/workspace
git clone https://github.com/<YOUR_ACCOUNT>/java-movie-precourse.git
cd java-movie-precourse
```

## 4. 빌드 · 테스트 · 실행
Gradle Wrapper가 포함되어 있어 별도 Gradle 설치가 필요 없습니다.

Windows
```
# 빌드
.\\gradlew build

# 테스트
.\\gradlew test

# (선택) 실행 — 콘솔 UI 추가 시 사용
.\\gradlew run
```
macOS/Linux
```
# 빌드
./gradlew build

# 테스트
./gradlew test

# (선택) 실행 — 콘솔 UI 추가 시 사용
./gradlew run
```

## 5. IDE 가져오기
- IntelliJ IDEA: Open → 프로젝트 루트 선택 → Gradle 프로젝트로 자동 인식
- VS Code: 폴더 열기 → Java Extension Pack 설치 → Gradle Tasks 사용

## 6. 커밋 컨벤션 요약 (AngularJS)
형식: `<type>(scope): <subject>`
- type: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`
- 예: `feat(discount): 무비데이 10% 비율 할인 구현`

권장 커밋 순서(요약)
1) README 계획 수립 → 2) 도메인 모델 → 3) 가격/할인 → 4) 검증/예외 → 5) 테스트 → 6) 리팩터링/문서

## 7. 자주 묻는 문제 (Troubleshooting)
- Gradle 다운로드 지연: 네트워크 프록시/방화벽 확인. 필요 시 미러 저장소 사용.
- `JAVA_HOME` 미설정: 2장 참조하여 환경 변수 재설정.
- Windows 스크립트 실행 문제: `Get-ExecutionPolicy` 확인. 필요 시 `RemoteSigned`로 조정.
- 빌드 실패: `./gradlew clean build` 또는 Windows에서 `.\\gradlew clean build`로 캐시 초기화 후 재시도.

## 8. 프로젝트 구조 참고
자세한 기능/전략은 `README.md` 참고:
- 요구사항 TL;DR
- 가격·할인 규칙 표 및 계산 순서
- 구현 기능 목록(커밋 단위)과 패키지 구조

---
문의/개선 제안은 이슈 또는 PR로 남겨주세요.
