# 기반 이미지로 eclipse-temurin-17을 사용한다.
FROM eclipse-temurin:17

# JAR_FILE 환경 변수를 설정한다.
ARG JAR_FILE=build/libs/*.jar

# JAR_FILE을 app.jar로 복사한다.
COPY ${JAR_FILE} app.jar

# app.jar를 실행한다.
# entrypoint를 사용하여 java -jar app.jar 커맨드를 실행한다.
ENTRYPOINT ["java","-jar","/app.jar"]

# 이미지 빌드
# docker build -t awesomedoka/{이미지 이름}:{태그} ./

# hub에 이미지 업로드
# docker push awesomedoka/{이미지 이름}:{태그}

# 안될경우 로그인 체크
# docker login