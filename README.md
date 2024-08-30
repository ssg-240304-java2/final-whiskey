# FINAL PROJECT

## TEAM 4. WHISKEY!!
<br>
![image_720](https://github.com/user-attachments/assets/910c2127-8552-40dd-aa76-88d6a8f29957)
<br>
- 배포 URL : https://web.dokalab.site
- test 계정
  - 일반 사용자
    - ID : regist2
    - PW : 1234
  - 점주
    - ID : lucas
    - PW : lucas
  - ADMIN
    - ID : admin@test.com
    - PW : admin

# 1. 팀원 구성
<div align="center">

| 노영록 | 이창민 | 최승인 | 김동휘 | 추형진 | 송재혁 |
| :------: |  :------: | :--------------: | :------: | :------: | :------: |
|[<img height="150" src="https://github.com/user-attachments/assets/934502e1-7367-4e61-9f7e-dc175c85dba6"> <br> @zZerorok](https://github.com/zZerorok)|[<img height="150" src="https://github.com/user-attachments/assets/37b962f1-9fd0-4a58-bac3-91cf944593f1"> <br> @l2chmnl](https://github.com/l2chmnl)|[<img height="150" src="https://github.com/user-attachments/assets/b09cb44f-e3f9-47cf-a813-8cc859b00d5b"> <br> @Lucas-Choi-17](https://github.com/Lucas-Choi-17)|[<img height="150" src="https://github.com/user-attachments/assets/0645ac17-7809-4ee4-8f58-f39b11b07b15"> <br> @DokaDev](https://github.com/DokaDev)|[<img height="150" src="https://github.com/user-attachments/assets/4e3ecfa2-1be1-4cfb-941b-3acb4ef507a7"> <br> @kebin0591](https://github.com/kebin0591)|[<img height="150" src="https://github.com/user-attachments/assets/b18a21d0-695b-4b48-a756-772e9d0d982e"> <br> @speter6501](https://github.com/speter6501)|
</div>


# 1. 프로젝트 설명
- TOPIC: 위치 및 영수증 기반 음식점 리뷰 커뮤니티 서비스
- SERVICE NAME: 푸드폴리오(FoodFolio)
- TEAM ORG: [Link Here](https://github.com/WhiskeyTeam)

# 2. 개발 기간
- 2024.07.19 - 2024.08.30 (42일)

# 3. 개발 환경
## 1. 인프라
- **Front** : HTML, CSS, JS
- **Back-end** : JAVA, Spring-Boot
- **Security** : Spring Security
- **KVM Based NCP Server** : Ubuntu based(Web / Database / Develop)
- **NCP Object Strage**
- **Pysical Data Server** : Ubuntu based
- **Docker**
- **GitHub Actions**

## 2. APIs / Libs
### _ Naver CLOVA OCR
- Document OCR
- Custom OCR

### _ Kakao API
- Kakao Map

### _ Social Login
- Naver Login
- Google Login

### _ WhiskeyTEAM Builds
- RestRequestInvoker : REST 요청에 따른 응답을 역직렬화하여 단순화하는 유틸리티성 라이브러리
- RestFileUploadLib : NCP Object Storage 기반 파일 업로드를 위한 중계 API 서버에 파일을 변환하여 전달하는 유틸리티
- StorageObjectUpload : NCP Object Storage에 파일을 전송하는 중계 REST API
- OcrRequestInvoker : OCR 요청의 응답 값을 불필요한 데이터는 소거하여 최적화된 응답을 반환하는 중계 REST API


# 4. 서버 아키텍쳐
![image_720](https://github.com/user-attachments/assets/757c981f-27f9-48c9-b980-a1e44a290f9e)


# 5. 프로젝트 후기
### 노영록
  1. 코드 이해도 향상
  프론트엔드와 연동하며 전체적인 코드의 구조와 동작 방식에 대해 더 깊이 이해하게 됐습니다.
  2. 커뮤니케이션의 중요성 인식
  커뮤니케이션이 원활할수록 코드 구현과 문제 해결이 더 수월하다는 것을 느꼈습니다.
  3. 요구사항 구현의 아쉬움과 시간 분배의 필요성
  원래 계획했던 요구사항 중 몇 가지는 구현하지 못했습니다. 이 과정에서 시간 관리의 중요성을 다시 한번 느끼게 됐고 앞으로는 시간 분배에 더 신경 써서 효율적으로 작업을 진행해, 계획된 요구사항들을 모두 구현할 수 있도록 해야겠다고 생각하게됐습니다.
  앞으로 이 경험을 바탕으로 더 나은 개발의 작업 흐름을 만들어 갈 수 있을 것 같습니다. 값진 경험이었습니다.

<br>

### 이창민
  어쩌다보니 신고 기능을 담당하게 되었다. 볼륨이 작고 어렵지 않은 기능이라 빨리 끝내고 다른 팀원들을 많이 도와주고 싶었다. 하지만 내 생각보다 내가 능숙하게 구현을 하지 못했고, 팀원들을 많이 도와주지 못해서 너무 미안했다. 그래서 더 열심히 했었던 것 같다. 문서 담당을 하게 되어 최대한 팀원들이 문서에 시간을 쏟지 않아도 되도록 문서쪽에도 열심히 기본 내용을 채워넣었다. 능력있는 팀원들을 만난 덕분에 RestAPI도 다뤄보고, 서버도 여러 개 사용해봤다. 그래서 기본적인 CRUD는 어느정도 기틀이 잡혔지만, Spring Security 부분은 많이 부족함을 느꼈다. 그래서 프로젝트가 끝난 후 혼자 구현을 다시 해봐겠다. RestAPI도 혼자 만들어 보고 싶지만 아직은 엄두가 나지 않아, 추후에 도전을 해봐야겠다.

<br>

### 최승인
  한 달이라는 긴 여정을 통해, 코드의 복잡함 속에서 제가 원하는 기능을 정확히 찾아내고 분별하는 능력을 확실히 기를 수 있었습니다. 그동안 배워왔던 기술들을 실제 프로젝트에 접목하면서, 그동안 헷갈렸던 개념들도 명확히 정리할 수 있었습니다. 하지만 한 달이라는 시간이 길다고 여유를 부리다 보니, 계획했던 모든 기능을 구현하지 못하는 한계를 경험하였고, 이를 통해 업무 계획과 일정 관리의 중요성을 다시금 깨닫게 되었습니다. 이번 프로젝트는 기술적 성장뿐만 아니라, 프로젝트 관리에 있어서도 큰 교훈을 준 값진 시간이었습니다.

<br>

### 김동휘
  웹 프로젝트 중 가장 긴 기간을 할당받은 프로젝트였음. 꾸준한 스크럼 및 리뷰 세션을 통한 협업을 이끌어낼 수 있었다고 생각함.

<br>

### 추형진
  이전부터 해보고 싶었던 학원 형님들과 같이 파이널 프로젝트인 만큼, 기대와 열정이 과정을 하면서 제일 컷었던 것 같습니다.
  그만큼 지금까지 했던 프로젝트들 중에 제일 노력을 쏟았지 않았나 싶습니다.
  이번 프로젝트에서 모든 프론트엔드를 책임지고 작업을 하며, 평소 부족했던 디자인 실력이 드러났었지만 팀원들의 격려에 끝까지 케어하고 신경을 많이 썼었습니다.
  백엔드 과정이지만 프론트를 하면서 한편으로는 아쉬움이 남았었지만 제가 할 일을 찾아 백엔드 개발자들이 구현을 편하게 하도록 미리 작업을 하며 JS나 연동 관련하여 어느 정도 이해와 배운게 많았던 것 같습니다.
  그동안 고생하신 팀원 분들에게도 너무 고생하셨다는 격려를 보냅니다. 모두 6개월동안 고생하셨습니다!!

<br>

### 송재혁
  너무 할수있다는 생각에 나태 했습니다. 선택과 집중이 부족했다고 생각했습니다. 빨리 끝낼수 있겠다는 마음으로 너무 질질 끌었던 제자신을 많이 반성하게 되는 마음을 가지게 되었습니다.

