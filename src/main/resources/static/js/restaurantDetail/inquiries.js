let loginMember = null;

RestaurantDetail.initInquiries = function() {
    loadCurrentMemberAndInquiries();
    setupInquiryForm();
    // setupInquirySort();
};

function loadCurrentMemberAndInquiries() {
    fetch(`/restaurant/inquiry/member`, {
        method: 'GET',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('멤버 정보를 가져오지 못했습니다.');
            }
            return response.json();
        })
        .then(async member => {
            loginMember = member;
            console.log('로그인한 사용자:', loginMember);
            await loadInquiries();
        })
        .catch(async error => {
            console.error('멤버 정보를 가져오는 중 오류 발생:', error);
            await loadInquiries();
        });
}

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
    const restaurantId = document.getElementById('restaurantId').value;
    const confirmed = confirm("문의를 제출하시겠습니까?");

    if (confirmed) {
        fetch(`/restaurant/${restaurantId}/inquiry`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: content
            })
        })
            .then(async response => {
                if (!response.ok) {
                    throw new Error('문의를 제출할 수 없습니다.');
                }
                alert("문의 제출 성공!!");

                document.getElementById('inquiryContent').value = '';

                await loadInquiries();
            })
            .catch(error => {
                alert("문의 제출 실패!");
                console.log("문의 제출 실패!", error);
            });
    }
}

// function sortInquiries(sortType) {
//     console.log(`문의 정렬: ${sortType}`);
//     // TODO: 백엔드 API를 호출하여 정렬된 문의 목록을 가져오는 로직 구현
// }

async function loadInquiries() {
    const restaurantId = document.getElementById('restaurantId').value;

    let inquiryList = await getInquiryList(restaurantId);
    console.log('inquiryList in async loadInquiries:', inquiryList);

    renderInquiry(inquiryList);
}

async function getInquiryList(restaurantId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: `/restaurant/${restaurantId}/user-inquiries`,
            headers: {
                "Accept": "application/json",  // 서버에 JSON 형식의 응답을 기대한다고 알림
            },
            success: function (inquiries) {
                resolve(inquiries);
            },
            error: function (xhr, status, error) {
                console.error("AJAX 요청 실패:", status, error); // 오류 시 콘솔 출력
                alert('문의 내역을 불러오는 데 실패했습니다.');
                reject(error);
            }
        });
    });
}

function renderInquiry(data) {
    const inquiryList = document.querySelector('.recent-inquiries');

    inquiryList.innerHTML = '';

    console.log('data in renderInquiry:', data);

    data.forEach(inquiry => {
        const inquiryItem = document.createElement('div');
        inquiryItem.classList.add('inquiry-item');
        let deleteButtonHtml = '';

        // loginMember의 null 체크 루틴 추가
        if(loginMember === null) {
            deleteButtonHtml = '';
        } else if(inquiry.writerId === loginMember.id) {
            deleteButtonHtml =
                `<button class="delete-inquiry" data-inquiry-id="${inquiry.id}">삭제</button>`;
        }

        inquiryItem.innerHTML = `
            <div class="inquiry-header">
                <div class="inquiry-user-info">
                    <span class="inquiry-user">${inquiry.writer}</span>
                </div>
            </div>
            <div class="inquiry-content">
                <p class="inquiry-question">${inquiry.content}</p>
                <p class="inquiry-date">${new Date(inquiry.createdAt).toLocaleString()} 질문</p>
                ${deleteButtonHtml}
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
            .then(async response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                console.log("문의 삭제 성공!!");
                alert("문의가 삭제되었습니다.");
                await loadInquiries();
            })
            .catch(error => {
                alert("Error!");
                console.log("ERROR: ", error);
            });
    }
}
