$(document).ready(function() {
    // 로그인 ID 입력 시 이메일 자동 채우기
    $('#loginId').on('keyup', function() {
        const loginId = $(this).val();

        // 로그인 ID가 이메일 형식이 아닌 경우 자동으로 이메일 필드에 채우기
        if (loginId.includes('@')) {
            $('#email').val(loginId);
        } else {
            const domain = '@example.com'; // 기본 도메인 설정
            $('#email').val(loginId + domain);
        }
    });

    // 로그인 ID 중복 확인
    $('#loginIdCheckBtn').on('click', function() {
        const loginId = $('#loginId').val();

        if (!loginId) {
            alert('로그인 ID를 입력하세요.');
            return;
        }

        $.ajax({
            url: '/checkLoginId',
            type: 'POST',
            data: { loginId: loginId },
            success: function(response) {
                if (response.exists) {
                    $('#loginId-check-message').text('이미 사용 중인 로그인 ID입니다.').css('color', 'red').show();
                    $('#submit-btn').prop('disabled', true);
                } else {
                    $('#loginId-check-message').text('사용 가능한 로그인 ID입니다.').css('color', 'green').show();
                    $('#submit-btn').prop('disabled', false);
                }
            },
            error: function() {
                alert('로그인 ID 중복 확인 중 오류가 발생했습니다.');
            }
        });
    });

    // 비밀번호 확인
    $('#password, #passwordConfirm').on('keyup', function() {
        const password = $('#password').val();
        const passwordConfirm = $('#passwordConfirm').val();

        if (password !== passwordConfirm) {
            $('#password-match-message').show();
            $('#submit-btn').prop('disabled', true);
        } else {
            $('#password-match-message').hide();
            $('#submit-btn').prop('disabled', false);
        }
    });
});
