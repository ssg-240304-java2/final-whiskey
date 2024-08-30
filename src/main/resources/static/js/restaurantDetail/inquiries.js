let loginMember = null;

$(document).ready(function () {
    checkMemberAndLoadInquiries();
    setupInquiryForm();

    $(document).on('click', '.prev-page', clickInquiryPost);
    $(document).on('click', '.next-page', clickInquiryForward);
});

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
        await loadInquiries(1);
    })
    .catch(async error => {
        console.error('사용자 정보를 가져오는 중 오류 발생:', error);
        await loadInquiries(1);
    });
}

// 문의 내역 불러오기
async function loadInquiries(pageNumber) {
    const restaurantId = document.getElementById('restaurantId').value;
    const inquiryList = await getInquiryList(restaurantId, pageNumber);

    renderInquiry(inquiryList, parseInt(pageNumber));
}

// 문의 내역을 서버에서 가져오기
async function getInquiryList(restaurantId, pageNumber) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `/restaurant/${restaurantId}/user-inquiries?pageNumber=${pageNumber}&pageSize=5`,
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
function renderInquiry(inquiries, pageNumber) {
    const inquiryList = document.querySelector('.recent-inquiries');
    inquiryList.innerHTML = '';

    inquiries.content.forEach(inquiry => {
        const inquiryItem = document.createElement('div');
        inquiryItem.classList.add('inquiry-item');
        let deleteButtonHtml = '';

        if(loginMember === null) {
            deleteButtonHtml = '';
        } else if(inquiry.writer.id === loginMember.id) {
            deleteButtonHtml =
                `<button class="delete-inquiry custom-delete-button"  data-inquiry-id="${inquiry.id}">삭제</button>`;
        }

        inquiryItem.innerHTML = `
            <div class="inquiry-header">
                <div class="inquiry-user-info">
                    <span class="inquiry-user">${inquiry.writer.name}</span>
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

    renderPageNumber(inquiries.totalPages, pageNumber);
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
            await loadInquiries(1);
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
            await loadInquiries(1);
        })
        .catch(error => {
            console.log("문의 삭제에 실패했습니다. ", error);
            alert("문의 삭제에 실패했습니다.");
        });
    }
}

// 페이지네이션 렌더링
function renderPageNumber(totalPages, pageNumber) {
    const pageList = $('#pagination-inquiry');
    pageList.empty();

    const postButton = `<button class="prev-page ${pageNumber === 1 ? 'disabled' : ''}" id="post-inquiry"><i class="fas fa-chevron-left"></i> 이전</button>`;
    pageList.append(postButton);

    for (let index = 1; index <= totalPages; index++) {
        pageList.append(`<span class="current-page ${pageNumber === index ? 'active' : ''}" id="${index}" onclick="clickInquiryPage(${index})">${index}</span>`);
    }

    const forwardButton = `<button class="next-page" data-totalPage="${totalPages}">다음 <i class="fas fa-chevron-right"></i></button>`;
    pageList.append(forwardButton);
}

// 페이지네이션 클릭 이벤트
function clickInquiryPage(pageNumber) {
    loadInquiries(pageNumber);
}

// 이전 페이지로 이동
function clickInquiryPost() {
    const currentPageNumber = getCurrentPage();

    if (currentPageNumber > 1) {
        loadInquiries(currentPageNumber - 1);
    }
}

// 다음 페이지로 이동
function clickInquiryForward() {
    const currentPageNumber = getCurrentPage();
    const totalPageNumber = parseInt($(this).attr('data-totalPage'));

    if (currentPageNumber < totalPageNumber) {
        loadInquiries(currentPageNumber + 1);
    }
}

// 현재 페이지 번호 반환
function getCurrentPage() {
    return parseInt($('.current-page.active').attr('id'));
}