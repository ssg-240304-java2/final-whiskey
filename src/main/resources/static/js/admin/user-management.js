$(document).ready(function () {
    function loadMembers(page) {
        $.ajax({
            url: '/admin/user-management',
            type: 'GET',
            data: { page: page },
            success: function (data) {
                const contentArea = $('#content-area');
                contentArea.empty();

                let htmlContent = `
                    <div class="card">
                        <div class="card-body">
                            <table class="table report-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>이름</th>
                                        <th>이메일</th>
                                        <th>가입일</th>
                                        <th>상태</th>
                                    </tr>
                                </thead>
                                <tbody>`;

                data.content.forEach(function (member) {
                    htmlContent += `
                        <tr>
                            <td>${member.id}</td>
                            <td>${member.name}</td>
                            <td>${member.email}</td>
                            <td>${new Date(member.createdAt).toLocaleDateString()}</td>
                            <td>
                                <span class="${member.active ? 'badge badge-success' : 'badge badge-secondary'}">
                                    ${member.active ? '활성' : '탈퇴'}
                                </span>
                            </td>
                        </tr>`;
                });

                htmlContent += `
                                </tbody>
                            </table>
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center" id="userManagementPagination">`;

                // 페이지네이션 처리
                htmlContent += generatePagination(data.number, data.totalPages);

                htmlContent += `
                                </ul>
                            </nav>
                        </div>
                    </div>`;

                contentArea.html(htmlContent);
                $('#page-title').text('사용자 관리');

                // 페이지네이션 클릭 이벤트 처리 (이벤트 위임)
                $('#userManagementPagination .page-link').on('click', function (e) {
                    e.preventDefault(); // 클릭 시 페이지 이동 방지
                    const page = $(this).data('page');
                    loadMembers(page);
                });
            },
            error: function (error) {
                console.error('Error loading user management:', error);
            }
        });
    }

    // 페이지네이션 버튼 생성 함수
    function generatePagination(currentPage, totalPages) {
        let paginationHtml = '';

        // 이전 페이지 버튼
        if (currentPage > 0) {
            paginationHtml += `
                <li class="page-item">
                    <a class="page-link" href="#" data-page="${currentPage - 1}" aria-label="Previous">
                        <span aria-hidden="true">«</span>
                    </a>
                </li>`;
        }

        // 페이지 번호 버튼들
        let startPage = Math.max(0, currentPage - 2);
        let endPage = Math.min(totalPages - 1, currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            paginationHtml += `
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>`;
        }

        // 다음 페이지 버튼
        if (currentPage < totalPages - 1) {
            paginationHtml += `
                <li class="page-item">
                    <a class="page-link" href="#" data-page="${currentPage + 1}" aria-label="Next">
                        <span aria-hidden="true">»</span>
                    </a>
                </li>`;
        }

        return paginationHtml;
    }

    // user-management 링크 클릭 시 데이터 로드
    $('[data-target="user-management"]').on('click', function (e) {
        loadMembers(0); // 첫 페이지를 로드
    });
});
