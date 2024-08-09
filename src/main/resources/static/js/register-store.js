document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('storeRegistrationForm');
    const verifyButton = document.getElementById('verifyBusiness');
    const additionalInfo = document.getElementById('additionalInfo');
    const submitButton = document.getElementById('submitApplication');

    // 사업자 정보 검증 버튼 클릭 이벤트
    verifyButton.addEventListener('click', function() {
        const businessNumber = document.getElementById('businessNumber').value;
        const openingDate = document.getElementById('openingDate').value;
        const businessOwner = document.getElementById('businessOwner').value;


        // TODO: 실제 사업자 정보 검증 API 연동
        // 백엔드 개발자는 여기에 실제 사업자 정보 검증 로직을 구현해야 합니다.
        // 예: axios.post('/api/verify-business', { businessNumber, openingDate, businessOwner })
        if (businessNumber && openingDate && businessOwner) {
            // 임시로 검증 성공으로 처리
            activateAdditionalInfo();
            alert('사업자 정보가 확인되었습니다. 추가 정보를 입력해주세요.');
        } else {
            alert('모든 사업자 정보를 입력해주세요.');
        }
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
        if (validateForm()) {

            // TODO: 실제 입점 신청 API 연동
            // 백엔드 개발자는 여기에 실제 입점 신청 처리 로직을 구현해야 합니다.
            // 예: axios.post('/api/submit-application', formData)
            alert('입점 신청이 완료되었습니다. 관리자 승인을 기다려주세요.');
            form.reset();
            deactivateAdditionalInfo();
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
