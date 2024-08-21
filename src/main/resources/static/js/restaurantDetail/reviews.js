document.addEventListener('DOMContentLoaded', function() {
    loadReviews();
    setupLoadMoreReviewsButton();
    setupReviewActions();
    setupReviewModal();
});

function loadReviews() {
    console.log('리뷰 로딩');
    // TODO: 백엔드 API를 호출하여 리뷰 데이터를 가져오는 로직 구현
}

function setupLoadMoreReviewsButton() {
    const loadMoreBtn = document.getElementById('loadMoreReviews');
    if (loadMoreBtn) {
        loadMoreBtn.addEventListener('click', function() {
            console.log('더 많은 리뷰 로딩');
            // TODO: 추가 리뷰를 로드하는 로직 구현
        });
    }
}

function setupReviewActions() {
    document.getElementById('reviews').addEventListener('click', function(event) {
        if (event.target.classList.contains('comment-button')) {
            const commentsSection = event.target.closest('.review-item').querySelector('.comments-section');
            if (commentsSection.style.display === 'none' || commentsSection.style.display === '') {
                commentsSection.style.display = 'block';
                const reviewId = event.target.closest('.review-item').dataset.reviewId;
                loadComments(reviewId);
            } else {
                commentsSection.style.display = 'none';
            }
        }
    });
}

function setupReviewModal() {
    const reviewTab = document.getElementById("reviews");
    const reviewModal = document.getElementById("reviewModal");
    const modalReviewTitle = document.getElementById("modalReviewTitle");
    const modalReviewContent = document.getElementById("modalReviewContent");
    const modalReviewInfo = document.getElementById("modalReviewInfo");

    reviewTab.addEventListener("click", function (e) {
        if (e.target.closest(".image-placeholder")) {
            e.preventDefault();
            const reviewItem = e.target.closest(".review-item");
            if (reviewItem) {
                const title = reviewItem.querySelector(".review-title").textContent;
                const content = reviewItem.querySelector(".review-text").textContent;
                const info = reviewItem.querySelector(".review-info").innerHTML;

                modalReviewTitle.textContent = title;
                modalReviewContent.textContent = content;
                modalReviewInfo.innerHTML = info;

                reviewModal.style.display = "block";
            }
        }
    });

    // 모달 설정
    window.RestaurantDetail.setupModal("reviewModal");
}

window.loadComments = function(reviewId) {
    console.log(`댓글 로딩: 리뷰 ID ${reviewId}`);
    // TODO: 백엔드 API를 호출하여 댓글 목록을 가져오는 로직 구현
};

window.submitComment = function(reviewId, content) {
    console.log(`댓글 제출: 리뷰 ID ${reviewId}, 내용: ${content}`);
    // TODO: 백엔드 API를 호출하여 새 댓글을 추가하는 로직 구현
};