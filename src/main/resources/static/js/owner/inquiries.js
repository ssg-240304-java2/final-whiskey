// 페이지 로드 문의 내역 목록 로드
$(document).ready(function () {
    const restaurantId = document.getElementById('restaurantId').value;
    const pageNumber = 1;
    loadInquiries(restaurantId, pageNumber);

    $(document).on('click', '#post-inquiry', clickInquiryPost);
    $(document).on('click', '#forward-inquiry', clickInquiryForward);

    // 기존 이벤트 리스너 제거 후 새로 등록
    $('#saveReply').off('click').on('click', saveInquiryReply);
});

// 문의 내역 전체 조회
function loadInquiries(restaurantId, pageNumber) {
    $.ajax({
        url: `/restaurant/${restaurantId}/owner-inquiries?pageNumber=${pageNumber}&pageSize=5`,
        type: 'GET',
        contentType: "application/json",
        success: function (inquiries) {
            const $inquiriesTable = $('#inquiries-table');
            $inquiriesTable.empty();

            inquiries.content.forEach(function (inquiry, index) {
                const tr = $('<tr></tr>');
                tr.append('<td>' + (index + 1) + '</td>');
                tr.append('<td>' + inquiry.writer.name + '</td>');
                tr.append('<td>' + inquiry.content + '</td>');
                tr.append('<td>' + new Date(inquiry.createdAt).toLocaleString() + '</td>');
                
                // 답변 여부에 따라 다른 뱃지 스타일 적용
                const badgeClass = inquiry.reply ? 'badge-answered' : 'badge-unanswered';
                const badgeText = inquiry.reply ? '답변완료' : '미답변';
                tr.append('<td><span class="' + badgeClass + '">' + badgeText + '</span></td>');
                
                // 상세보기 버튼 스타일 변경
                tr.append('<td>' +
                    `<button class="btn-view-inquiry" data-id=${inquiry.id} onclick=inquiryDetail(${inquiry.id})>상세보기</button>` +
                    '</td>');
                $inquiriesTable.append(tr);
            });
            renderPageNumber(inquiries.totalPages, pageNumber);
        },
        error: function (xhr, status, error) {
            console.error("문의 내역을 불러오는 중 오류 발생", status, error);
            alert('문의 내역을 불러오는 데 실패했습니다.');
        }
    });
}

// 문의 내역 상세 보기
function inquiryDetail(inquiryId) {
    $.ajax({
        url: `/restaurant/${inquiryId}/inquiry`,
        type: 'GET',
        contentType: "application/json",
        success: function (inquiry) {
            $('#inquiryWriter').text(inquiry.writer);
            $('#inquiryDate').text(new Date(inquiry.createdAt).toLocaleString());
            $('#inquiryContent').text(inquiry.content);
            $('#inquiryReply').val(inquiry.reply ? inquiry.reply.content : '');
            $('#inquiryModal').modal('show');
            $('#inquiryModal').data('id', inquiryId);
        },
        error: function (xhr, status, error) {
            console.error("문의 상세 정보를 불러오는 중 오류 발생", status, error);
            alert('문의 상세 정보를 불러오는 데 실패했습니다.');
        }
    });
}

// 문의 내역 답변 작성
function saveInquiryReply() {
    const restaurantId = document.getElementById('restaurantId').value;
    const reply = $('#inquiryReply').val();
    const inquiryId = $('#inquiryModal').data('id');

    if (!reply || reply.trim() === '') {
        alert('답변을 입력해주세요.');
        return;
    }

    $.ajax({
        url: `/restaurant/inquiry/reply/${inquiryId}`,
        type: 'POST',
        data: JSON.stringify({
            content: reply
        }),
        contentType: 'application/json',
        success: function () {
            alert('답변이 저장되었습니다: ' + reply);
            const currentPage = getCurrentPage();
            $('#inquiryModal').modal('hide');
            loadInquiries(restaurantId, currentPage);
        },
        error: function (xhr, status, error) {
            console.error("답변을 작성하는 중 오류 발생", status, error);
            alert('답변을 저장하는 데 실패했습니다.');
        }
    });
}

// 페이지네이션 렌더링
function renderPageNumber(totalPages, pageNumber) {
    const pageList = $('#pagination-inquiry');
    pageList.empty();

    const postButton = `<li class="page-item ${pageNumber === 1 ? 'disabled' : ''}" id="post-inquiry">
                                 <a class="page-link" href="#" tabindex="-1">이전</a>
                               </li>`;
    pageList.append(postButton);

    for (let index = 1; index <= totalPages; index++) {
        pageList.append(`<li class="page-item ${pageNumber === index ? 'active' : ''}" id="${index}">
                             <a class="page-link" href="#" onclick="clickInquiryPage(${index})">${index}</a>
                         </li>`);
    }

    const forwardButton = `<li class="page-item ${pageNumber === totalPages ? 'disabled' : ''}" id="forward-inquiry" data-totalPage="${totalPages}">
                                    <a class="page-link" href="#" onclick="clickInquiryForward()">다음</a>
                                  </li>`;
    pageList.append(forwardButton);
}

// 페이지네이션 클릭 이벤트
function clickInquiryPage(pageNumber) {
    const restaurantId = document.getElementById('restaurantId').value;
    loadInquiries(restaurantId, pageNumber);
}

// 이전 페이지로 이동
function clickInquiryPost() {
    const restaurantId = document.getElementById('restaurantId').value;
    const currentPageNumber = getCurrentPage();

    if (currentPageNumber > 1) {
        loadInquiries(restaurantId,currentPageNumber - 1);
    }
}

// 다음 페이지로 이동
function clickInquiryForward() {
    const restaurantId = document.getElementById('restaurantId').value;
    const currentPageNumber = getCurrentPage();
    const totalPageNumber = parseInt($(this).attr('data-totalPage'));

    if (currentPageNumber < totalPageNumber) {
        loadInquiries(restaurantId,currentPageNumber + 1);
    }
}

// 현재 페이지 번호 반환
function getCurrentPage() {
    return parseInt($('.page-item.active').attr('id'));
}