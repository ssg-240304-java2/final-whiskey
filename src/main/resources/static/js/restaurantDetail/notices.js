RestaurantDetail.initNotices = function () {
    checkOwnerPermission();
    setupWriteNoticeButton();
    loadNotices();
};

function checkOwnerPermission() {
    // TODO: 백엔드 API를 호출하여 현재 사용자의 권한을 확인하는 로직 구현
    console.log('소유자 권한 확인');
    const isOwner = true; // 임시로 true 설정
    const writeNoticeBtn = document.getElementById('writeNoticeBtn');
    if (writeNoticeBtn) {
        writeNoticeBtn.style.display = isOwner ? 'block' : 'none';
    }
}

function setupWriteNoticeButton() {
    const writeNoticeBtn = document.getElementById('writeNoticeBtn');
    if (writeNoticeBtn) {
        writeNoticeBtn.addEventListener('click', function () {
            console.log('공지사항 작성 버튼 클릭');
            const modal = document.getElementById('noticeModal');
            if (modal) {
                // 모달 내용 초기화
                const form = modal.querySelector('#noticeForm');
                if (form) {
                    form.reset();
                }
                // 공지사항 폼만 표시
                const noticeForm = modal.querySelector('.notice-form');
                const replyForm = modal.querySelector('.reply-form');
                if (noticeForm) noticeForm.style.display = 'block';
                if (replyForm) replyForm.style.display = 'none';

                modal.style.display = 'block';
            }
        });
    }
}

function loadNotices() {
    console.log('공지사항 로딩');
    // TODO: 백엔드 API를 호출하여 공지사항 목록을 가져오는 로직 구현
    const restaurantId = document.getElementById('restaurantId').value;

    fetch(`/restaurant/${restaurantId}/notice`)
        .then(response => response.json())
        .then(data => {
            const noticeContainer = document.querySelector('.notices-scroll');
            noticeContainer.innerHTML = ''; // 초기화
            console.log('공지사항 데이터', data);

            data.forEach(notice => {
                const noticeCard = document.createElement('div');
                noticeCard.classList.add('notice-card');
                noticeCard.innerHTML = `
                    <h3>${notice.title}</h3>
                    <p>${notice.content}</p>
                    <span class="notice-date">${new Date(notice.createdAt).toLocaleDateString()}</span>
                `;
                noticeContainer.appendChild(noticeCard);
            });
            console.log('공지사항 html', noticeContainer);
        })
        .catch(error => console.error('공지사항 오류', error));
}

// 공지사항 작성 폼 제출 처리
document.addEventListener('DOMContentLoaded', function () {
    // 이벤트 위임을 통해 동적으로 추가된 모달 요소에 이벤트 연결
    document.addEventListener('click', function (event) {
        if (event.target && event.target.id === 'save-notice') {
            event.preventDefault();

            // 공지사항 작성 폼 제출 (restaurantId, title, content)
            const restaurantId = document.getElementById('restaurantId').value;
            const title = document.getElementById('title').value;
            const content = document.getElementById('content').value;

            if (!title) {
                alert('제목을 입력하세요.');
                return;
            }

            if (!content) {
                alert('내용을 입력하세요.');
                return;
            }

            // 공지사항 작성 요청
            fetch(`/restaurant/${restaurantId}/notice`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ title, content }),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Network response was not ok");
                    }
                    // 성공적으로 처리되었을 경우 처리
                    console.log('공지사항 작성 성공');
                    closeNoticeModal(); // 모달 닫기
                    loadNotices(); // 공지사항 목록 다시 로드
                })
                .catch(error => {
                    console.error('공지사항 작성 오류', error);
                    alert('공지사항 작성에 실패했습니다.');
                });
        }
    });
});



// 모달 닫기 함수
function closeNoticeModal() {
    const modal = document.getElementById('noticeModal');
    if (modal) {
        modal.style.display = 'none';
    }
    // 폼 초기화
    document.getElementById('title').value = '';
    document.getElementById('content').value = '';
}