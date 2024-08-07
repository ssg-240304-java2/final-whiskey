# FINAL PROJECT

TEAM 4. WHISKEY!!

### OVERVIEW
- TOPIC: 위치 및 영수증 기반 음식점 리뷰 커뮤니티 서비스
- SERVICE NAME: 푸드폴리오(FoodFolio)
- TEAM ORG: [Link Here](https://github.com/WhiskeyTeam)

### USED
#### 1. 인프라
- KVM Based NCP Server : Ubuntu based(Web / Database / Develop)
- NCP Object Strage
- Pysical Data Server : Ubuntu based
- Docker
- GitHub Actions

#### 2. APIs / Libs
##### _ Naver CLOVA OCR
- Document OCR
- Custom OCR

##### _ Social Login
- Naver Login
- ...

##### _ WhiskeyTEAM Builds
- RestRequestInvoker : REST 요청에 따른 응답을 역직렬화하여 단순화하는 유틸리티성 라이브러리
- RestFileUploadLib : NCP Object Storage 기반 파일 업로드를 위한 중계 API 서버에 파일을 변환하여 전달하는 유틸리티
- StorageObjectUpload : NCP Object Storage에 파일을 전송하는 중계 REST API
- OcrRequestInvoker : OCR 요청의 응답 값을 불필요한 데이터는 소거하여 최적화된 응답을 반환하는 중계 REST API