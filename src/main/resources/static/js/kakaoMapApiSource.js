// Kakao api 키
$.ajax({
    url: "/kakao/apiKey",
    async: false,
    type: 'get',
    success: function (key) {
        var kakaoApiScript = document.createElement('script');
        kakaoApiScript.type = 'text/javascript';
        kakaoApiScript.src = 'https://dapi.kakao.com/v2/maps/sdk.js?appkey=' + key + '&autoload=false&libraries=services,clusterer,drawing';
        document.head.prepend(kakaoApiScript);
    },
    error: function () {
        console.log("서버에서 지도를 받아올 수 없습니다.");
    }
})