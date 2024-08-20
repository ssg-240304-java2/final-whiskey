// 전역 객체 생성
window.RestaurantDetail = {
    initInfo: null,
    initReviews: null,
    initInquiries: null,
    initNotices: null
};

document.addEventListener('DOMContentLoaded', function() {
    setupTabs();
    setupModals();
    setupReports();
    // 초기 URL 설정
    setInitialTab();
});

// 탭 설정 함수
function setupTabs() {
    const tabs = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const tabId = tab.getAttribute('data-tab');
            activateTab(tab, tabId);
            // URL 변경 (브라우저 히스토리에 추가)
            history.pushState(null, '', `/restaurant/${tabId}`);
        });
    });

    // 탭 활성화 함수
    function activateTab(clickedTab, tabId) {
        // 모든 탭과 컨텐츠의 active 클래스 제거
        tabs.forEach(t => t.classList.remove('active'));
        tabContents.forEach(content => content.classList.remove('active'));

        // 클릭된 탭과 해당 컨텐츠에 active 클래스 추가
        clickedTab.classList.add('active');
        const activeContent = document.getElementById(tabId);
        if (activeContent) {
            activeContent.classList.add('active');
            // 탭 전환 시 해당 탭의 초기화 함수 호출 (동적으로 함수 이름 생성)
            if (RestaurantDetail[`init${tabId.charAt(0).toUpperCase() + tabId.slice(1)}`]) {
                RestaurantDetail[`init${tabId.charAt(0).toUpperCase() + tabId.slice(1)}`]();
            }
        }
    }

    // URL 변경 시 탭 활성화 (브라우저 뒤로가기/앞으로가기 대응)
    window.addEventListener('popstate', setInitialTab);
}

// 초기 탭 설정 함수
function setInitialTab() {
    const path = window.location.pathname;
    const tabId = path.split('/').pop() || 'info';  // URL에서 탭 ID 추출, 없으면 'info'
    const tab = document.querySelector(`.tab[data-tab="${tabId}"]`);
    if (tab) {
        tab.click();  // 해당 탭 클릭 이벤트 발생
    } else {
        // 기본 탭(info) 활성화
        const defaultTab = document.querySelector('.tab[data-tab="info"]');
        if (defaultTab) {
            defaultTab.click();
        }
    }
}

// 모달 설정 함수
function setupModals() {
    // 모달 설정을 전역 객체에 추가
    window.RestaurantDetail.setupModal = function(modalId) {
        const modal = document.getElementById(modalId);
        const closeBtn = modal.querySelector('.close');

        // 닫기 버튼 클릭 시 모달 숨김
        closeBtn.onclick = function() {
            modal.style.display = 'none';
        };

        // 모달 외부 클릭 시 모달 숨김
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        };
    };

    // 공지사항 모달 설정
    RestaurantDetail.setupModal('noticeModal');

    // 답변 모달 설정
    RestaurantDetail.setupModal('replyModal');
}

// 신고 기능 설정 함수
function setupReports() {
    let reportType;
    let idx;

    // 모달 열기 이벤트 리스너 설정
    document.getElementById('restaurantReport').addEventListener('click', function() {
        document.getElementById('modal').style.display = 'block';
        reportType = "restaurant";
    });
    document.getElementById('reviewReport').addEventListener('click', function() {
        document.getElementById('modal').style.display = 'block';
        reportType = "review";
    });
    document.getElementById('commentReport').addEventListener('click', function() {
        document.getElementById('modal').style.display = 'block';
        reportType = "comment";
    });

    // 모달 닫기
    document.getElementById('closeModalBtn').addEventListener('click', function() {
        document.getElementById('modal').style.display = 'none';
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', function(event) {
        if (event.target == document.getElementById('modal')) {
            document.getElementById('modal').style.display = 'none';
        }
    });

    // 폼 전송 이벤트
    document.getElementById('reportModalForm').addEventListener('submit', function(event) {
        event.preventDefault(); // 폼 기본 전송 방지

        // TODO: 백엔드 개발자는 각 reportType에 따른 실제 id 값을 가져오는 로직 구현 필요
        if(reportType === "restaurant") {
            idx = 20; // 레스토랑의 id 가져올 예정
        } else if(reportType === "review") {
            idx = 3; // 리뷰의 id 가져올 예정
        } else if(reportType === "comment") {
            idx = 2; // 댓글의 id 가져올 예정
        }

        const formData = new FormData(this);
        const data = {
            title: formData.get('reportTitle'),
            content: formData.get('reportContent'),
            id: idx
        };

        // 데이터를 서버로 전송
        fetch(`/${reportType}report/regist`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),  // 데이터를 JSON 형식으로 변환
        })
        .then(data => console.log(data))
        .catch(error => console.error('There was a problem with the fetch operation:', error));

        // 폼 전송 후 모달 닫기
        document.getElementById('modal').style.display = 'none';

        // 폼 초기화
        this.reset();
    });
}

// TODO: 백엔드 개발자는 각 탭(info, reviews, inquiries, notices)에 대한 데이터 로딩 API 구현 필요
// TODO: 백엔드 개발자는 신고 기능(restaurantreport, reviewreport, commentreport)에 대한 API 구현 필요
