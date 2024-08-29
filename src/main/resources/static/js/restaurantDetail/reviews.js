document.addEventListener('DOMContentLoaded', function () {
    loadReviews();
    setupLoadMoreReviewsButton();
    setupReviewActions();
    setupReviewModal();
    setupWriteReviewButton(); // 리뷰 작성 버튼 이벤트 핸들러 등록
});

function loadReviews() {
    console.log('리뷰 로딩');
    // TODO: 백엔드 API를 호출하여 리뷰 데이터를 가져오는 로직 구현
}

function setupLoadMoreReviewsButton() {
    const loadMoreBtn = document.getElementById('loadMoreReviews');
    if (loadMoreBtn) {
        loadMoreBtn.addEventListener('click', function () {
            console.log('더 많은 리뷰 로딩');
            // TODO: 추가 리뷰를 로드하는 로직 구현
        });
    }
}

function setupReviewActions() {
    document.getElementById('reviews').addEventListener('click', function (event) {
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

// 리뷰 작성하기 버튼 설정 함수
function setupWriteReviewButton() {
    const writeReviewButton = document.querySelector('.write-review-button');
    if (writeReviewButton) {
        writeReviewButton.addEventListener('click', function (e) {
            e.preventDefault();
            const restaurantId = this.getAttribute('data-restaurant-id');
            // 영수증 인증 페이지로 이동, restaurantId를 쿼리 파라미터로 전달
            window.location.href = `/receipt/verify/${restaurantId}`;
        });
    }
}

window.loadComments = function (reviewId) {
    console.log(`댓글 로딩: 리뷰 ID ${reviewId}`);
    // TODO: 백엔드 API를 호출하여 댓글 목록을 가져오는 로직 구현
};

window.submitReviewComment = function (reviewId, content) {
    console.log(`댓글 제출: 리뷰 ID ${reviewId}, 내용: ${content}`);
    // TODO: 백엔드 API를 호출하여 새 댓글을 추가하는 로직 구현
};

function submitCommentAsync(reviewId) {

    /*
     저장 후 댓글 목록을 갱신하는 로직을 추가합니다.
     위 엘리먼트 부분을 새로 가져오는 리스트 값으로 대치해야 함.
     */
    console.log(`댓글 제출: ${reviewId}\n`);
    const commentContent = document.getElementById('comment' + reviewId).value;
    console.log(`댓글 내용: ${commentContent}`);

    $.ajax({
        type: 'POST',
        url: '/review/comment/add',
        contentType: 'application/json',  // Content-Type을 JSON으로 지정
        data: JSON.stringify({
            reviewId: reviewId,
            content: commentContent
        }),
        success: function (data) {
            console.log(data);

            // 입력창을 비운다.
            document.getElementById('comment' + reviewId).value = '';

            // 댓글 목록을 갱신하는 로직을 추가합니다.
            const commentsList = document.getElementById('commentsBox' + reviewId);
            commentsList.innerHTML = '';

            //commentbutton도 최신화
            const commentButton = document.getElementById('comment-button-' + reviewId);
            commentButton.innerText = `💬 댓글 (${data.length})`;

            data.forEach(comment => {
                const commentElement = document.createElement('div');
                commentElement.classList.add('comment');

                const commentContent = document.createElement('p');
                commentContent.innerText = comment.content;
                commentElement.appendChild(commentContent);

                const reportButton = document.createElement('button');
                reportButton.classList.add('report-button');
                reportButton.style.float = 'right';
                reportButton.innerText = '🚨';
                commentElement.appendChild(reportButton);

                const commentInfo = document.createElement('span');
                commentInfo.innerText = `${comment.createdAt} / ${comment.commenter.nickname}`;
                commentElement.appendChild(commentInfo);

                commentsList.appendChild(commentElement);
            });
        },
        error: function (error) {
            alert("먼저 로그인해주세요.");

            // href.location = "/login";
            console.error(error);

            // redirect to login page
            window.location.href = "/login";
        }
    });
}


// document.addEventListener('click', function (e) {
//     if (e.target && e.target.classList.contains('like-button')) {
//         const reviewId = e.target.id.split('-')[3];
//
//         $.ajax({
//             method: 'POST',
//             url: '/reviewlike/add',
//             data: {reviewId: reviewId},
//             success: function (data) {
//                 console.log('좋아요 수 : ' + data);
//                 e.target.innerHTML = '👍 좋아요 (' + data + ')';
//             },
//             error: function (error) {
//                 alert('로그인이 필요합니다.');
//                 console.log(error);
//             }
//         });
//     }
// });

function addReviewLike(id) {
    console.log(`좋아요 추가: 리뷰 ID ${id}`);

    $.ajax({
        method: 'POST',
        url: '/review/reviewlike/add',
        data: {reviewId: id},
        success: function (data) {
            console.log('좋아요 수 : ' + data);
            const likeButton = document.getElementById(`review-like-button-${id}`);
            likeButton.innerHTML = `👍 좋아요 (${data})`;
        },
        error: function (error) {
            alert('로그인이 필요합니다.');
            console.log(error);
        }
    });
}