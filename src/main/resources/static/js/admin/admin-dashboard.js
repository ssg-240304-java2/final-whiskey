$(document).ready(function() {
    console.log('Document ready');

    // FoodFolio 로고 클릭 이벤트 처리
    $('#foodfolio-logo').on('click', function(e) {
        e.preventDefault();
        loadContent('dashboard');
        updateActiveMenu($('.nav-link[data-target="dashboard"]'));
    });

    // 사이드바 메뉴 클릭 이벤트 처리
    $('.nav-link').on('click', function(e) {
        e.preventDefault();
        var target = $(this).data('target');
        console.log('Nav link clicked:', target);
        loadContent(target);
        updateActiveMenu(this);
    });

    // 빠른 링크 버튼 클릭 이벤트 처리 개선
    $(document).on('click', '.quick-link-btn', function(e) {
        e.preventDefault();
        var target = $(this).data('target');
        console.log('Quick link clicked:', target);
        loadContent(target);
        updateActiveMenu($('.nav-link[data-target="' + target + '"]'));
    });

    // 빠른 액세스 메뉴 클릭 이벤트 처리
    $('.dropdown-item[data-target]').on('click', function(e) {
        e.preventDefault();
        var target = $(this).data('target');
        loadContent(target);
        updateActiveMenu($('.nav-link[data-target="' + target + '"]'));
    });

    // 초기 대시보드 로드
    loadContent('dashboard');

    function loadContent(target) {
        var url = '/admin/' + target;
        $.ajax({
            url: url,
            type: 'GET',
            success: function(response) {
                $('#content-area').html(response);
                $('#page-title').text(getPageTitle(target));
                initializeCharts();
                highlightActiveButton(target);

                // 대시보드로 돌아갈 때 추가 작업
                if (target === 'dashboard') {
                    console.log('대시보드로 돌아왔습니다.');
                    // TODO: 대시보드 특정 초기화 작업이 필요하다면 여기에 추가
                }

                if (target === 'restaurant-management') {
                    loadRestaurantRegistrations();
                }
            },
            error: function(xhr, status, error) {
                console.error("콘텐츠 로딩 오류:", error);
                $('#content-area').html('<p>콘텐츠를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.</p>');
            }
        });
    }

    function updateActiveMenu(clickedItem) {
        $('.nav-link').removeClass('active');
        $(clickedItem).addClass('active');
    }

    function getPageTitle(target) {
        switch(target) {
            case 'dashboard': return '관리자 대시보드';
            case 'user-management': return '사용자 관리';
            case 'report-management': return '신고 관리';
            case 'statistics': return '통계';
            case 'restaurant-management': return '입점 관리';
            default: return 'FoodFolio 관리자';
        }
    }

    // 활성 버튼 하이라이트 함수
    function highlightActiveButton(target) {
        $('#dashboard .btn').removeClass('active');
        $('#dashboard .btn[data-target="' + target + '"]').addClass('active');
    }

    // 활성 메뉴 업데이트 함수
    function updateActiveMenu(clickedItem) {
        $('.nav-link').removeClass('active');
        $(clickedItem).addClass('active');
    }

    function initializeCharts() {
        if ($('#dailyActiveUsersChart').length) {
            new Chart($('#dailyActiveUsersChart'), {
                type: 'line',
                data: {
                    labels: ['월', '화', '수', '목', '금', '토', '일'],
                    datasets: [{
                        label: '일일 활성 사용자',
                        data: [120, 190, 300, 250, 200, 400, 380],
                        borderColor: 'rgb(75, 192, 192)',
                        tension: 0.1
                    }]
                }
            });
        }

        // TODO: 다른 차트들도 비슷한 방식으로 초기화
    }
});
