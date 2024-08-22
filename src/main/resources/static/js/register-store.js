document.addEventListener('DOMContentLoaded', function() {

    const form = document.getElementById('storeRegistrationForm');
    const verifyButton = document.getElementById('verifyBusiness');
    const additionalInfo = document.getElementById('additionalInfo');
    const submitButton = document.getElementById('submitApplication');

    var businessNumber;
    var openingDate;
    var businessOwner;

    var memberId;
    var storeName;
    var phoneNumber;
    var storeAddress;
    var storeCategory;

// 카테고리 선택하면 그 값으로 변경하는 이벤트 로직
    const selectElement = document.getElementById('storeCategory');
    selectElement.addEventListener('change', function() {
        storeCategory = selectElement.value;
        console.log(storeCategory);
    });



    // 사업자 정보 검증 버튼 클릭 이벤트
    verifyButton.addEventListener('click', function() {

        businessNumber = document.getElementById('businessNumber').value;
        openingDate = document.getElementById('openingDate').value;
        businessOwner = document.getElementById('businessOwner').value;

        $.ajax({
            url: '/businessregister/valid',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                "businesses": [
                    {
                        "b_no": businessNumber,
                        "start_dt": openingDate,
                        "p_nm": businessOwner
                    }
                ]
            }),
            success: function(result) {
                console.log(result);
                if (result === 'success') {
                    alert('사업자 정보가 확인되었습니다. 추가 정보를 입력해주세요.');
                    activateAdditionalInfo();

                } else {
                    alert('사업자 정보가 일치하지 않습니다.');
                }
            },
            error: function() {
                alert('사업자 정보 검증에 실패했습니다.');
            }
        });
    });




    // 추가 정보 입력 필드 활성화 함수
    function activateAdditionalInfo() {
        additionalInfo.classList.remove('disabled');
        const inputs = additionalInfo.querySelectorAll('input, select');
        inputs.forEach(input => input.disabled = false);
        submitButton.disabled = false;
    }


// 폼 제출 이벤트
    form.addEventListener('submit', function(e) {
        e.preventDefault();

        memberId = 43;
        storeName = document.getElementById('storeName').value;
        phoneNumber = document.getElementById('phoneNumber').value;
        storeAddress = document.getElementById('storeAddress').value;

        if (validateForm()) {

            // 동기적으로 POST 요청을 보내기 위해 XMLHttpRequest 사용
            const xhr = new XMLHttpRequest();
            xhr.open('POST', '/businessregister/regist', false); // false로 설정하면 동기적 요청이 됩니다.
            xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

            // 서버로 보낼 데이터 구성
            const data = JSON.stringify({
                memberId: memberId,
                name: storeName,
                phone: phoneNumber,
                address: storeAddress,
                category: storeCategory
            });

            // 요청 보내기
            xhr.send(data);

            if (xhr.status === 200) { // 요청이 성공적으로 완료된 경우
                alert('입점 신청이 완료되었습니다. 관리자 승인을 기다려주세요.');
                form.reset();
                deactivateAdditionalInfo();
            } else {
                alert('입점 신청에 실패하였습니다. 다시 시도해주세요.');
            }
        }
    });

    // 폼 유효성 검사 함수
    function validateForm() {
        const requiredFields = form.querySelectorAll('[required]');
        for (let field of requiredFields) {
            if (!field.value) {
                alert('모든 필수 항목을 입력해주세요.');
                return false;
            }
        }
        return true;
    }

    // 추가 정보 입력 필드 비활성화 함수
    function deactivateAdditionalInfo() {
        additionalInfo.classList.add('disabled');
        const inputs = additionalInfo.querySelectorAll('input, select');
        inputs.forEach(input => {
            input.disabled = true;
            input.value = '';
        });
        submitButton.disabled = true;
    }
});
