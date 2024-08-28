// 페이지 로드 시 공지사항 목록 로드
$(document).ready(function () {
    const restaurantId = document.getElementById('restaurantId').value;

    loadNotices(restaurantId, 1);

    $(document).on('click', '[name=delete-notice]', deleteNotice);
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
            noticeList.empty(); // 기존 목록 초기화

            notices.content.forEach(notice => {
                const noticeCard = `
                                <div class="notice-card ${notice.isDeleted ? '삭제됨' : ''}" id="${notice.id}">
                                    <div class="notice-content">
                                        <h3>${notice.title}</h3>
                                        <p>${notice.content}</p>
                                        <div class="notice-footer">
                                            <span class="notice-date">${new Date(notice.createdAt).toLocaleString()}</span>
                                            ${!notice.isDeleted ? `<button class="btn btn-sm btn-outline-danger" id="${notice.id}" name="delete-notice"><i class="fas fa-trash"></i> 삭제</button>` : ''}
                                        </div>
                                    </div>
                                </div>`;

                noticeList.append(noticeCard);
            });

            renderPageNumber(notices.totalPages, pageNumber);
        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청 실패:", status, error); // 오류 시 콘솔 출력
            alert('공지사항 목록을 불러오는 데 실패했습니다.');
        }
    });
}

// 공지사항 작성
function createNotice() {
    // 음식점 id 값 가져오기
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
        data: JSON.stringify({title, content}),
        contentType: 'application/json',
        success: function (response) {
            console.log('공지사항 작성 성공:', response);
            alert('공지사항이 작성되었습니다.');
            loadNotices(restaurantId, 1);
            closeModal();
        },
        error: function (xhr, status, error) {
            console.error('AJAX 요청 실패:', status, error);
            alert('공지사항 작성에 실패했습니다.');
        }
    });
}

// 공지사항 삭제
function deleteNotice() {
    const restaurantId = document.getElementById('restaurantId').value;
    const noticeId = $(this).attr('id');
    // todo: 시간남으면 커스텀 모달창으로 삭제 해보기
    // $('#deleteConfirmModal').modal('show');
    // $('#deleteNoticeId').val(noticeId);

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
                console.error('AJAX 요청 실패:', status, error);
                alert('공지사항 삭제 확인에 실패했습니다.');
            }
        });
    }
}

function closeModal() {
    $('#createNoticeModal').modal('hide');
    $('.modal-backdrop').remove();
    $('#title').val('');
    $('#content').val('');
}

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

    const forwardButton = `<li class="page-item ${totalPages === pageNumber ? 'disabled' : ''}" id="forward-notice" data-totalPage="${totalPages}">
                                    <a class="page-link" href="#" onclick="clickNoticeForward()">다음</a>
                                </li>`;
    pageList.append(forwardButton);
}

function clickNoticePage(pageNumber) {
    const restaurantId = document.getElementById('restaurantId').value;
    loadNotices(restaurantId, pageNumber);
}

function clickNoticePost() {
    const restaurantId = document.getElementById('restaurantId').value;
    const currentPageNumber = getCurrentPage();

    if (currentPageNumber > 1) {
        loadNotices(restaurantId, currentPageNumber - 1);
    }
}

function clickNoticeForward() {
    const restaurantId = document.getElementById('restaurantId').value;
    const currentPageNumber = getCurrentPage();
    const totalPageNumber = parseInt($(this).attr('data-totalPage'));

    if (currentPageNumber < totalPageNumber) {
        loadNotices(restaurantId, currentPageNumber + 1);
    }
}

function getCurrentPage() {
    return parseInt($('.page-item.active').attr('id'));
}
