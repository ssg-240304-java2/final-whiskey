// 주소-좌표 변환 객체 생성
var geocoder = new kakao.maps.services.Geocoder();

if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        function (position) {
            var presentLocation = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
            geocoder.coord2Address(presentLocation.getLng(), presentLocation.getLat(), callback);
            var lat = presentLocation.getLat(),
                lng = presentLocation.getLng();

            getLocationRestaurant(lat, lng);
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
    var lat = presentLocation.getLat(),
        lng = presentLocation.getLng();
    getLocationRestaurant(lat, lng);
}

var callback = function (result, status) {
    if (status === kakao.maps.services.Status.OK) {
        document.getElementById('location').textContent = result[0].address.address_name;
    }
}

function getLocationRestaurant(lat, lng) {
    var restaurantGrid = document.getElementById("restaurant-grid");

    // 스켈레톤 UI 표시
    restaurantGrid.innerHTML = Array(6).fill().map(() => `
        <div class="restaurant-card skeleton">
            <div class="restaurant-image-placeholder skeleton-image"></div>
            <div class="restaurant-info">
                <div class="skeleton-text skeleton-title"></div>
                <div class="skeleton-text skeleton-details"></div>
                <div class="skeleton-text skeleton-hours"></div>
                <div class="skeleton-text skeleton-rating"></div>
            </div>
        </div>
    `).join('');

    $.ajax({
        url: "/restaurant/userLocationMain",
        type: "GET",
        data: {lat, lng},
        success: function (restaurantList) {
            // 스켈레톤 UI 제거
            restaurantGrid.innerHTML = '';

            restaurantList.forEach((restaurant, index) => {
                setTimeout(() => {
                    var element = document.createElement('div');
                    element.className = 'pop-in';
                    element.innerHTML = `<div class="restaurant-card">
                            <div class="restaurant-image-placeholder">
                                음식점 사진
                            </div>
                            <div class="restaurant-info">
                                <h3>${restaurant.name}</h3>
                                <p class="restaurant-details">
                                    <span class="distance">${restaurant.distance}</span>
                                    <span class="category">${restaurant.category}</span>
                                </p>
                                <p class="hours"><i class="far fa-clock"></i> <span>${restaurant.openCloseTime}</span></p>
                                <div class="rating">★★★★★</div>
                                <button class="find-order" onclick="location.href='/restaurant/${restaurant.id}/info'"><i class="fas fa-info-circle"></i> 상세 정보 / 주문</button>
                            </div>
                        </div>`;

                    restaurantGrid.appendChild(element);
                }, index * 200); // 각 카드를 150ms 간격으로 표시
            });
        },
        error: function () {
            console.log("데이터 불러오기 실패!!");
            // 에러 발생 시 사용자에게 알림
            restaurantGrid.innerHTML = '<p class="error-message">음식점 정보를 불러오는 데 실패했습니다. 다시 시도해 주세요.</p>';
        }
    });
}

// TODO: 필터링 기능 구현
// 정렬 기준과 음식 종류에 따른 필터링 로직을 추가해야 합니다.
// 선택된 필터 옵션에 따라 서버에 요청을 보내고 결과를 업데이트하는 함수를 구현해야 합니다.
function getDistance(lat1, lng1, lat2, lng2) {
    var R = 6371;
    var dLat = (lat2 - lat1) * (Math.PI / 180);
    var dLng = (lng2 - lng1) * (Math.PI / 180);
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * (Math.PI / 180)) * Math.cos(lat2 * (Math.PI / 180)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}