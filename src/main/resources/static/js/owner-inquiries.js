$(document).ready(function() {
    // 문의 상세 보기
    $('.view-inquiry').click(function() {
        var inquiryId = $(this).data('id');
        // TODO: 백엔드에서 문의 상세 정보를 가져오는 AJAX 요청
        // 더미 데이터로 모달 내용 채우기
        $('#inquiryTitle').text('음식 품질에 대한 문의');
        $('#inquiryAuthor').text('홍길동');
        $('#inquiryDate').text('2024-05-20');
        $('#inquiryContent').text('음식의 맛과 품질이 일정하지 않은 것 같습니다. 개선 방안이 있나요?');
        
        // 답변 여부에 따라 UI 조정
        if ($(this).closest('tr').find('td:eq(4)').text() === '답변완료') {
            $('#inquiryReply').val('이미 답변된 내용입니다.').prop('disabled', true);
            $('#saveReply').hide();
        } else {
            $('#inquiryReply').val('').prop('disabled', false);
            $('#saveReply').show();
        }
        
        $('#inquiryModal').modal('show');
    });

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
    $('#saveReply').click(function() {
        var reply = $('#inquiryReply').val();
        // TODO: 백엔드에 답변을 저장하는 AJAX 요청
        alert('답변이 저장되었습니다: ' + reply);
        $('#inquiryModal').modal('hide');
    });
});
