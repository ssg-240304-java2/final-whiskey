function checkLoginId() {
    const loginId = $('#loginId').val();

    // Ajax를 통해 서버로 로그인 ID 중복 체크 요청
    $.ajax({
        url: '/checkLoginId',  // 서버에서 처리할 엔드포인트
        type: 'POST',
        data: { loginId: loginId },
        success: function(response) {
            console.log("Server response:", response); // 서버 응답을 콘솔에 출력하여 확인
            if (response.exists) {
                alert('이미 사용 중인 로그인 ID입니다.');
                $('#submit-btn').prop('disabled', true);
            } else {
                alert('사용 가능한 로그인 ID입니다.');
                checkPasswordMatch();  // 비밀번호 일치 여부도 함께 확인
            }
        },
        error: function() {
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        }
    });
}

function checkPasswordMatch() {
    const password = $('#password').val();
    const passwordConfirm = $('#passwordConfirm').val();

    if (password !== passwordConfirm) {
        $('#password-match-message').show();
        $('#submit-btn').prop('disabled', true);
    } else {
        $('#password-match-message').hide();
        $('#submit-btn').prop('disabled', false);
    }
}

// 비밀번호 입력 필드의 값이 변경될 때마다 일치 여부 확인
$('#password, #passwordConfirm').on('keyup', function() {
    checkPasswordMatch();
});
