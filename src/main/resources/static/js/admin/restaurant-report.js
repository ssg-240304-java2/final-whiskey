
$(document).ready(function () {
    let currentSortOrder = 'asc';
    let currentPage = 0;
    const pageSize = 10;

    fetchRestaurantReports(currentPage, currentSortOrder);

    function fetchRestaurantReports(page, sortOrder = 'asc') {
        $.ajax({
            url: /*[[@{/restaurantreport/list}]]*/ '/restaurantreport/list',
            data: { page: page, sortOrder: sortOrder },
            type: 'GET',
            success: function (data) {
                let tableBody = $('#restaurantReportList');
                tableBody.empty();
                if (data && data.content && data.content.length > 0) {
                    data.content.forEach(function (report) {
                        let formattedDate = new Date(report.reportedAt).toLocaleDateString();
                        let row = `
                                          <tr data-id="${report.id}">
                                              <td>${report.id}</td>
                                              <td>${report.restaurant ? report.restaurant.name : 'N/A'}</td>
                                              <td>${report.title}</td>
                                              <td>${formattedDate}</td>
                                              <td>${report.checked ? '처리 완료' : '처리 전'}</td>
                                          </tr>
                                      `;
                        tableBody.append(row);
                    });
                    updatePaginationControls(data.number, data.totalPages);
                } else {
                    tableBody.append('<tr><td colspan="5">신고된 식당이 없습니다.</td></tr>');
                }
            },
            error: function (xhr, status, error) {
                $('#restaurantReportList').html('<tr><td colspan="5">데이터를 불러오는 중 오류가 발생했습니다.</td></tr>');
            }
        });
    }

    // 페이지네이션 컨트롤 업데이트
    function updatePaginationControls(currentPage, totalPages) {
        $('#pre').prop('disabled', currentPage === 0);
        $('#back').prop('disabled', currentPage === totalPages - 1);
    }

    // 식당 신고 상세 정보 로드
    function loadRestaurantReportDetail(reportId) {
        $.ajax({
            url: `/restaurantreport/detail/${reportId}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                let report = data.report;
                let detailHtml = `
                                <h6>신고 번호: <span id="reportId">${report.id}</span></h6>
                                <p>식당 이름: <span id="restaurantTitle">${report.restaurant.name}</span></p>
                                <p>제목: <span id="title">${report.title}</span></p>
                                <p>내용: <span id="content">${report.content}</span></p>
                                <p>신고일: <span id="reportedAt">${new Date(report.reportedAt).toLocaleDateString()}</span></p>
                                <p>처리여부: <span id="checked">${report.checked ? '처리완료' : '처리 전'}</span></p>
                                <button type="button" class="btn btn-danger" id="punish">처벌</button>
                                <button type="button" class="btn btn-warning" id="pass">보류</button>
                            `;
                $('#restaurantReportDetail').html(detailHtml);
            }
        });
    }

    // 신고 처리 함수
    function updateReport(reportId, btnId) {
        if (confirm("신고처리를 진행하시겠습니까?")) {
            $.ajax({
                url: `/restaurantreport/update/${reportId}`,
                type: "PUT",
                data: { btnId: btnId },
                success: function () {
                    alert('신고처리 되었습니다.');
                    fetchRestaurantReports(currentPage, currentSortOrder);
                    loadRestaurantReportDetail(reportId);
                },
                error: function () {
                    alert('신고처리에 실패했습니다.');
                }
            });
        }
    }

    // 이벤트 리스너들
    $(document).on('click', '#restaurantReportList tr', function () {
        let reportId = $(this).data('id');
        loadRestaurantReportDetail(reportId);
    });

    $(document).on('click', '#reportDate', function () {
        currentSortOrder = currentSortOrder === 'asc' ? 'desc' : 'asc';
        fetchRestaurantReports(currentPage, currentSortOrder);
    });

    $(document).on('click', '#pre', function () {
        if (currentPage > 0) {
            currentPage--;
            fetchRestaurantReports(currentPage, currentSortOrder);
        }
    });

    $(document).on('click', '#back', function () {
        currentPage++;
        fetchRestaurantReports(currentPage, currentSortOrder);
    });

    $(document).on('click', '#punish, #pass', function () {
        let btnId = $(this).attr('id');
        let reportId = $('#reportId').text();
        updateReport(reportId, btnId);
    });

    // 초기 로드
    fetchRestaurantReports(currentPage, currentSortOrder);
});
