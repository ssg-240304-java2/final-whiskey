$(document).ready(function() {
    let isLoginIdChecked = false;

    // 로그인 ID 입력 시 이메일 자동 채우기 및 중복 확인 초기화
    $('#loginId').on('keyup', function() {
        const loginId = $(this).val();
        isLoginIdChecked = false; // 로그인 ID가 변경되면 중복 확인 초기화
        console.log("로그인 ID 입력됨:", loginId);

        // 로그인 ID가 이메일 형식인 경우 이메일 필드에 자동 채우기
        if (loginId.includes('@')) {
            $('#email').val(loginId);
            console.log("이메일 자동 채우기:", loginId);
        } else {
            const domain = '@example.com'; // 기본 도메인 설정
            $('#email').val(loginId + domain);
            console.log("기본 도메인으로 이메일 설정:", loginId + domain);
        }
    });

    // 로그인 ID 중복 확인
    $('#loginIdCheckBtn').on('click', function() {
        const loginId = $('#loginId').val();
        console.log("로그인 ID 중복 확인 버튼 클릭됨");

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
                    console.log("중복된 로그인 ID");
                    $('#loginId-check-message').text('이미 사용 중인 로그인 ID입니다.').css('color', 'red').show();
                    alert('이미 사용 중인 로그인 ID입니다.');
                    isLoginIdChecked = false;
                } else {
                    console.log("사용 가능한 로그인 ID");
                    $('#loginId-check-message').text('사용 가능한 로그인 ID입니다.').css('color', 'green').show();
                    isLoginIdChecked = true;
                }
            },
            error: function() {
                alert('로그인 ID 중복 확인 중 오류가 발생했습니다.');
            }
        });
    });

    // 이메일 인증 코드 발송
    $('#emailVerifyBtn').on('click', function() {
        console.log("이메일 인증 코드 발송 버튼 클릭됨");

        if (!isLoginIdChecked) {
            console.log("로그인 ID 중복 확인되지 않음");
            alert('로그인 ID 중복 확인부터 해주세요.');
            return;
        }

        const email = $('#email').val();

        if (!email) {
            console.log("이메일이 입력되지 않음");
            alert('이메일을 입력하세요.');
            return;
        }

        console.log("이메일:", email);

        $.ajax({
            url: '/api/email/send',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email }),
            success: function(response) {
                console.log("이메일 인증 코드가 전송됨");
                alert('인증 코드가 이메일로 전송되었습니다.');
            },
            error: function() {
                console.log("이메일 인증 코드 전송 중 오류 발생");
                alert('인증 코드 전송 중 오류가 발생했습니다.');
            }
        });
    });

    // 이메일 인증 코드 확인
    $('#verifyCodeBtn').on('click', function() {
        console.log("이메일 인증 코드 확인 버튼 클릭됨");

        if (!isLoginIdChecked) {
            console.log("로그인 ID 중복 확인되지 않음");
            alert('로그인 ID 중복 확인부터 해주세요.');
            return;
        }

        const email = $('#email').val();
        const code = $('#emailVerificationCode').val();

        if (!email || !code) {
            console.log("이메일 또는 인증 코드가 입력되지 않음");
            alert('이메일과 인증 코드를 입력하세요.');
            return;
        }

        console.log("이메일:", email, "코드:", code);

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
                    console.log("인증 성공");
                    alert('인증이 완료되었습니다.');
                    $('#verifyCodeBtn').addClass('verified').text('인증 완료');
                    $('#verifyCodeBtn').prop('disabled', true);
                    $('#submit-btn').prop('disabled', false); // 회원가입 버튼 활성화
                } else {
                    console.log("인증 실패");
                    alert('인증 코드가 올바르지 않습니다.');
                    $('#submit-btn').prop('disabled', true);
                }
            },
            error: function() {
                console.log("인증 확인 중 오류 발생");
                alert('인증 확인 중 오류가 발생했습니다.');
            }
        });
    });

    // 비밀번호 확인
    $('#password, #passwordConfirm').on('keyup', function() {
        const password = $('#password').val();
        const passwordConfirm = $('#passwordConfirm').val();

        if (password !== passwordConfirm) {
            console.log("비밀번호 불일치");
            $('#password-match-message').show();
            $('#submit-btn').prop('disabled', true);
        } else {
            console.log("비밀번호 일치");
            $('#password-match-message').hide();
            $('#submit-btn').prop('disabled', false);
        }
    });
});
