RestaurantDetail.initInquiries = function() {
    setupInquiryForm();
    setupInquirySort();
    loadInquiries();
    setupReplyButtons();
};

function setupReplyButtons() {
    const replyButtons = document.querySelectorAll('.reply-inquiry');
    replyButtons.forEach(button => {
        button.addEventListener('click', function() {
            const inquiryId = this.getAttribute('data-inquiry-id');
            openReplyModal(inquiryId);
        });
    });
}

function openReplyModal(inquiryId) {
    const modal = document.getElementById('replyModal');
    if (modal) {
        modal.style.display = 'block';
        const submitReplyBtn = document.getElementById('submitReply');
        submitReplyBtn.onclick = function() {
            const replyText = document.getElementById('replyText').value;
            submitReply(inquiryId, replyText);
            modal.style.display = 'none';
        };
    }
}

function submitReply(inquiryId, replyText) {
    console.log(`답변 제출: 문의 ID ${inquiryId}, 내용: ${replyText}`);
    // TODO: 백엔드 API를 호출하여 답변을 제출하는 로직 구현
}
function setupInquiryForm() {
    const submitInquiryBtn = document.getElementById('submitInquiry');
    if (submitInquiryBtn) {
        submitInquiryBtn.addEventListener('click', function() {
            const inquiryText = document.getElementById('inquiryText').value;
            submitInquiry(inquiryText);
        });
    }
}

function setupInquirySort() {
    const inquirySortSelect = document.getElementById('inquirySort');
    if (inquirySortSelect) {
        inquirySortSelect.addEventListener('change', function() {
            sortInquiries(this.value);
        });
    }
}

function submitInquiry(text) {
    console.log(`문의 제출: ${text}`);
    // TODO: 백엔드 API를 호출하여 새 문의를 추가하는 로직 구현
}

function sortInquiries(sortType) {
    console.log(`문의 정렬: ${sortType}`);
    // TODO: 백엔드 API를 호출하여 정렬된 문의 목록을 가져오는 로직 구현
}

function loadInquiries() {
    console.log('문의 목록 로딩');
    const restaurantId = document.getElementById('restaurantId').value;
    console.log("음식점 확인!!", restaurantId);

    fetch(`/restaurant/${restaurantId}/inquiries`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            renderInquiry(data);
        })
        .catch(error => {
            alert("Error!");
            console.log("ERROR: ", error);
        });
}

function renderInquiry(data) {
    const inquiryList = document.querySelector('.recent-inquiries');

    inquiryList.innerHTML = '';

    data.forEach(inquiry => {
        const inquiryItem = document.createElement('div');
        inquiryItem.classList.add('inquiry-item');

        inquiryItem.innerHTML = `
            <div class="inquiry-header">
            <!-- 이미지 있으면 사용 예정-->
<!--                <img src="https://via.placeholder.com/40" alt="사용자 프로필" class="user-avatar">-->
                <div class="inquiry-user-info">
                    <span class="inquiry-user">${inquiry.writer}</span>
                </div>
            </div>
            <div class="inquiry-content">
                <p class="inquiry-question">${inquiry.content}</p>
                <p class="inquiry-date">${new Date(inquiry.createdAt).toLocaleString()} 질문</p>
            </div>
            ${inquiry.reply ? `
            <div class="inquiry-answer">
                <p class="answer-text">${inquiry.reply.content}</p>
                <p class="answer-meta">
                    <span class="answer-date">${new Date(inquiry.reply.createdAt).toLocaleString()} 답변</span>
                </p>
            </div>
            ` : ''}
        `;
        inquiryList.appendChild(inquiryItem);
    });
}

window.loadComments = function(reviewId) {
    console.log(`댓글 로딩: 리뷰 ID ${reviewId}`);
    // TODO: 백엔드 API를 호출하여 댓글 목록을 가져오는 로직 구현
};

window.submitComment = function(reviewId, content) {
    console.log(`댓글 제출: 리뷰 ID ${reviewId}, 내용: ${content}`);
    // TODO: 백엔드 API를 호출하여 새 댓글을 추가하는 로직 구현
};
