if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        function (position) {
            var presentLocation = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
            displayMap(presentLocation);

            displayMarker(presentLocation);
        },
        function (err) {
            console.warn('ERROR(' + err.code + '): ' + err);
        }, {
            enableHighAccuracy: true,
            timeout: 5000,
            maximumAge: 0
        }
    )
}

function displayMap(location) {
    var mapContainer = document.getElementById('map-container'), // 지도를 표시할 div
        mapOption = {
            center: location, // 지도의 중심 좌표
            level: 3 // 지도의 확대 레벨
        };

    window.map = new kakao.maps.Map(mapContainer, mapOption);
}

function displayMarker(location) {
    var markerImageSrc = '//t1.daumcdn.net/localimg/localimages/07/2018/mw/m640/ico_marker.png', // 마커이미지
        markerImageSize = new kakao.maps.Size(30, 30), // 마커이미지 크기
        markerImageOption = {offset: new kakao.maps.Point(15, 15)}; // 마커이미지 옵션 - 마커와 위치의 중심을 맞춤

    var markerImage = new kakao.maps.MarkerImage(markerImageSrc, markerImageSize, markerImageOption); // 마커이미지 생성
    
    // 마커 생성
    var marker = new kakao.maps.Marker({
        map: window.map, // 마커를 표시할 지도
        position: location, // 마커 표시 위치
        image: markerImage // 마커 이미지
    });
}