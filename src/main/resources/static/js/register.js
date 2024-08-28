$(document).ready(function() {
    let isLoginIdChecked = false;

    // 로그인 ID 입력 시 이메일 자동 채우기
    $('#loginId').on('keyup', function() {
        const loginId = $(this).val();
        isLoginIdChecked = false; // 로그인 ID가 변경되면 중복확인 초기화

        // 로그인 ID가 이메일 형식인 경우 이메일 필드에 자동 채우기
        if (loginId.includes('@')) {
            $('#email').val(loginId);
        } else {
            const domain = '@example.com'; // 기본 도메인 설정
            $('#email').val(loginId + domain);
        }

        $('#emailVerifyBtn').prop('disabled', true); // 중복 확인 안되면 이메일 인증 버튼 비활성화
    });

    // 로그인 ID 중복 확인
    $('#loginIdCheckBtn').on('click', function() {
        const loginId = $('#loginId').val();

        if (!loginId) {
            showAlert('로그인 ID를 입력하세요.');
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
                    $('#emailVerifyBtn').prop('disabled', true); // 이메일 인증 버튼 비활성화
                    isLoginIdChecked = false;
                } else {
                    $('#loginId-check-message').text('사용 가능한 로그인 ID입니다.').css('color', 'green').show();
                    $('#submit-btn').prop('disabled', false);
                    $('#emailVerifyBtn').prop('disabled', false); // 이메일 인증 버튼 활성화
                    isLoginIdChecked = true;
                }
            },
            error: function() {
                showAlert('로그인 ID 중복 확인 중 오류가 발생했습니다.');
            }
        });
    });

    // 이메일 인증 코드 발송
    $('#emailVerifyBtn').on('click', function() {
        if (!isLoginIdChecked) {
            showAlert('로그인 ID 중복 확인을 먼저 해주세요.');
            return;
        }

        const email = $('#email').val();

        if (!email) {
            showAlert('이메일을 입력하세요.');
            return;
        }

        $.ajax({
            url: '/api/email/send',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email }),
            success: function(response) {
                showAlert('인증 코드가 이메일로 전송되었습니다.');
            },
            error: function() {
                showAlert('인증 코드 전송 중 오류가 발생했습니다.');
            }
        });
    });

    // 이메일 인증 코드 확인
    $('#verifyCodeBtn').on('click', function() {
        if (!isLoginIdChecked) {
            showAlert('로그인 ID 중복 확인을 먼저 해주세요.');
            return;
        }

        const email = $('#email').val();
        const code = $('#emailVerificationCode').val();

        if (!email || !code) {
            showAlert('이메일과 인증 코드를 입력하세요.');
            return;
        }

        $.ajax({
            url: '/api/email/verify',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email,
                code: code // 인증 코드도 서버로 전달
            }),
            success: function(response) {
                if (response === code) {
                    showAlert('인증이 완료되었습니다.');
                    $('#verifyCodeBtn').addClass('verified').text('인증 완료');
                    $('#verifyCodeBtn').prop('disabled', true);
                    $('#submit-btn').prop('disabled', false); // 회원가입 버튼 활성화
                } else {
                    showAlert('인증 코드가 올바르지 않습니다.');
                    $('#submit-btn').prop('disabled', true);
                }
            },
            error: function() {
                showAlert('인증 확인 중 오류가 발생했습니다.');
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

    // 초기 로딩 시 이메일 인증 버튼 비활성화
    $('#emailVerifyBtn').prop('disabled', true);
});

// 비동기 알림 처리 함수
function showAlert(message) {
    // 간단하게 alert로 메시지 표시
    alert(message);
}
