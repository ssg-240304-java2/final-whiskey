// 전역 객체 생성
window.RestaurantDetail = {
    initInfo: null,
    initReviews: null,
    initInquiries: null,
    initNotices: null,
};
document.addEventListener("DOMContentLoaded", function () {
    setupTabs();
    setupModals();
    setupReports();
    setInitialTab();
    RestaurantDetail.favorite.init();
});
// 즐겨찾기 관련 함수들
window.RestaurantDetail.favorite = {
    init: function() {
        this.bookmarkButton = document.getElementById('bookmarkButton');
        this.restaurantId = this.bookmarkButton.getAttribute('data-restaurant-id');
        this.isFavorite = false;
        this.checkFavoriteStatus();
        this.addEventListeners();
    },

    checkFavoriteStatus: function() {
        fetch(`/api/favorites/check?restaurantId=${this.restaurantId}`)
            .then(response => response.json())
            .then(data => {
                this.isFavorite = data;
                this.updateButtonUI();
            })
            .catch(error => console.error('Error:', error));
    },

    addEventListeners: function() {
        this.bookmarkButton.addEventListener('click', () => this.toggleFavorite());
    },

    toggleFavorite: function() {
        const url = this.isFavorite ? '/api/favorites/remove' : '/api/favorites';
        const method = this.isFavorite ? 'POST' : 'POST';
        
        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `restaurantId=${this.restaurantId}`
        })
        .then(response => {
            if (response.ok) {
                this.isFavorite = !this.isFavorite;
                this.updateButtonUI();
            }
        })
        .catch(error => console.error('Error:', error));
    },

    updateButtonUI: function() {
        if (this.isFavorite) {
            this.bookmarkButton.innerHTML = '<i class="fas fa-bookmark"></i> 즐겨찾기 완료';
            this.bookmarkButton.classList.add('active');
        } else {
            this.bookmarkButton.innerHTML = '<i class="far fa-bookmark"></i> 즐겨찾기';
            this.bookmarkButton.classList.remove('active');
        }
    }
};
function setupReports() {
    let idx;

    const reportModal = document.getElementById("reportModal");
    const closeBtn = reportModal.querySelector(".close-btn");
    const form = document.getElementById("reportForm");

    // 식당신고 버튼 클릭 시 로그인 여부 확인 후 모달 열기
    document.getElementById("restaurantReport").addEventListener("click", function () {

        fetch('/report/valid', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
            .then(response => response.json()) // Expecting a JSON response from the server
            .then(data => {
                if (data.success) {
                    // If the user is logged in, show the modal
                    document.getElementById("modal").style.display = "block";
                } else {
                    // If the user is not logged in, show an alert
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error("Request failed", error);
            })
    });

    // 리뷰 및 댓글 신고 버튼 클릭 시 로그인 여부 확인 후 모달 열기
    document.addEventListener("click", function(e) {

        if (e.target.classList.contains("review-report") || e.target.classList.contains("comment-report")) {
            fetch('/report/valid', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            })
                .then(response => response.json()) // Expecting a JSON response from the server
                .then(data => {
                    if (data.success) {
                        // 버튼의 id 값을 가져옴 (예: "commentReport123", "reviewReport45")
                        const fullId = e.target.id;
                        console.log(fullId)

                        // 정규 표현식을 사용하여 "reviewReport" 또는 "commentReport" 접두사 제거 후 숫자만 추출
                        const id = fullId.replace(/^(reviewReport|commentReport)/, "");
                        console.log(id)

                        // 타입을 결정 (commentReport 또는 reviewReport 접두사에 따라)
                        const type = fullId.startsWith("reviewReport") ? "review" : "comment";
                        console.log(type)
                        // 모달 열기 함수 호출
                        openReportModal(type, id);

                    } else {
                        // If the user is not logged in, show an alert
                        alert(data.message);
                        console.log("알럿 밑 부분")
                    }
                })
                .catch(error => {
                    console.error("Request failed", error);
                });
        }
    });

    // 모달 닫기
    document
        .getElementById("closeModalBtn")
        .addEventListener("click", function () {
            document.getElementById("modal").style.display = "none";
        });

    // 모달 외부 클릭 시 닫기
    window.addEventListener("click", function (event) {
        if (event.target == document.getElementById("modal")) {
            document.getElementById("modal").style.display = "none";
        }
    });

    // 폼 전송 이벤트
    document
        .getElementById("reportModalForm")
        .addEventListener("submit", function (event) {
            event.preventDefault(); // 폼 기본 전송 방지

            idx = document.getElementById("restaurantId").value;
            console.log(idx);

            const formData = new FormData(this);
            const data = {
                title: formData.get("reportTitle"),
                content: formData.get("reportContent"),
                id: idx,
            };

            // 데이터를 서버로 전송
            fetch(`/restaurantreport/regist`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            })
                .then((response) => {
                    if (response.ok) {
                        alert("신고가 접수되었습니다.");
                    } else {
                        alert("신고 접수에 실패했습니다.");
                    }

                    // 폼 전송 후 모달 닫기
                    document.getElementById("modal").style.display = "none";

                    // 폼 초기화
                    this.reset();
                })
                .catch((error) => {
                    console.error(
                        "There was a problem with the fetch operation:",
                        error
                    );
                });
        });

    // 리뷰,댓글 신고 모달 submit 클릭 이벤트
    document.getElementById("reportForm").addEventListener("submit", function submitReport() {
        // 폼에서 타입과 ID 가져오기
        const type = document.getElementById("reportType").value;
        const targetId = document.getElementById("reportTargetId").value;
        const reportTitile = document.getElementById("reportTitle").value;
        const reportContent = document.getElementById("reportContent").value;

        // 타입에 따라 URL 설정
        const url = type === "review" ? "/reviewreport/regist" : "/reviewcommentreport/regist";

        // JSON 데이터를 생성
        const data = {
            title: reportTitile,  // 여기에 적절한 제목을 설정합니다.
            content: reportContent,  // 여기에 적절한 내용 입력
            reportedAt: new Date().toISOString(), // ISO 형식의 현재 시간
            isChecked: false,  // 초기 상태로 설정
            isVisible: true,  // 초기 상태로 설정
            id: targetId
        };

        // 신고 데이터 전송
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'  // JSON 형식으로 보낸다는 것을 서버에 알림
            },
            body: JSON.stringify(data)  // 데이터를 JSON 문자열로 변환하여 전송
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
            })
            .then(data => {
                alert("신고가 접수되었습니다.");
                document.getElementById("reportModal").style.display = "none";
                document.getElementById("reportForm").reset();
            })
            .catch(error => {
                console.error('Error:', error);
                alert("신고 접수 중 오류가 발생했습니다.");
            });
    });
}

