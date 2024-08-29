// DOM이 로드되면 실행
document.addEventListener('DOMContentLoaded', function () {
    unansweredInquiryCount();
    // 사이드바 링크와 콘텐츠 영역, 페이지 제목 요소 선택
    const sidebarLinks = document.querySelectorAll('#sidebar .nav-link');
    const quickLinkBtns = document.querySelectorAll('.quick-link-btn');
    const contentArea = document.getElementById('content-area');
    const pageTitle = document.getElementById('page-title');
    // 사이드바 토글 기능 추가
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');

    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.classList.toggle('active');
        });
    }
    
    // 로고 클릭 이벤트 리스너 추가
    const logo = document.querySelector('.foodfolio-logo');
    logo.addEventListener('click', function (e) {
        e.preventDefault();
        loadContent('dashboard');
    });

    // 화면 크기 변경 시 사이드바 상태 조정
    window.addEventListener('resize', function() {
        if (window.innerWidth >= 768) {
            sidebar.classList.remove('active');
        }
    });

    // 콘텐츠 영역 클릭 시 모바일에서 사이드바 닫기
    contentArea.addEventListener('click', function() {
        if (window.innerWidth < 768) {
            sidebar.classList.remove('active');
        }
    });

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

// <<<<<<< feat/owner_review_bind
    // 초기 로드 시 대시보드 프래그먼트 로드
    loadFragment('dashboard');
// =======
//     function loadContent(targetId) {
//         // 모든 링크에서 'active' 클래스 제거
//         sidebarLinks.forEach(l => l.classList.remove('active'));
        
//         // 해당하는 사이드바 링크에 'active' 클래스 추가
//         const activeLink = document.querySelector(`#sidebar .nav-link[data-bs-target="#${targetId}"]`);
//         if (activeLink) {
//             activeLink.classList.add('active');
//             pageTitle.textContent = activeLink.textContent.trim();
//         }

//         // AJAX를 사용하여 해당 프래그먼트 로드
//         fetch(`/fragments/owner/${targetId}.html`)
//             .then(response => response.text())
//             .then(html => {
//                 contentArea.innerHTML = html;
//                 // 동적으로 로드된 스크립트 실행
//                 const scripts = contentArea.getElementsByTagName('script');
//                 Array.from(scripts).forEach(script => {
//                     const newScript = document.createElement('script');
//                     newScript.src = script.src;
//                     document.body.appendChild(newScript);
//                 });
//                 // 모달 초기화 함수 호출
//                 initializeModals();
//                 // 페이지별 초기화 함수 호출
//                 if (typeof window[`init${targetId.charAt(0).toUpperCase() + targetId.slice(1)}`] === 'function') {
//                     window[`init${targetId.charAt(0).toUpperCase() + targetId.slice(1)}`]();
//                 }
//             })
//             .catch(error => {
//                 console.error('Error loading content:', error);
//                 contentArea.innerHTML = '<p>콘텐츠를 불러오는 중 오류가 발생했습니다.</p>';
//             });
//     }

//     // 사이드바 링크 이벤트 리스너
//     sidebarLinks.forEach(link => {
//         link.addEventListener('click', function (e) {
//             e.preventDefault();
//             const targetId = this.getAttribute('data-bs-target').substring(1);
//             loadContent(targetId);
//         });
//     });

//     // 빠른 링크 버튼 이벤트 리스너
//     quickLinkBtns.forEach(btn => {
//         btn.addEventListener('click', function (e) {
//             e.preventDefault();
//             const targetId = this.getAttribute('data-bs-target');
//             loadContent(targetId);
//         });
//     });

//     // 초기 로드 시 첫 번째 사이드바 링크(대시보드) 클릭
//     sidebarLinks[0].click();
// >>>>>>> devel
});

function initializeModals() {
    const modals = document.querySelectorAll('.modal:not(.initialized)');
    modals.forEach(modal => {
        new bootstrap.Modal(modal);
        modal.classList.add('initialized');
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

// 미답변 문의 카운트
function unansweredInquiryCount() {
    const restaurantId = document.getElementById('restaurantId').value;

    $.ajax({
        url: `/restaurant/${restaurantId}/inquiries/unanswered-count`,
        type: 'GET',
        cache: false,
        success: function(count) {
            $('.unansweredInquiryCount').text(count + '건');
        },
        error: function(xhr, status, error) {
            console.error("미답변 문의 수를 불러오는 중 오류 발생", status, error);
        }
    });
}
// 다른 페이지들의 초기화 함수도 필요에 따라 추가
