window.markers = [];

if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        function (position) {
            var presentLocation = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
            console.log(position.coords.latitude + " " + position.coords.longitude);
            displayMap(presentLocation);

            displayMarker(presentLocation);

            var latitude = $('#latitude ul');
            var longitude = $('#longitude ul');

            for (let i = 0; i < latitude.length; i++) {
                displayRestaurantMarker(new kakao.maps.LatLng(latitude[i].innerText, longitude[i].innerText));
                // 결과 카드에 호버 효과 추가
            }

        },
        function (err) {
            console.warn('ERROR(' + err.code + '): ' + err);
        }, {
            enableHighAccuracy: true,
            timeout: 5000,
            maximumAge: 0
        }
    )
} else {
    // geolocation을 사용하지 못하는 경우
    var location = new kakao.maps.LatLng(37.50973389280427, 127.05598102644028); // 임시 위치 - SAC 아트홀 위치
    displayMap(location);

    displayMarker(location);
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

function displayRestaurantMarker(location) {
    // 결과 음식점 마커 이미지
    var restaurantMarkerImageSrc = 'https://cdn4.iconfinder.com/data/icons/spirit20/bullet-black.png', // 마커이미지
        restaurantMarkerImageSize = new kakao.maps.Size(30, 30), // 마커이미지 크기
        restaurantMarkerImageOption = {offset: new kakao.maps.Point(15, 15)}; // 마커이미지 옵션 - 마커와 위치의 중심을 맞춤
    var restaurantMarkerImage = new kakao.maps.MarkerImage(restaurantMarkerImageSrc, restaurantMarkerImageSize, restaurantMarkerImageOption); // 마커이미지 생성

    // pinned 음식점 마커 이미지
    var pinnedMarkerImageSrc = 'https://t1.daumcdn.net/mapjsapi/images/marker.png', // 마커이미지
        pinnedMarkerImageSize = new kakao.maps.Size(32, 46), // 마커이미지 크기
        pinnedMarkerImageOption = {offset: new kakao.maps.Point(16, 46)}; // 마커이미지 옵션 - 마커와 위치의 중심을 맞춤
    var pinnedMarkerImage = new kakao.maps.MarkerImage(pinnedMarkerImageSrc, pinnedMarkerImageSize, pinnedMarkerImageOption); // 마커이미지 생성

    // 마커 생성
    var marker = new kakao.maps.Marker({
        map: window.map, // 마커를 표시할 지도
        position: location, // 마커 표시 위치
        image: restaurantMarkerImage
    });

    window.markers.push(marker);

    var restaurantCard = $('.result-card-link');
    var card = document.querySelectorAll('.result-card')[window.markers.length - 1];
    kakao.maps.event.addListener(marker, 'mouseover', function () {
        marker.setImage(pinnedMarkerImage);
        card.style.borderColor = '#f15c4f';
        card.style.boxShadow = '0 4px 10px rgba(0, 0, 0, 0.2);';
    });
    kakao.maps.event.addListener(marker, 'mouseout', function () {
        marker.setImage(restaurantMarkerImage);
        card.style.borderColor = '';
        card.style.boxShadow = '';
    });

    restaurantCard.mouseover(function () {
        var restaurantIndex = $(this).attr('id');
        markers.at(restaurantIndex).setImage(pinnedMarkerImage);
    });
    restaurantCard.mouseout(function () {
        var restaurantIndex = $(this).attr('id');
        markers.at(restaurantIndex).setImage(restaurantMarkerImage);
    });
}