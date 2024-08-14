// 주소-좌표 변환 객체 생성
var geocoder = new kakao.maps.services.Geocoder();

if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        function (position) {
            var presentLocation = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
            console.log("확인");
            geocoder.coord2Address(presentLocation.getLng(), presentLocation.getLat(), callback);
        }, function (err) {
            console.warn('ERROR(' + err.code + '): ' + err);
        },
        {
            enableHighAccuracy: true,
            timeout: 5000,
            maximumAge: 0
        }
    )

} else {
    console.log('현재 위치를 알 수 없어 기본 위치로 설정합니다.');
    var presentLocation = new kakao.maps.LatLng(33.450701, 126.570667);
    geocoder.coord2Address(presentLocation.getLng(), presentLocation.getLat(), callback);
}

var callback = function (result, status) {
    if (status === kakao.maps.services.Status.OK) {
        console.log(result[0].address.address_name);
        document.getElementById('location').textContent = result[0].address.address_name;
    }
}