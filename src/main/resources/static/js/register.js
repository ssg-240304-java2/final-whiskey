$(document).ready(function() {
    // 로그인 ID 중복 확인 로직
    $('#loginIdCheckBtn').on('click', function() {
        const loginId = $('#loginId').val();

        if (!loginId) {
            alert('로그인 ID를 입력하세요.');
            return;
        }

        $.ajax({
            url: '/checkLoginId', // 서버에서 로그인 ID 중복 확인하는 API 엔드포인트
            method: 'POST',
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: { loginId: loginId },
            success: function(response) {
                if (response.exists) {
                    $('#loginId-check-message').show().text('이미 사용 중인 로그인 ID입니다.');
                    $('#submit-btn').prop('disabled', true);
                } else {
                    $('#loginId-check-message').show().css('color', 'green').text('사용 가능한 로그인 ID입니다.');
                    $('#submit-btn').prop('disabled', false);
                }
            },
            error: function(error) {
                console.error('에러:', error);
                alert('로그인 ID 확인 중 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });

    // 비밀번호 확인 로직
    $('#password, #passwordConfirm').on('keyup', function() {
        const password = $('#password').val();
        const passwordConfirm = $('#passwordConfirm').val();

        if (password.length > 0 && password !== passwordConfirm) {
            $('#password-match-message').show().text('비밀번호가 일치하지 않습니다.');
            $('#submit-btn').prop('disabled', true);
        } else {
            $('#password-match-message').hide();
            $('#submit-btn').prop('disabled', false);
        }
    });
});
