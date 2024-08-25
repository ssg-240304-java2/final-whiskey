$(document).ready(function() {

loadInquiries();
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


function loadInquiries() {

    const restaurantId = 2;

    $.ajax({
        type: 'GET',
        url: `/restaurant/${restaurantId}/inquiry`,
        content: "application/json",
        success: function(response) {
            console.log(response);
            const $inquiriesTable = $('#inquiries-table');
            $inquiriesTable.empty();
            // 문의 목록을 테이블에 추가
            response.forEach(function(inquiry, index) {
                const tr = $('<tr></tr>');
                tr.append('<td>' + (index + 1) + '</td>');
                tr.append('<td>' + inquiry.content + '</td>');
                tr.append('<td>' + inquiry.writer + '</td>');
                tr.append('<td>' + inquiry.createdAt + '</td>');
                tr.append('<td>' + (inquiry.reply ? '답변완료' : '미답변') + '</td>');
                tr.append('<td>' +
                    `<button class="btn btn-sm btn-primary view-inquiry" data-id=${inquiry.id} onclick=inquiryDetail(${inquiry.id})>보기</button>` +
                    '</td>');
                $inquiriesTable.append(tr);
            });
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
