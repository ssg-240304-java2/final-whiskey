/* 식당 신고 버튼 클릭 시 신고 창 모달과 값 넘겨주는 js */
document.addEventListener('DOMContentLoaded', function() {

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
        }) .then(data => console.log(data))
            .catch(error => console.error('There was a problem with the fetch operation:', error));;

        // 폼 전송 후 모달 닫기
        document.getElementById('modal').style.display = 'none';

        // 폼 초기화
        this.reset();
    });
});
/* 식당 신고 버튼 클릭 시 신고 창 모달과 값 넘겨주는 js */


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
            const commentsSection = this.closest(".review-item").querySelector(".comments-section");
            if (commentsSection.style.display === "none") {
                commentsSection.style.display = "block";
                loadComments(this.closest(".review-item").dataset.reviewId);
            } else {
                commentsSection.style.display = "none";
            }
        });
    });

    // 댓글 작성 버튼에 이벤트 리스너를 추가합니다.
    document.querySelectorAll(".submit-comment").forEach((button) => {
        button.addEventListener("click", function () {
            const reviewItem = this.closest(".review-item");
            const commentInput = reviewItem.querySelector(".comment-input");
            const reviewId = reviewItem.dataset.reviewId;
            submitComment(reviewId, commentInput.value);
        });
    });

    // TODO: 백엔드 - 새 리뷰를 작성하는 API 엔드포인트 구현
    // TODO: 백엔드 - 리뷰 수정/삭제 기능을 위한 API 엔드포인트 구현
    // TODO: 백엔드 - 리뷰 평점 집계 및 업데이트를 위한 로직 구현

    // 공지사항 탭 클릭 이벤트
    const noticesTab = document.querySelector('.tab[data-tab="notices"]');
    const noticesContent = document.getElementById("notices");

    noticesTab.addEventListener("click", function() {
        // TODO: 백엔드 API를 통해 공지사항 데이터를 가져오는 로직 구현
        // 예시:
        // fetchNotices().then(notices => {
        //     renderNotices(notices);
        // });
    });

    function renderNotices(notices) {
        const noticesScroll = document.querySelector('.notices-scroll');
        noticesScroll.innerHTML = notices.map(notice => `
            <div class="notice-card">
                <h3>${notice.title}</h3>
                <p>${notice.content}</p>
                <span class="notice-date">${notice.date}</span>
            </div>
        `).join('');
    }

    const writeNoticeBtn = document.getElementById('writeNoticeBtn');

    // TODO: 백엔드에서 현재 사용자의 권한을 확인하는 API 호출
    function checkOwnerPermission() {
        // 임시로 true를 반환. 실제로는 백엔드 API를 통해 권한을 확인해야 함
        return true;
    }

    if (checkOwnerPermission()) {
        writeNoticeBtn.style.display = 'block';
    }

    writeNoticeBtn.addEventListener('click', function() {
        // TODO: 공지사항 작성 모달 또는 페이지로 이동하는 로직 구현
        console.log('공지사항 작성 버튼 클릭');
    });

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

function loadComments(reviewId) {
    // TODO: 백엔드 API를 호출하여 댓글 목록을 가져옵니다.
    // 예시 코드:
    fetch(`/api/reviews/${reviewId}/comments`)
        .then(response => response.json())
        .then(comments => {
            const commentsList = document.querySelector(`[data-review-id="${reviewId}"] .comments-list`);
            commentsList.innerHTML = comments.map(comment => `
                <div class="comment">
                    <p>${comment.content}</p>
                    <span>${comment.author} - ${comment.date}</span>
                </div>
            `).join('');
        });
    // 백엔드 개발자: 리뷰 ID에 해당하는 댓글 목록을 반환하는 API 엔드포인트를 구현해야 합니다.
    // 반환된 댓글 데이터는 content, author, date 필드를 포함해야 합니다.
}

function submitComment(reviewId, content) {
    // TODO: 백엔드 API를 호출하여 새 댓글을 추가합니다.
    // 예시 코드:
    fetch(`/api/reviews/${reviewId}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({content: content}),
    })
        .then(response => response.json())
        .then(newComment => {
            const commentsList = document.querySelector(`[data-review-id="${reviewId}"] .comments-list`);
            const newCommentElement = document.createElement('div');
            newCommentElement.className = 'comment';
            newCommentElement.innerHTML = `
            <p>${newComment.content}</p>
            <span>${newComment.author} - ${newComment.date}</span>
        `;
            commentsList.appendChild(newCommentElement);
        });
    // 백엔드 개발자: 새 댓글을 저장하는 API 엔드포인트를 구현해야 합니다.
    // 요청 본문에서 content를 받아 새 댓글을 생성하고, 생성된 댓글 정보를 응답으로 반환해야 합니다.

}