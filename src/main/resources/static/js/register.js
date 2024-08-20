$(document).ready(function() {
    // 이메일 인증 코드 전송 버튼 클릭 이벤트
    $('#emailVerifyBtn').on('click', function() {
        const email = $('#email').val();

        if (!email) {
            alert('이메일을 입력하세요.');
            return;
        }

        $.ajax({
            url: '${{secrets.MAIL_URL}}', // POST 요청을 보낼 URL
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email }),
            success: function(response) {
                alert('인증 코드가 이메일로 전송되었습니다.');
                $('#emailVerifyBtn').addClass('verified').text('인증 완료');
                $('#emailVerifyBtn').prop('disabled', true);
                $('#emailVerificationCode').prop('disabled', false); // 인증 코드 입력 필드 활성화
            },
            error: function(error) {
                alert('인증 코드 전송 중 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });

    // 인증 코드 확인 버튼 클릭 이벤트
    $('#verifyCodeBtn').on('click', function() {
        const email = $('#email').val();
        const code = $('#emailVerificationCode').val();

        if (!email || !code) {
            alert('이메일과 인증 코드를 입력하세요.');
            return;
        }

        $.ajax({
            url: '${{secrets.MAIL_URL}}', // GET 요청을 보낼 URL
            method: 'GET',
            data: { key: email, code: code }, // 이메일과 인증 코드를 쿼리 파라미터로 보냄
            success: function(response) {
                if (response) {
                    alert('인증이 완료되었습니다.');
                    $('#verifyCodeBtn').addClass('verified').text('인증 완료');
                    $('#verifyCodeBtn').prop('disabled', true);
                    $('#submit-btn').prop('disabled', false); // 회원가입 버튼 활성화
                } else {
                    alert('인증 코드가 올바르지 않습니다.');
                    $('#submit-btn').prop('disabled', true);
                }
            },
            error: function(error) {
                alert('인증 확인 중 오류가 발생했습니다. 다시 시도해주세요.');
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
