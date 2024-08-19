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
});

function setupTabs() {
    const tabs = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const tabId = tab.getAttribute('data-tab');
            activateTab(tab, tabId);
        });
    });

    function activateTab(clickedTab, tabId) {
        tabs.forEach(t => t.classList.remove('active'));
        tabContents.forEach(content => content.classList.remove('active'));

        clickedTab.classList.add('active');
        const activeContent = document.getElementById(tabId);
        if (activeContent) {
            activeContent.classList.add('active');
            // 탭 전환 시 해당 탭의 초기화 함수 호출
            if (RestaurantDetail[`init${tabId.charAt(0).toUpperCase() + tabId.slice(1)}`]) {
                RestaurantDetail[`init${tabId.charAt(0).toUpperCase() + tabId.slice(1)}`]();
            }
        }
    }

    // 기본 탭 활성화
    const defaultTab = document.querySelector('.tab[data-tab="info"]');
    if (defaultTab) {
        defaultTab.click();
    }
}

function setupModals() {
    // 모달 설정을 전역 객체에 추가
    window.RestaurantDetail.setupModal = function(modalId) {
        const modal = document.getElementById(modalId);
        const closeBtn = modal.querySelector('.close');

        closeBtn.onclick = function() {
            modal.style.display = 'none';
        };

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

function setupReports() {
    let reportType;
    let idx;

    // 모달 열기
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
        }) .then(response => {
            if (response.ok) {  // 응답 상태 코드가 200-299일 경우
                alert('신고가 접수되었습니다.');
            } else {
                alert('신고 접수에 실패했습니다.');
            }
        }) .catch(error => alert(error('There was a problem with the fetch operation:', error)));
        // 폼 전송 후 모달 닫기
        document.getElementById('modal').style.display = 'none';

        // 폼 초기화
        this.reset();
    });
}
