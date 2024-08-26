var restaurantId = 2; // 예시로 고정된 값

$(document).ready(function() {

    loadInquiries(1);
$(document).on('click', '#post', clickPost);
$(document).on('click', '#forward', clickForward);
    // 문의 삭제
    $('.delete-inquiry').click(function() {
        var inquiryId = $(this).data('id');
        $('#deleteConfirmModal').modal('show');
        $('#confirmDelete').data('id', inquiryId);
    });

    // 삭제 확인
    $('#confirmDelete').click(function() {
        var inquiryId = $(this).data('id');
        // TODO: 백엔드에 문의 삭제 요청을 보내는 AJAX 요청
        alert('문의 ' + inquiryId + '번이 삭제되었습니다.');
        $('#deleteConfirmModal').modal('hide');
    });

    // 답변 저장
    $('#saveReply').click(saveInquiryReply);
});

function loadInquiries(pageNumber) {
    $.ajax({
        type: 'GET',
        url: `/restaurant/${restaurantId}/allInquiry?pageNumber=${pageNumber}&pageSize=5`,
        content: "application/json",
        success: function(inquiries) {
            //TODO: 삭제해야함
            console.log(inquiries);
            console.log(pageNumber);

            const $inquiriesTable = $('#inquiries-table');
            $inquiriesTable.empty();
            // 문의 목록을 테이블에 추가
            inquiries.content.forEach(function(inquiry, index) {
                const tr = $('<tr></tr>');
                tr.append('<td>' + (index + 1) + '</td>');
                tr.append('<td>' + inquiry.content + '</td>');
                tr.append('<td>' + inquiry.writer.name + '</td>');
                tr.append('<td>' + inquiry.createdAt + '</td>');
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

function inquiryDetail(inquiryId) {
    // TODO: 백엔드에서 문의 상세 정보를 가져오는 AJAX 요청
    $.ajax({
        type: 'GET',
        url: `/restaurant/inquiry/${inquiryId}`,
        content: "application/json",
        success: function(response) {
            console.log(response);
            $('#inquiryWriter').text(response.writer);
            $('#inquiryDate').text(response.createdAt);
            $('#inquiryContent').text(response.content);
            $('#inquiryReply').val(response.reply ? response.reply.content : '');
            $('#inquiryModal').modal('show');
            $('#inquiryModal').data('id', inquiryId);
        }
    })

    // 답변 여부에 따라 UI 조정
    if ($(this).closest('tr').find('td:eq(4)').text() === '답변완료') {
        $('#inquiryReply').val('이미 답변된 내용입니다.').prop('disabled', true);
        $('#saveReply').hide();
    } else {
        $('#inquiryReply').val('').prop('disabled', false);
        $('#saveReply').show();
    }

    $('#inquiryModal').modal('show');
}

function saveInquiryReply() {
    const reply = $('#inquiryReply').val();
    const inquiryId = $('#inquiryModal').data('id');
    // TODO: 백엔드에 답변을 저장하는 AJAX 요청
    alert('답변이 저장되었습니다: ' + reply);
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: `/restaurant/inquiry/reply/${inquiryId}`,
            data: JSON.stringify({content: reply}),
            success: function(response) {
                loadInquiries();
            }
        }
    )
    $('#inquiryModal').modal('hide');
}

function renderPageNumber(totalPages, pageNumber) {
    const pageList = $('#pagination-inquiry');
    pageList.empty();

    const postButton = `<li class=page-item ${1 === pageNumber ? 'disabled' : ''} id=post>
                                    <a class="page-link" href="#" tabindex="-1">이전</a>
                                </li>`;
    pageList.append(postButton);

    for (let i = 1; i <= totalPages; i++) {
        pageList.append( `<li class="page-item ${i === pageNumber ? 'active' : ''}" id=${i}>
                                    <a class="page-link" onclick="clickPage(${i})">${i}</a>
                                </li>`);
    }

    const forwardButton = `<li class="page-item ${totalPages === pageNumber ? 'disabled' : ''}" id="forward" data-totalPage="${totalPages}">
                                    <a class="page-link" href="#">다음</a>
                                </li>`;
    pageList.append(forwardButton);
}

function clickPage(pageNumber) {
    loadInquiries(pageNumber);
}

function clickPost() {
    const currentPageNumber = getCurrentPage();
    if (currentPageNumber === 1) {
        return;
    }
    loadInquiries(currentPageNumber - 1);
}

function clickForward() {
    const currentPageNumber = getCurrentPage();
    const totalPageNumber = parseInt($(this).attr('data-totalPage'));
    if (currentPageNumber === totalPageNumber) {
        return;
    }
    loadInquiries(currentPageNumber + 1);
}

function getCurrentPage() {
    return $('.page-item-active').attr('id');
}