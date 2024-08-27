var restaurantId = 2; // 예시로 고정된 값

$(document).ready(function () {

    const pageNumber = 1;
    loadInquiries(1);

    $(document).on('click', '#post', clickPost);
    $(document).on('click', '#forward', clickForward);

    $('#saveReply').click(saveInquiryReply);
});

// 문의 내역 전체 조회
function loadInquiries(pageNumber) {
    $.ajax({
        type: 'GET',
        url: `/restaurant/${restaurantId}/allInquiry?pageNumber=${pageNumber}&pageSize=5`,
        content: "application/json",
        success: function (inquiries) {
            const $inquiriesTable = $('#inquiries-table');
            $inquiriesTable.empty();

            inquiries.content.forEach(function (inquiry, index) {
                const tr = $('<tr></tr>');
                tr.append('<td>' + (index + 1) + '</td>');
                tr.append('<td>' + inquiry.writer.name + '</td>');
                tr.append('<td>' + inquiry.content + '</td>');
                tr.append('<td>' + new Date(inquiry.createdAt).toLocaleString() + '</td>');
                tr.append('<td>' + (inquiry.reply ? '답변완료' : '미답변') + '</td>');
                tr.append('<td>' +
                    `<button class="btn btn-sm btn-primary view-inquiry" data-id=${inquiry.id} onclick=inquiryDetail(${inquiry.id})>보기</button>` +
                    '</td>');
                $inquiriesTable.append(tr);
            });
            renderPageNumber(inquiries.totalPages, pageNumber);
        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청 실패:", status, error); // 오류 시 콘솔 출력
            alert('문의 내역을 불러오는 데 실패했습니다.');
        }
    });
}

// 문의 내역 상세 보기
function inquiryDetail(inquiryId) {
    $.ajax({
        type: 'GET',
        url: `/restaurant/inquiry/${inquiryId}`,
        content: "application/json",
        success: function (response) {
            console.log(response);
            $('#inquiryWriter').text(response.writer);
            $('#inquiryDate').text(new Date(response.createdAt).toLocaleString());
            $('#inquiryContent').text(response.content);
            $('#inquiryReply').val(response.reply ? response.reply.content : '');
            $('#inquiryModal').modal('show');
            $('#inquiryModal').data('id', inquiryId);
        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청 실패:", status, error);
            alert('문의 상세 정보를 불러오는 데 실패했습니다.');
        }
    });
}

// 문의 내역 답변 작성
function saveInquiryReply() {
    const reply = $('#inquiryReply').val();
    const inquiryId = $('#inquiryModal').data('id');

    alert('답변이 저장되었습니다: ' + reply);
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: `/restaurant/inquiry/reply/${inquiryId}`,
        data: JSON.stringify({content: reply}),
        success: function (response) {
            $('#inquiryModal').modal('hide');   // 모달 닫기
            loadInquiries(getCurrentPage());    // 답변 저장 후 현재 페이지 목록 다시 불러오기
        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청 실패:", status, error);
            alert('답변을 저장하는 데 실패했습니다.');
        }
    });
}

function renderPageNumber(totalPages, pageNumber) {
    const pageList = $('#pagination-inquiry');
    pageList.empty();

    const postButton = `<li class=page-item ${pageNumber === 1 ? 'disabled' : ''} id="post">
                                    <a class="page-link" href="#" tabindex="-1">이전</a>
                                </li>`;
    pageList.append(postButton);

    for (let index = 1; index <= totalPages; index++) {
        pageList.append(`<li class="page-item ${pageNumber === index ? 'active' : ''}" id=${index}>
                                    <a class="page-link" href="#" onclick="clickPage(${index})">${index}</a>
                                </li>`);
    }

    const forwardButton = `<li class="page-item ${pageNumber === totalPages ? 'disabled' : ''}" id="forward" data-totalPage="${totalPages}">
                                    <a class="page-link" href="#" onclick="clickForward()">다음</a>
                                </li>`;
    pageList.append(forwardButton);
}

function clickPage(pageNumber) {
    loadInquiries(pageNumber);
}

function clickPost() {
    const currentPageNumber = getCurrentPage();
    if (currentPageNumber > 1) {
        loadInquiries(currentPageNumber - 1);
    }
}

function clickForward() {
    const currentPageNumber = getCurrentPage();
    const totalPageNumber = parseInt($(this).attr('data-totalPage'));
    if (currentPageNumber < totalPageNumber) {
        loadInquiries(currentPageNumber + 1);
    }
}

function getCurrentPage() {
    return parseInt($('.page-item.active').attr('id'));
}