// DOM이 로드되면 실행
document.addEventListener('DOMContentLoaded', function() {
    // 사이드바 링크와 콘텐츠 영역, 페이지 제목 요소 선택
    const sidebarLinks = document.querySelectorAll('#sidebar .nav-link');
    const contentArea = document.getElementById('content-area');
    const pageTitle = document.getElementById('page-title');

    // 각 사이드바 링크에 클릭 이벤트 리스너 추가
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            // 모든 링크에서 'active' 클래스 제거 후 클릭된 링크에 추가
            sidebarLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');

            // 클릭된 링크의 대상 ID와 텍스트 가져오기
            const targetId = this.getAttribute('data-bs-target').substring(1);
            pageTitle.textContent = this.textContent.trim();

            // AJAX를 사용하여 해당 프래그먼트 로드
            fetch(`/fragments/owner/${targetId}.html`)
                .then(response => response.text())
                .then(html => {
                    // 로드된 HTML을 콘텐츠 영역에 삽입
                    contentArea.innerHTML = html;
                })
                .catch(error => {
                    // 오류 발생 시 콘솔에 로그 출력 및 오류 메시지 표시
                    console.error('Error loading content:', error);
                    contentArea.innerHTML = '<p>콘텐츠를 불러오는 중 오류가 발생했습니다.</p>';
                });
        });
    });

    // 초기 로드 시 첫 번째 사이드바 링크(대시보드) 클릭
    sidebarLinks[0].click();
});
