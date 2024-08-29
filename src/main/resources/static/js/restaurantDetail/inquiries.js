let loginMember = null;

RestaurantDetail.initInquiries = function() {
    checkMemberAndLoadInquiries();
    setupInquiryForm();
};

// 사용자 정보 확인 후 문의 내역 조회
function checkMemberAndLoadInquiries() {
    fetch(`/restaurant/inquiry/member`, {
        method: 'GET',
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('사용자 정보를 가져오지 못했습니다.');
        }
        return response.json();
    })
    .then(async member => {
        loginMember = member;
        await loadInquiries();
    })
    .catch(async error => {
        console.error('사용자 정보를 가져오는 중 오류 발생:', error);
        await loadInquiries();
    });
}

// 문의 내역 불러오기
async function loadInquiries() {
    const restaurantId = document.getElementById('restaurantId').value;
    const inquiryList = await getInquiryList(restaurantId);

    renderInquiry(inquiryList);
}

// 문의 내역을 서버에서 가져오기
async function getInquiryList(restaurantId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `/restaurant/${restaurantId}/user-inquiries`,
            type: 'GET',
            headers: {
                "Accept": "application/json",
            },
            success: function (inquiries) {
                resolve(inquiries);
            },
            error: function (xhr, status, error) {
                console.error("문의 내역을 가져오는 중 오류 발생: ", status, error);
                alert('문의 내역을 불러오는 데 실패했습니다.');
                reject(error);
            }
        });
    });
}

// 문의 내역 렌더링
function renderInquiry(inquiries) {
    const inquiryList = document.querySelector('.recent-inquiries');
    inquiryList.innerHTML = '';

    inquiries.forEach(inquiry => {
        const inquiryItem = document.createElement('div');
        inquiryItem.classList.add('inquiry-item');
        let deleteButtonHtml = '';

        if(loginMember === null) {
            deleteButtonHtml = '';
        } else if(inquiry.writerId === loginMember.id) {
            deleteButtonHtml =
                `<button class="delete-inquiry custom-delete-button"  data-inquiry-id="${inquiry.id}">삭제</button>`;
        }

        inquiryItem.innerHTML = `
            <div class="inquiry-header">
                <div class="inquiry-user-info">
                    <span class="inquiry-user">${inquiry.writer}</span>
                </div>
            </div>
            <div class="inquiry-content">
                <p class="inquiry-question">${inquiry.content}</p>
                <p class="inquiry-date">${new Date(inquiry.createdAt).toLocaleString()}</p>
                ${deleteButtonHtml}
            </div>
            ${inquiry.reply ? `
            <div class="inquiry-answer">
                <div class="inquiry-answer-info">
                    <span class="answer-owner">점주</span>
                </div>
                <div class="inquiry-answer-content">
                    <p class="answer-text">${inquiry.reply.content}</p>
                    <p class="answer-meta">
                        <span class="answer-date">${new Date(inquiry.reply.createdAt).toLocaleString()} 답변완료</span>
                    </p>
                </div>
            </div>
            ` : ''}
        `;
        inquiryList.appendChild(inquiryItem);
    });
    setupDeleteButtons();
}

// 문의 등록 폼 설정
function setupInquiryForm() {
    const submitInquiryButton = document.getElementById('submitInquiry');

    if (submitInquiryButton) {
        submitInquiryButton.addEventListener('click', function() {
            const inquiryContent = document.getElementById('inquiryContent').value;
            submitInquiry(inquiryContent);
        });
    }
}

// 문의 등록
function submitInquiry(inquiryContent) {
    const restaurantId = document.getElementById('restaurantId').value;
    const confirmed = confirm("문의를 제출하시겠습니까?");

    if (confirmed) {
        fetch(`/restaurant/${restaurantId}/inquiry`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: inquiryContent
            })
        })
        .then(async response => {
            if (!response.ok) {
                throw new Error('문의를 제출할 수 없습니다.');
            }
            alert("문의 등록 성공!!");
            document.getElementById('inquiryContent').value = '';
            await loadInquiries();
        })
        .catch(error => {
            console.log("문의를 등록하는 중 오류 발생", error);
            alert("문의 등록 실패!");
        });
    }
}

// 문의 삭제 버튼 설정
function setupDeleteButtons() {
    const deleteButtons = document.querySelectorAll('.delete-inquiry');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const inquiryId = this.getAttribute('data-inquiry-id');
            deleteInquiry(inquiryId);
        });
    });
}

// 문의 삭제
function deleteInquiry(inquiryId) {
    const confirmed = confirm("정말 삭제하시겠습니까?");

    if (confirmed) {
        fetch(`/restaurant/${inquiryId}/inquiry`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(async response => {
            if (!response.ok) {
                throw new Error('문의 삭제에 실패했습니다.');
            }
            alert("문의가 삭제되었습니다.");
            await loadInquiries();
        })
        .catch(error => {
            console.log("문의 삭제에 실패했습니다. ", error);
            alert("문의 삭제에 실패했습니다.");
        });
    }
}
