// TODO: 음식점의 주소를 전달받아 음식점의 주소를 표시
var restaurantLocation = new kakao.maps.LatLng($('input[id=latitude]').val(), $('input[id=longitude]').val()); // 음식점의 좌표

var mapContainer = document.getElementById('map'),
    mapOption = {
        center: restaurantLocation, // 음식점의 좌표
        level: 5
    };

var map = new kakao.maps.Map(mapContainer, mapOption);

// 음식점의 좌표에 마커 생성
var marker = new kakao.maps.Marker({
    position: restaurantLocation
});

marker.setMap(map);
