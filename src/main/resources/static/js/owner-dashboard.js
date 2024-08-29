// DOM이 로드되면 실행
document.addEventListener('DOMContentLoaded', function () {
    // 사이드바 링크와 콘텐츠 영역, 페이지 제목 요소 선택
    const sidebarLinks = document.querySelectorAll('#sidebar .nav-link');
    const contentArea = document.getElementById('content-area');
    const pageTitle = document.getElementById('page-title');

    function loadFragment(targetId) {
        fetch(`/fragments/owner/${targetId}`)
            .then(response => response.text())
            .then(html => {
                contentArea.innerHTML = html;
                // 동적으로 로드된 스크립트 실행
                const scripts = contentArea.getElementsByTagName('script');
                Array.from(scripts).forEach(script => {
                    const newScript = document.createElement('script');
                    newScript.src = script.src;
                    document.body.appendChild(newScript);
                });
                // 페이지별 초기화 함수 호출
                if (targetId === 'restaurant-info') {
                    initRestaurantInfo();
                } else if (targetId === 'menu-management') {
                    initMenuManagement();
                } else if (targetId === 'dashboard') {
                    initDashboard();
                }
            })
            .catch(error => {
                console.error('Error loading content:', error);
                contentArea.innerHTML = '<p>콘텐츠를 불러오는 중 오류가 발생했습니다.</p>';
            });
    }

    // 사이드바 링크 클릭 이벤트 수정
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            sidebarLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
            const targetId = this.getAttribute('data-bs-target').substring(1);
            pageTitle.textContent = this.textContent.trim();
            loadFragment(targetId);
        });
    });

    // 초기 로드 시 대시보드 프래그먼트 로드
    loadFragment('dashboard');
});

function initializeModals() {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        new bootstrap.Modal(modal);
    });
}

// 대시보드 초기화 함수
function initDashboard() {
    console.log('Dashboard initialized');
    // 대시보드 특정 초기화 로직 (필요한 경우)
}

function initRestaurantInfo() {
    console.log('Restaurant Info initialized');
    // 음식점 정보 페이지 특정 초기화 로직
}

// 메뉴 관리 초기화 함수
function initMenuManagement() {
    console.log('Menu Management initialized');
    // menu-management.js의 초기화 함수 호출
    if (typeof initializeMenuManagement === 'function') {
        initializeMenuManagement();
    }
}

// 다른 페이지들의 초기화 함수도 필요에 따라 추가
