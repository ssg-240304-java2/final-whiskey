RestaurantDetail.initInquiries = function() {
    setupInquiryForm();
    // setupInquirySort();
    loadInquiries();
    // setupReplyButtons();
};

// function setupReplyButtons() {
//     const replyButtons = document.querySelectorAll('.reply-inquiry');
//     replyButtons.forEach(button => {
//         button.addEventListener('click', function() {
//             const inquiryId = this.getAttribute('data-inquiry-id');
//             openReplyModal(inquiryId);
//         });
//     });
// }

// function openReplyModal(inquiryId) {
//     const modal = document.getElementById('replyModal');
//     if (modal) {
//         modal.style.display = 'block';
//         const submitReplyBtn = document.getElementById('submitReply');
//         submitReplyBtn.onclick = function() {
//             const replyText = document.getElementById('replyText').value;
//             submitReply(inquiryId, replyText);
//             modal.style.display = 'none';
//         };
//     }
// }

// function submitReply(inquiryId, replyText) {
//     console.log(`답변 제출: 문의 ID ${inquiryId}, 내용: ${replyText}`);
//     // TODO: 백엔드 API를 호출하여 답변을 제출하는 로직 구현
// }

function setupInquiryForm() {
    const submitInquiryBtn = document.getElementById('submitInquiry');

    if (submitInquiryBtn) {
        submitInquiryBtn.addEventListener('click', function() {
            const inquiryContent = document.getElementById('inquiryContent').value;
            submitInquiry(inquiryContent);
        });
    }
}

// function setupInquirySort() {
//     const inquirySortSelect = document.getElementById('inquirySort');
//     if (inquirySortSelect) {
//         inquirySortSelect.addEventListener('change', function() {
//             sortInquiries(this.value);
//         });
//     }
// }

function submitInquiry(content) {
    console.log(`문의 제출: ${content}`);
    // TODO: 백엔드 API를 호출하여 새 문의를 추가하는 로직 구현

    const restaurantId = document.getElementById('restaurantId').value;

    fetch(`/restaurant/${restaurantId}/inquiry`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            content: content
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log("문의 제출 성공!!");

            document.getElementById('inquiryContent').value = '';

            loadInquiries();
        })
        .catch(error => {
            alert("Error!");
            console.log("ERROR: ", error);
        });
}

// function sortInquiries(sortType) {
//     console.log(`문의 정렬: ${sortType}`);
//     // TODO: 백엔드 API를 호출하여 정렬된 문의 목록을 가져오는 로직 구현
// }

function loadInquiries() {
    console.log('문의 목록 로딩');
    const restaurantId = document.getElementById('restaurantId').value;
    console.log("음식점 확인!!", restaurantId);

    fetch(`/restaurant/${restaurantId}/inquiry`, {
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
                 <button type="button" class="btn btn-danger delete-inquiry" data-inquiry-id="${inquiry.id}">삭제</button>                
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

    setupDeleteButtons();
}

function setupDeleteButtons() {
    const deleteButtons = document.querySelectorAll('.delete-inquiry');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const inquiryId = this.getAttribute('data-inquiry-id');
            deleteInquiry(inquiryId);
        });
    });
}

function deleteInquiry(inquiryId) {
    const confirmed = confirm("정말 삭제하시겠습니까?");
    console.log(`문의 삭제: ${inquiryId}`);
    if (confirmed) {
        fetch(`/restaurant/${inquiryId}/inquiry`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log("문의 삭제 성공!!");
            alert("문의가 삭제되었습니다.");
            loadInquiries();
        })
        .catch(error => {
            alert("Error!");
            console.log("ERROR: ", error);
        });
    }
}
// window.loadComments = function(reviewId) {
//     console.log(`댓글 로딩: 리뷰 ID ${reviewId}`);
//     // TODO: 백엔드 API를 호출하여 댓글 목록을 가져오는 로직 구현
// };
//
// window.submitComment = function(reviewId, content) {
//     console.log(`댓글 제출: 리뷰 ID ${reviewId}, 내용: ${content}`);
//     // TODO: 백엔드 API를 호출하여 새 댓글을 추가하는 로직 구현
// }