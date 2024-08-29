$(document).ready(function () {
    // user-management 링크 클릭 시 데이터 로드
    $('[data-target="user-management"]').on('click', function(e) {
        e.preventDefault();
        loadMembers(0); // 첫 페이지를 로드

        // 회원 목록을 로드하는 함수
        function loadMembers(page) {
            $.ajax({
                url: '/admin/user-management',
                type: 'GET',
                data: { page: page },
                success: function(data) {
                    console.log(data); // 서버에서 반환된 데이터를 콘솔에 출력

                    const contentArea = $('#content-area');
                    contentArea.empty();

                    // HTML 구조 생성
                    let htmlContent = `
                        <div class="card">
                            <div class="card-body">
                                <table class="table table-hover">
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

                    // 회원 데이터를 테이블에 추가
                    data.content.forEach(function(member) {
                        htmlContent += `
                            <tr>
                                <td>${member.id}</td>
                                <td>${member.name}</td>
                                <td>${member.email}</td>
                                <td>${new Date(member.createdAt).toLocaleDateString()}</td>
                                <td>
                                    <span class="${member.active ? 'badge badge-success' : 'badge badge-secondary'}">
                                        ${member.active ? '활성' : '비활성'}
                                    </span>
                                </td>
                            </tr>`;
                    });

                    htmlContent += `
                                    </tbody>
                                </table>
                                <nav aria-label="Page navigation">
                                    <ul class="pagination justify-content-center">`;

                    // 이전 페이지 버튼
                    if (data.number > 0) {
                        htmlContent += `
                                <li class="page-item">
                                    <a class="page-link" href="#" data-page="${data.number - 1}">이전</a>
                                </li>`;
                    }

                    // 페이지 번호 버튼들
                    for (let i = 0; i < data.totalPages; i++) {
                        htmlContent += `
                                <li class="page-item ${i === data.number ? 'active' : ''}">
                                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                                </li>`;
                    }

                    // 다음 페이지 버튼
                    if (data.number < data.totalPages - 1) {
                        htmlContent += `
                                <li class="page-item">
                                    <a class="page-link" href="#" data-page="${data.number + 1}">다음</a>
                                </li>`;
                    }

                    htmlContent += `
                                    </ul>
                                </nav>
                            </div>
                        </div>`;

                    // 생성한 HTML을 contentArea에 삽입
                    contentArea.html(htmlContent);
                    $('#page-title').text('회원 관리');
                },
                error: function(error) {
                    console.error('Error loading user management:', error);
                }
            });
        }

        // 페이지네이션 클릭 이벤트 처리
        $(document).on('click', '.page-link', function(e) {
            e.preventDefault();
            const page = $(this).data('page');
            loadMembers(page);
        });
    });
});