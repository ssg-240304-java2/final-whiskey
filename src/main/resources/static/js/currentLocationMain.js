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

    $.ajax({
        url: "/restaurant/userLocationMain",
        type: "GET",
        data: {lat, lng},
        success: function (restaurantList) {
            for (let restaurant of restaurantList) {
                var restaurantGrid = document.getElementById("restaurant-grid");
                var element = document.createElement('div');

                var dayOfWeek = new Date().getDay();


                var openCloseTime;

                function avoidNotNull(obj) {
                    if (obj) {
                        console.log(obj);
                        let openTime = obj.openTime ? obj.openTime : '00:00';
                        let closeTime = obj.closeTime ? obj.closeTime : '00:00';
                        return openTime + " - " + closeTime;
                    }
                    return "00:00 - 00:00";
                }

                if (restaurant.weeklyOpenCloseTime) {
                    switch (dayOfWeek) {
                        case 0:
                            openCloseTime = avoidNotNull(restaurant.weeklyOpenCloseTime.sunday);
                            break;
                        case 1:
                            openCloseTime = avoidNotNull(restaurant.weeklyOpenCloseTime.monday);
                            break;
                        case 2:
                            openCloseTime = avoidNotNull(restaurant.weeklyOpenCloseTime.tuesday);
                            break;
                        case 3:
                            openCloseTime = avoidNotNull(restaurant.weeklyOpenCloseTime.wednesday);
                            break;
                        case 4:
                            openCloseTime = avoidNotNull(restaurant.weeklyOpenCloseTime.thursday);
                            break;
                        case 5:
                            openCloseTime = avoidNotNull(restaurant.weeklyOpenCloseTime.friday);
                            break;
                        case 6:
                            openCloseTime = avoidNotNull(restaurant.weeklyOpenCloseTime.saturday);
                            break;
                    }
                }

                var distance = getDistance(lat, lng, restaurant.address.latitude, restaurant.address.longitude);

                if (distance >= 1) {
                    distance = distance.toFixed(1) + "km";
                } else {
                    distance = (distance * 1000).toFixed(0) + "m";
                }

                element.innerHTML = `
                    <div class="restaurant-card">
                        <div class="restaurant-image-placeholder">
                            음식점 사진
                        </div>
                        <div class="restaurant-info">
                            <h3>${restaurant.name}</h3>
                            <p class="restaurant-details">
                                <span class="distance">${distance}</span>
                                <span class="category">${restaurant.category}</span>
                            </p>
                            <p class="hours"><i class="far fa-clock"></i> <span>${openCloseTime}</span></p>
                            <div class="rating">★★★★★</div>
                            <button class="find-order"><i class="fas fa-info-circle"></i> 상세 정보 / 주문</button>
                        </div>
                    </div>`;

                restaurantGrid.appendChild(element);
            }
        },
        error: function () {
            console.log("데이터 불러오기 실패!!");
        }
    });
}

function getDistance(lat1, lng1, lat2, lng2) {
    var R = 6371;
    var dLat = (lat2 - lat1) * (Math.PI / 180);
    var dLng = (lng2 - lng1) * (Math.PI / 180);
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * (Math.PI / 180)) * Math.cos(lat2 * (Math.PI / 180)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}