function openReportModal(type, id) {
    const reportModal = document.getElementById("reportModal");
    if (id) {
        document.getElementById("reportType").value = type;
        document.getElementById("reportTargetId").value = id; // 모달에 ID 설정
        reportModal.style.display = "block";

    } else {
        console.error("No target ID found for reporting");
    }
}


function setupTabs() {
    const tabs = document.querySelectorAll(".tab");
    const tabContents = document.querySelectorAll(".tab-content");
    const restaurantId = document.getElementById("restaurantId").value;

    tabs.forEach((tab) => {
        tab.addEventListener("click", () => {
            const tabId = tab.getAttribute("data-tab");
            activateTab(tab, tabId);
            // URL 변경 (브라우저 히스토리에 추가)
            history.pushState(null, "", `/restaurant/${restaurantId}/${tabId}`);
        });
    });

    // 탭 활성화 함수
    function activateTab(clickedTab, tabId) {
        // 모든 탭과 컨텐츠의 active 클래스 제거
        tabs.forEach((t) => t.classList.remove("active"));
        tabContents.forEach((content) => content.classList.remove("active"));

        // 클릭된 탭과 해당 컨텐츠에 active 클래스 추가
        clickedTab.classList.add("active");
        const activeContent = document.getElementById(tabId);
        if (activeContent) {
            activeContent.classList.add("active");
            // 탭 전환 시 해당 탭의 초기화 함수 호출 (동적으로 함수 이름 생성)
            if (
                RestaurantDetail[
                    `init${tabId.charAt(0).toUpperCase() + tabId.slice(1)}`
                ]
            ) {
                RestaurantDetail[
                    `init${tabId.charAt(0).toUpperCase() + tabId.slice(1)}`
                ]();
            }
        }

        // TODO: 백엔드 API 호출하여 탭 내용 로드
        loadTabContent(tabId);
    }

    // URL 변경 시 탭 활성화 (브라우저 뒤로가기/앞으로가기 대응)
    window.addEventListener("popstate", setInitialTab);
}

// 초기 탭 설정 함수
function setInitialTab() {
    const path = window.location.pathname;
    const pathParts = path.split("/");
    const tabId = pathParts[pathParts.length - 1] || "info"; // URL에서 탭 ID 추출, 없으면 'info'
    const tab = document.querySelector(`.tab[data-tab="${tabId}"]`);
    if (tab) {
        tab.click(); // 해당 탭 클릭 이벤트 발생
    } else {
        // 기본 탭(info) 활성화
        const defaultTab = document.querySelector('.tab[data-tab="info"]');
        if (defaultTab) {
            defaultTab.click();
        }
    }
}

// TODO: 탭 내용을 동적으로 로드하는 함수 구현
function loadTabContent(tabId) {
    const restaurantId = document.getElementById("restaurantId").value;
    // TODO: 백엔드 API 호출하여 탭 내용 로드
    // 예: fetch(`/api/restaurant/${restaurantId}/${tabId}`)
    //     .then(response => response.json())
    //     .then(data => updateTabContent(tabId, data));
}

// TODO: 탭 내용을 업데이트하는 함수 구현
function updateTabContent(tabId, data) {
    const tabContent = document.getElementById(tabId);
    // TODO: data를 사용하여 tabContent의 내용 업데이트
}

// 모달 설정 함수
function setupModals() {
    // 모달 설정을 전역 객체에 추가
    window.RestaurantDetail.setupModal = function (modalId) {
        const modal = document.getElementById(modalId);
        const closeBtn = modal.querySelector(".close");

        // 닫기 버튼 클릭 시 모달 숨김
        closeBtn.onclick = function () {
            modal.style.display = "none";
        };

        // 모달 외부 클릭 시 모달 숨김
        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        };
    };

    // 공지사항 모달 설정
    RestaurantDetail.setupModal("noticeModal");

    // 답변 모달 설정
    RestaurantDetail.setupModal("replyModal");
}

// TODO: 백엔드 개발자는 각 탭(info, reviews, inquiries, notices)에 대한 데이터 로딩 API 구현 필요
// TODO: 백엔드 개발자는 신고 기능(restaurantreport, reviewreport, commentreport)에 대한 API 구현 필요
