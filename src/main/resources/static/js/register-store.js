document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('storeRegistrationForm');
    const verifyButton = document.getElementById('verifyBusinessNumber');
    const inputMethod = document.getElementById('inputMethod');

    verifyButton.addEventListener('click', function() {
        const businessNumber = document.getElementById('businessNumber').value;
        // TODO: 사업자 등록번호 확인 API 연동
        // - API 엔드포인트: /api/verify-business-number
        // - 메소드: POST
        // - 요청 데이터: { businessNumber: string }
        // - 응답 데이터: { isValid: boolean, message: string }
        alert('사업자 등록번호 확인 로직이 구현되어야 합니다.');
    });

    inputMethod.addEventListener('change', function() {
        if (this.value === 'ocr') {
            // TODO: OCR 기능 구현
            // - 카메라 또는 이미지 업로드 기능 추가
            // - OCR API 연동 (/api/ocr-business-number)
            // - OCR 결과를 businessNumber 입력란에 자동 입력
            alert('OCR 기능이 구현되어야 합니다.');
        }
    });

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        // TODO: 폼 제출 로직 구현
        // - API 엔드포인트: /api/register-store
        // - 메소드: POST
        // - 요청 데이터: FormData 객체 사용 (파일 업로드 포함)
        // - 응답 처리: 성공 시 완료 메시지 표시, 실패 시 에러 메시지 표시
        alert('입점 신청이 완료되었습니다. 관리자 승인을 기다려주세요.');
    });

    // TODO: 폼 유효성 검사 함수 구현
    // - 모든 필수 필드가 채워져 있는지 확인
    // - 사업자 등록번호 형식 검증
    // - 파일 업로드 여부 확인

    // TODO: 주소 검색 기능 구현
    // - 카카오 주소 API 또는 다음 주소 API 연동
    // - 주소 검색 결과를 주소 입력란에 자동 입력
});
