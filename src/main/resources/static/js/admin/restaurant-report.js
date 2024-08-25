$(document).ready(function () {
    let currentSortField = 'reportedAt';
    let currentSortOrder = 'desc';
    let currentPage = 0;
    const pageSize = 10;

    fetchRestaurantReports(currentPage, currentSortField, currentSortOrder);
    initializeSortButtons();

    function fetchRestaurantReports(page, sortField, sortOrder) {
        $.ajax({
            url: /*[[@{/restaurantreport/list}]]*/ '/restaurantreport/list',
            data: { page: page, sortField: sortField, sortOrder: sortOrder },
            type: 'GET',
            success: function (data) {
                renderReportTable(data);
                updatePaginationControls(data.number, data.totalPages);
            },
            error: function (xhr, status, error) {
                handleFetchError(error);
            }
        });
    }

    function renderReportTable(data) {
        let tableBody = $('#restaurantReportList');
        tableBody.empty();
        if (data && data.content && data.content.length > 0) {
            data.content.forEach(function (report) {
                let row = createReportRow(report);
                tableBody.append(row);
            });
        } else {
            tableBody.append('<tr><td colspan="5">신고된 식당이 없습니다.</td></tr>');
        }
    }

    function createReportRow(report) {
        let formattedDate = new Date(report.reportedAt).toLocaleDateString();
        let statusClass = report.checked ? 'report-completed' : '';
        let statusText = report.checked ? '처리 완료' : '처리 전';
        return `
            <tr data-id="${report.id}">
                <td>${report.id}</td>
                <td>${report.restaurant ? report.restaurant.name : 'N/A'}</td>
                <td>${report.title}</td>
                <td>${formattedDate}</td>
                <td class="report-status ${statusClass}" aria-label="처리 상태">${statusText}</td>
            </tr>
        `;
    }

    function updatePaginationControls(currentPage, totalPages) {
        $('#pre').prop('disabled', currentPage === 0);
        $('#back').prop('disabled', currentPage === totalPages - 1);
    }

    function loadRestaurantReportDetail(reportId) {
        $.ajax({
            url: `/restaurantreport/detail/${reportId}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                renderReportDetail(data.report);
            },
            error: function (xhr, status, error) {
                console.error("Error fetching report detail:", error);
                $('#restaurantReportDetail').html('<p>상세 정보를 불러오는 데 실패했습니다.</p>');
            }
        });
    }

    function renderReportDetail(report) {
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

    function updateReport(reportId, btnId) {
        if (confirm("신고처리를 진행하시겠습니까?")) {
            $.ajax({
                url: `/restaurantreport/update/${reportId}`,
                type: "PUT",
                data: { btnId: btnId },
                success: function () {
                    handleReportUpdateSuccess(reportId);
                },
                error: function () {
                    alert('신고처리에 실패했습니다.');
                }
            });
        }
    }

    function handleReportUpdateSuccess(reportId) {
        alert('신고처리 되었습니다.');
        fetchRestaurantReports(currentPage, currentSortField, currentSortOrder);
        loadRestaurantReportDetail(reportId);
        updateReportStatusUI(reportId);
    }

    function updateReportStatusUI(reportId) {
        $(`tr[data-id="${reportId}"] .report-status`)
            .addClass('report-completed')
            .text('처리 완료')
            .attr('aria-label', '처리 상태: 처리 완료');
    }

    function initializeSortButtons() {
        $('.sort-btn').click(function () {
            let newSortField = $(this).data('sort');
            if (newSortField === currentSortField) {
                currentSortOrder = currentSortOrder === 'asc' ? 'desc' : 'asc';
            } else {
                currentSortField = newSortField;
                currentSortOrder = 'desc';
            }
            updateSortButtonsUI();
            fetchRestaurantReports(currentPage, currentSortField, currentSortOrder);
        });
    }

    function updateSortButtonsUI() {
        $('.sort-btn').removeClass('active');
        $(`.sort-btn[data-sort="${currentSortField}"]`).addClass('active');
        $('.sort-icon').removeClass('fa-sort-up fa-sort-down').addClass('fa-sort');
        $(`.sort-btn[data-sort="${currentSortField}"] .sort-icon`)
            .removeClass('fa-sort')
            .addClass(currentSortOrder === 'asc' ? 'fa-sort-up' : 'fa-sort-down');
    }

    function handleFetchError(error) {
        $('#restaurantReportList').html('<tr><td colspan="5">데이터를 불러오는 중 오류가 발생했습니다.</td></tr>');
        console.error("Error fetching restaurant reports:", error);
    }

    // 이벤트 리스너들
    $(document).on('click', '#restaurantReportList tr', function () {
        let reportId = $(this).data('id');
        loadRestaurantReportDetail(reportId);
    });

    $(document).on('click', '#pre', function () {
        if (currentPage > 0) {
            currentPage--;
            fetchRestaurantReports(currentPage, currentSortField, currentSortOrder);
        }
    });

    $(document).on('click', '#back', function () {
        currentPage++;
        fetchRestaurantReports(currentPage, currentSortField, currentSortOrder);
    });

    $(document).on('click', '#punish, #pass', function () {
        let btnId = $(this).attr('id');
        let reportId = $('#reportId').text();
        updateReport(reportId, btnId);
    });
});
