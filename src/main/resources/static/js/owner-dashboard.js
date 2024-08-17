document.addEventListener('DOMContentLoaded', function() {
    const sidebarLinks = document.querySelectorAll('#sidebar .nav-link');
    const contentArea = document.getElementById('content-area');
    const pageTitle = document.getElementById('page-title');

    sidebarLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            sidebarLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');

            const targetId = this.getAttribute('data-bs-target').substring(1);
            pageTitle.textContent = this.textContent.trim();

            // AJAX를 사용하여 해당 프래그먼트 로드
            fetch(`/fragments/owner/${targetId}.html`)
                .then(response => response.text())
                .then(html => {
                    contentArea.innerHTML = html;
                })
                .catch(error => {
                    console.error('Error loading content:', error);
                    contentArea.innerHTML = '<p>콘텐츠를 불러오는 중 오류가 발생했습니다.</p>';
                });
        });
    });

    // 초기 로드 시 대시보드 탭 활성화
    sidebarLinks[0].click();
});
