document.addEventListener("DOMContentLoaded", function () {
    // TODO: 백엔드 - 페이지 로드 시 필요한 초기 데이터(레스토랑 정보, 리뷰 목록 등)를 API로 제공해야 합니다.

    // 탭과 탭 컨텐츠 요소들을 선택합니다.
    const tabs = document.querySelectorAll(".tab");
    const tabContents = document.querySelectorAll(".tab-content");
    const infoTab = document.querySelector('.tab[data-tab="info"]');
    const infoContent = document.getElementById("info");

    // 기본적으로 'info' 탭을 활성화합니다.
    if (infoTab && infoContent) {
        infoTab.classList.add("active");
        infoContent.classList.add("active");
    }

    // 각 탭에 클릭 이벤트 리스너를 추가합니다.
    tabs.forEach((tab) => {
        tab.addEventListener("click", () => {
            const tabId = tab.getAttribute("data-tab");
            // 모든 탭과 컨텐츠의 'active' 클래스를 제거합니다.
            tabs.forEach((t) => t.classList.remove("active"));
            tabContents.forEach((content) =>
                content.classList.remove("active")
            );
            // 클릭된 탭과 해당 컨텐츠를 활성화합니다.
            tab.classList.add("active");
            const activeContent = document.getElementById(tabId);
            if (activeContent) {
                activeContent.classList.add("active");
            }

            // TODO: 백엔드 - 탭 변경 시 필요한 데이터를 동적으로 로드하는 API 엔드포인트 구현 (예: 리뷰 탭 클릭 시 리뷰 데이터 로드)
        });
    });

    // 리뷰 모달 관련 요소들을 선택합니다.
    const reviewTab = document.getElementById("reviews");
    const reviewModal = document.getElementById("reviewModal");
    const modalReviewTitle = document.getElementById("modalReviewTitle");
    const modalReviewContent = document.getElementById("modalReviewContent");
    const modalReviewInfo = document.getElementById("modalReviewInfo");
    const closeModal = document.querySelector(".close");

    // 리뷰 탭에 클릭 이벤트 리스너를 추가합니다.
    reviewTab.addEventListener("click", function (e) {
        if (e.target.classList.contains("image-placeholder")) {
            const reviewItem = e.target.closest(".review-item");
            if (reviewItem) {
                // 클릭된 리뷰의 정보를 모달에 표시합니다.
                const title =
                    reviewItem.querySelector(".review-title").textContent;
                const content =
                    reviewItem.querySelector(".review-text").textContent;
                const info = reviewItem.querySelector(".review-info").innerHTML;

                modalReviewTitle.textContent = title;
                modalReviewContent.textContent = content;
                modalReviewInfo.innerHTML = info;

                // 모달을 표시합니다.
                reviewModal.style.display = "block";

                // TODO: 백엔드 - 특정 리뷰의 상세 정보를 가져오는 API 엔드포인트 구현
            }
        }
    });

    // 모달 닫기 버튼에 이벤트 리스너를 추가합니다.
    closeModal.onclick = function () {
        reviewModal.style.display = "none";
    };

    // 모달 외부 클릭 시 모달을 닫습니다.
    window.onclick = function (event) {
        if (event.target == reviewModal) {
            reviewModal.style.display = "none";
        }
    };

    // 댓글 버튼에 이벤트 리스너를 추가합니다.
    document.querySelectorAll(".comment-button").forEach((button) => {
        button.addEventListener("click", function () {
            const commentsSection =
                this.closest(".review-item").querySelector(".comments-section");
            // 댓글 섹션을 토글합니다.
            commentsSection.style.display =
                commentsSection.style.display === "none" ? "block" : "none";

            // TODO: 백엔드 - 특정 리뷰의 댓글 목록을 가져오는 API 엔드포인트 구현
            // TODO: 백엔드 - 새 댓글을 추가하는 API 엔드포인트 구현
        });
    });

    // TODO: 백엔드 - 새 리뷰를 작성하는 API 엔드포인트 구현
    // TODO: 백엔드 - 리뷰 수정/삭제 기능을 위한 API 엔드포인트 구현
    // TODO: 백엔드 - 리뷰 평점 집계 및 업데이트를 위한 로직 구현
});

// 백엔드 개발자를 위한 구현할 기능에 대한 설명:
// 1. 모달 창을 통한 문의 답변 기능 구현
// 2. 답변 내용을 서버로 전송하는 API 엔드포인트 생성
// 3. 답변 저장 후 페이지 새로고침 또는 동적 업데이트 로직 추가
document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("replyModal");
    var btns = document.getElementsByClassName("reply-inquiry");
    var span = document.getElementsByClassName("close")[0];

    for (var i = 0; i < btns.length; i++) {
        btns[i].onclick = function () {
            modal.style.display = "block";
        };
    }

    span.onclick = function () {
        modal.style.display = "none";
    };

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    };
});
