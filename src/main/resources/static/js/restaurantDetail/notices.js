RestaurantDetail.initNotices = function() {
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
        writeNoticeBtn.addEventListener('click', function() {
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
}

// 모달 닫기 함수
function closeNoticeModal() {
    const modal = document.getElementById('noticeModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

// 공지사항 작성 폼 제출 처리
document.addEventListener('DOMContentLoaded', function() {
    const noticeForm = document.getElementById('noticeForm');
    if (noticeForm) {
        noticeForm.addEventListener('submit', function(event) {
            event.preventDefault();
            // TODO: 폼 데이터를 백엔드로 전송하는 로직 구현
            console.log('공지사항 작성 폼 제출');
            closeNoticeModal();
        });
    }
});
