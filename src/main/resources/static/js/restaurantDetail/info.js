RestaurantDetail.initInfo = function() {
    initMap();
    setupViewFullMenuButton();
    setupRestaurantReportButton();
};

function initMap() {
    console.log('지도 초기화');
    // 카카오 지도 API 초기화 및 설정
}

function setupViewFullMenuButton() {
    const viewFullMenuBtn = document.querySelector('.view-full-menu');
    if (viewFullMenuBtn) {
        viewFullMenuBtn.addEventListener('click', function() {
            console.log('전체 메뉴 보기');
            // 전체 메뉴 모달 표시 로직
        });
    }
}

function setupRestaurantReportButton() {
    const restaurantReportBtn = document.getElementById('restaurantReport');
    if (restaurantReportBtn) {
        restaurantReportBtn.addEventListener('click', function() {
            console.log('레스토랑 신고하기');
            // 신고 모달 표시 로직
        });
    }
}