// 페이지 로드 시 공지사항 목록 로드
$(document).ready(function () {
    const restaurantId = document.getElementById('restaurantId').value;
    const pageNumber = 1;

    loadNotices(restaurantId, pageNumber);

    $(document).on('click', '#post-notice', clickNoticePost);
    $(document).on('click', '#forward-notice', clickNoticeForward);
});

// 공지사항 전체 조회
function loadNotices(restaurantId, pageNumber) {
    $.ajax({
        url: `/restaurant/${restaurantId}/owner-notices?pageNumber=${pageNumber}&pageSize=5`,
        type: 'GET',
        success: function (notices) {
            const noticeList = $('.notice-list');
            noticeList.empty();

            notices.content.forEach(notice => {
                const noticeCard = `
                                <div class="notice-card ${notice.isDeleted ? '삭제됨' : ''}" id="${notice.id}">
                                    <div class="notice-content">
                                        <h3>${notice.title}</h3>
                                        <p>${notice.content}</p>
                                        <div class="notice-footer">
                                            <span class="notice-date">${new Date(notice.createdAt).toLocaleString()}</span>
                                            ${!notice.isDeleted ? `<button class="btn btn-sm btn-outline-danger" id="${notice.id}" name="delete-notice" onclick="deleteNotice(this)"><i class="fas fa-trash"></i> 삭제</button>` : ''}
                                        </div>
                                    </div>
                                </div>`;
                noticeList.append(noticeCard);
            });
            renderPageNumber(notices.totalPages, pageNumber);
        },
        error: function (xhr, status, error) {
            console.error("공지사항 목록 불러오는 중 오류 발생", status, error);
            alert('공지사항 목록을 불러오는 데 실패했습니다.');
        }
    });
}

// 공지사항 작성
function createNotice() {
    const restaurantId = document.getElementById('restaurantId').value;
    const title = $('#title').val();
    const content = $('#content').val();

    if (!title) {
        alert('제목을 입력해주세요.');
        return;
    }

    if (!content) {
        alert('내용을 입력해주세요.');
        return;
    }

    $.ajax({
        url: `/restaurant/${restaurantId}/notice`,
        type: 'POST',
        data: JSON.stringify({
            title,
            content
        }),
        contentType: 'application/json',
        success: function () {
            alert('공지사항이 작성되었습니다.');
            closeModal();
            loadNotices(restaurantId, 1);
        },
        error: function (xhr, status, error) {
            console.error('공지사항 작성하는 중 오류 발생', status, error);
            alert('공지사항 작성에 실패했습니다.');
        }
    });
}

// 공지사항 삭제
function deleteNotice(deleteButton) {
    const restaurantId = document.getElementById('restaurantId').value;
    const noticeId = $(deleteButton).attr('id');

    if (confirm('정말로 삭제하시겠습니까?')) {
        $.ajax({
            url: `/restaurant/${noticeId}/notice`,
            type: 'DELETE',
            success: function () {

                alert('공지사항이 삭제되었습니다.');
                let currentPage = getCurrentPage();
                if ($('.notice-list .notice-card').length === 1) {
                    currentPage -= 1;
                }
                loadNotices(restaurantId, currentPage);
            },
            error: function (xhr, status, error) {
                console.error('공지사항 삭제하는 중 오류 발생', status, error);
                alert('공지사항 삭제 확인에 실패했습니다.');
            }
        });
    }
}

// 모달 닫기
function closeModal() {
    $('#createNoticeModal').modal('hide');
    $('.modal-backdrop').remove();
    $('#title').val('');
    $('#content').val('');
}

// 페이지네이션 렌더링
function renderPageNumber(totalPages, pageNumber) {
    const pageList = $('#pagination-notice');
    pageList.empty();

    const postButton = `<li class="page-item ${pageNumber === 1 ? 'disabled' : ''}" id="post-notice">
                                 <a class="page-link" href="#" tabindex="-1">이전</a>
                               </li>`;
    pageList.append(postButton);

    for (let index = 1; index <= totalPages; index++) {
        pageList.append(`<li class="page-item ${pageNumber === index ? 'active' : ''}" id="${index}">
                             <a class="page-link" href="#" onclick="clickNoticePage(${index})">${index}</a>
                         </li>`);
    }

    const forwardButton = `<li class="page-item ${pageNumber === totalPages ? 'disabled' : ''}" id="forward-notice" data-totalPage="${totalPages}">
                                    <a class="page-link" href="#" onclick="clickNoticeForward()">다음</a>
                                </li>`;
    pageList.append(forwardButton);
}

// 페이지네이션 클릭 이벤트
function clickNoticePage(pageNumber) {
    const restaurantId = document.getElementById('restaurantId').value;
    loadNotices(restaurantId, pageNumber);
}

// 이전 페이지로 이동
function clickNoticePost() {
    const restaurantId = document.getElementById('restaurantId').value;
    const currentPageNumber = getCurrentPage();

    if (currentPageNumber > 1) {
        loadNotices(restaurantId, currentPageNumber - 1);
    }
}

// 다음 페이지로 이동
function clickNoticeForward() {
    const restaurantId = document.getElementById('restaurantId').value;
    const currentPageNumber = getCurrentPage();
    const totalPageNumber = parseInt($(this).attr('data-totalPage'));

    if (currentPageNumber < totalPageNumber) {
        loadNotices(restaurantId, currentPageNumber + 1);
    }
}

// 현재 페이지 번호 반환
function getCurrentPage() {
    return parseInt($('.page-item.active').attr('id'));
}
